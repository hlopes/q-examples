package org.hlopes.restclient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/")
@RegisterRestClient
@ApplicationScoped
public interface GreetingClient {

  @GET
  @Path("/hello")
  @Produces(MediaType.TEXT_PLAIN)
  String hello();
}
