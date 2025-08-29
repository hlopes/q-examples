package org.hlopes.services;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class GreetingServiceTest {

  @Inject
  GreetingService greetingService;

  @Test
  void testGreetService() {
    assertEquals("ola", greetingService.greet());
  }
}
