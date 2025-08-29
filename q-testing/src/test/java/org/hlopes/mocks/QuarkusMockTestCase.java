package org.hlopes.mocks;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class QuarkusMockTestCase {

  @Inject
  MockableBean1 mockableBean1;

  @Inject
  MockableBean2 mockableBean2;

  @BeforeAll
  public static void setup() {
    MockableBean1 mock = Mockito.mock(MockableBean1.class);
    Mockito.when(mock.greet("Stuart")).thenReturn("A mock for Stuart");
    QuarkusMock.installMockForType(mock, MockableBean1.class);
  }

  @Test
  public void testBeforeAll() {
    assertEquals("A mock for Stuart", mockableBean1.greet("Stuart"));
    assertEquals("Hello Stuart", mockableBean2.greet("Stuart"));
  }

  @Test
  public void testPerTestMock() {
    QuarkusMock.installMockForInstance(new BonjourGreeter(), mockableBean2);
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


  public static class BonjourGreeter extends MockableBean2 {
    @Override
    public String greet(String name) {
      return "Bonjour " + name;
    }
  }
}
