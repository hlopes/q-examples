package org.hlopes.resources;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;

public class MyWireMockResource implements QuarkusTestResourceLifecycleManager {

  WireMockServer wireMockServer;


  @Override
  public Map<String, String> start() {
    wireMockServer = new WireMockServer(8090);
    wireMockServer.start();

    return Map.of("some.service.url", "localhost:" + wireMockServer.port());
  }

  @Override
  public void stop() {
    if (wireMockServer != null) {
      wireMockServer.stop();
      wireMockServer = null;
    }
  }

  @Override
  public void inject(TestInjector testInjector) {
    testInjector.injectIntoFields(wireMockServer,
        new TestInjector.AnnotatedAndMatchesType(InjectWireMock.class, WireMockServer.class));
  }
}
