package org.hlopes.mocks;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.hlopes.services.GreetingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.jboss.resteasy.reactive.RestResponse.StatusCode;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
public class MockSpyGreetingServiceTest {

  private static final Logger log = LoggerFactory.getLogger(MockSpyGreetingServiceTest.class);
  @InjectSpy
  GreetingService greetingService;

  @Test
  void testWithSpyDefaultGreeting() {
    given().when().get("/hello").then().statusCode(StatusCode.OK).body(is("ola"));

    Mockito.verify(greetingService, Mockito.times(1)).greet();
  }

  @Test
  void testOverride() {
    doReturn("hi").when(greetingService).greet();

    given().when().get("/hello").then().statusCode(StatusCode.OK).body(is("hi"));
  }
}
