package org.hlopes.resources;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(MyWireMockResource.class)
public class TestWireMock {

  @InjectWireMock
  WireMockServer wireMockServer;

  @Test
  void test() {
  }
}
