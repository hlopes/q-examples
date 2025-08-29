package org.hlopes.restclient;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.hamcrest.CoreMatchers;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
public class GreetingClientTest {
  @InjectMock
  @RestClient
  GreetingClient greetingClient;

  @Test
  void testHelloEndpoint() {
    Mockito.when(greetingClient.hello()).thenReturn("hello from mockito");

    RestAssured.given().when().get("/hello").then().statusCode(RestResponse.StatusCode.OK)
        .body(CoreMatchers.is("hello from mockito"));
  }
}
