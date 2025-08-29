package org.hlopes;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Resource class that defines a REST endpoint for greetings.
 */
@Path("/hello")
public class GreetingResource {

  /**
   * Hello endpoint that returns a simple greeting message.
   */
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String hello() {
    return "Hello from Quarkus REST";
  }
}
