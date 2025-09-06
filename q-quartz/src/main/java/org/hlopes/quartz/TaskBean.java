package org.hlopes.quartz;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TaskBean {

    @Transactional
//    @Scheduled(every = "10s", identity = "task-job")
    void schedule() {
        Task task = new Task();
        task.persist();

        Log.info("### Persisted a new task " + task.createdAt.toString());
    }
}
