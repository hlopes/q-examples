package org.hlopes.mocks;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.ApplicationScoped;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class InjectMockTestCase {

  @InjectMock
  MockableBean1 mockableBean1;

  @InjectMock
  MockableBean2 mockableBean2;

  @BeforeEach
  public void setup() {
    Mockito.when(mockableBean1.greet("Stuart")).thenReturn("A mock for Stuart");
  }

  @Test
  public void firstTest() {
    assertEquals("A mock for Stuart", mockableBean1.greet("Stuart"));
    assertEquals(null, mockableBean2.greet("Stuart"));
  }

  @Test
  public void secondTest() {
    Mockito.when(mockableBean2.greet("Stuart")).thenReturn("Bonjour Stuart");
    assertEquals("A mock for Stuart", mockableBean1.greet("Stuart"));
    assertEquals("Bonjour Stuart", mockableBean2.greet("Stuart"));
  }

  @ApplicationScoped
  public static class MockableBean1 {

    public String greet(String name) {
      return "Hello " + name;
    }
  }


  @ApplicationScoped
  public static class MockableBean2 {

    public String greet(String name) {
      return "Hello " + name;
    }
  }
}
