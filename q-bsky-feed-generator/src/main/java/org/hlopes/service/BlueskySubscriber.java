package org.hlopes.service;

import java.net.URI;
import java.time.OffsetDateTime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.flywaydb.core.internal.util.JsonUtils;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;

import com.fasterxml.jackson.databind.JsonNode;
import io.vertx.core.Vertx;
import io.vertx.core.http.WebSocketClient;
import io.vertx.core.http.WebSocketClientOptions;
import io.vertx.core.http.WebSocketConnectOptions;

@ApplicationScoped
public class BlueskySubscriber {

  @Inject
  Vertx vertx;

  private WebSocketClient wsClient;

  void onStart(@Observes StartupEvent event) {
    var firehoseUrl = "wss://jetstream2.us-east.bsky.network/subscribe?wantedCollections=app.bsky.feed.post";
    var description = "Jetstream2 US-East (Official)";

    WebSocketClientOptions options = new WebSocketClientOptions().setSsl(true).setVerifyHost(false).setTrustAll(true);
    wsClient = vertx.createWebSocketClient(options);

    connectToFirehose(firehoseUrl, description);
  }

  private void connectToFirehose(String url, String description) {
    Log.infof("Connecting to firehose: %s: %s", description, url);

    try {
      URI uri = URI.create(url);
      WebSocketConnectOptions connectOptions = new WebSocketConnectOptions().setHost(uri.getHost()).setPort(
        uri.getPort() == -1 ? 443 : uri.getPort()).setURI(uri.getPath() + (uri.getQuery() != null ? "?" + uri.getQuery()
                                                                                                  : "")).setSsl(true);

      wsClient.connect(connectOptions).onSuccess(ws -> {
        Log.infof("Connected to firehose: %s: %s", description, url);

        // setup message handler
        ws.textMessageHandler(msg -> {
          vertx.executeBlocking(() -> {
            handleTextMessage(msg);

            return null;
          }, false);
        });

        ws.exceptionHandler(e -> {
          Log.errorf(e, "Error on firehose: %s: %s", description, url);
          vertx.setTimer(5000, timer -> connectToFirehose(url, description));
        });

        ws.closeHandler(e -> {
          Log.infof("Disconnected from firehose: %s: %s", description, url);
          vertx.setTimer(5000, timer -> connectToFirehose(url, description));
        });

        Log.infof("Listening for real-time Bluesky posts...");
      }).onFailure(e -> {
        Log.errorf(e, "Error connecting to firehose: %s: %s", description, url);
        vertx.setTimer(10000, timer -> connectToFirehose(url, description));
      });
    }
    catch (Exception e) {
      Log.errorf(e, "Error connecting to firehose: %s: %s", description, url);
    }
  }

  @Transactional
  void processJetstreamEvent(String json) throws Exception {
    JsonNode root = JsonUtils.getJsonMapper().readTree(json);

    if (!"commit".equals(root.path("kind").asText())) {
      return;
    }

    JsonNode commit = root.path("commit");

    if (!"create".equals(commit.path("operation").asText()) || !"app.bsky.feed.post".equals(commit.path("collection").asText())) {
      return;
    }

    // Extract post text and creation timestamp
    var text = commit.path("record").path("text").asText();
    var createdAtStr = commit.path("record").path("createdAt").asText();

    if (text.isBlank() || !text.contains("#Java")) {
      return;
    }

    if (!isTechnicalPost(text)) {
      return;
    }

    // Extract metadata
    OffsetDateTime createdAt = OffsetDateTime.parse(createdAtStr);
    int hour = createdAt.getHour();
    String frameworks = findFrameworks(text);
    String hashtags = extractHashtags(text);
    String links = extractLinks(text);
    String language = detectLanguage(text);

  }

  private void handleTextMessage(String msg) {
    try {
      processJetstreamEvent(msg);
    }
    catch (Exception e) {
      Log.errorf(e, "Error handling text message: %s", msg);
    }
  }
}
