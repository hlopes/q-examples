package org.acme.scheduler;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/count")
public class CountResource {

    CounterBean counter;

    public CountResource(CounterBean counterBean) {
        this.counter = counterBean;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "count: " + counter.get();
    }
}
