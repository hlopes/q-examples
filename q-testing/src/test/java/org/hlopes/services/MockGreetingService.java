package org.hlopes.services;

import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class MockGreetingService extends GreetingService {

  @Override
  public String greet() {
    return "ola";
  }
}
