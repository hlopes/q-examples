package org.hlopes.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class FooService {

  @Inject
  Charlie charlie;

  @ConfigProperty(name="bar")
  boolean bar;

  public String ping(){
    return bar? charlie.ping() : "nok";
  }
}
