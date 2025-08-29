package org.acme.quartz;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@ApplicationScoped
public class QuartzJobBean implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        performTask();
    }

    @Transactional
    void performTask() {
        Task task = new Task();

        task.persist();

        Log.info("### Persisted task: "+ task.createdAt);
    }
}
