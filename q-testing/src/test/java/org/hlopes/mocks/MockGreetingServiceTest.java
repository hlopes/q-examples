package org.hlopes.mocks;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.hlopes.services.GreetingService;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.jboss.resteasy.reactive.RestResponse.StatusCode;
import static org.mockito.Mockito.when;

@QuarkusTest
public class MockGreetingServiceTest {

  @InjectMock
  GreetingService greetingService;

  @Test
  void testGreeting() {
    when(greetingService.greet()).thenReturn("hi");

    given().when().get("/hello").then().statusCode(StatusCode.OK).body(is("hi"));
  }
}
