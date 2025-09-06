package org.hlopes;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.hlopes.quartz.Task;

import java.util.List;

@Path("/tasks")
public class TaskResource {

    @GET
    public List<Task> listAll() {
        return Task.listAll();
    }
}
