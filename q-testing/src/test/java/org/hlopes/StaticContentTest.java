package org.hlopes;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import io.quarkus.logging.Log;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class StaticContentTest {

    @TestHTTPResource("index.html")
    URL url;

    @TestHTTPEndpoint(GreetingResource.class)
    @TestHTTPResource
    URL greetingResourceUrl;

    @Test
    void testIndexHtml() {
        try (InputStream in = url.openStream()) {
            String contents = new String(in.readAllBytes(), StandardCharsets.UTF_8);

            assertTrue(contents.contains("<title>Testing Guide</title>"));
        } catch (Exception e) {
            Log.error(e.getMessage());
        }
    }

    @Test
    void testGreetingResource() {
        try (InputStream in = greetingResourceUrl.openStream()) {
            String contents = new String(in.readAllBytes(), StandardCharsets.UTF_8);

            assertTrue(contents.contains("hello"));
        } catch (Exception e) {
            Log.error(e.getMessage());
        }
    }
}
