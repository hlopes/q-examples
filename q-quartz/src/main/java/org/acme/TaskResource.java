package org.acme;

import java.util.List;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.acme.quartz.Task;

@Path("/tasks")
public class TaskResource {

    @GET
    public List<Task> listAll() {
        return Task.listAll();
    }
}
