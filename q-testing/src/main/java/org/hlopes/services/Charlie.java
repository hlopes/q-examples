package org.hlopes.services;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Charlie {

  public String ping() {
    return "Ping from Charlie";
  }
}
