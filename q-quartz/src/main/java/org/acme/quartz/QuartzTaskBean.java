package org.acme.quartz;

import jakarta.enterprise.context.ApplicationScoped;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

@ApplicationScoped
public class QuartzTaskBean {

    private final Scheduler quartz;

    public QuartzTaskBean(Scheduler quartz) {
        this.quartz = quartz;
    }

    public void schedule(final String triggerName) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(QuartzJobBean.class)
                                  .withIdentity(triggerName, "myGroup")
                                  .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                                        .withIdentity(triggerName, "myGroup")
                                        .startNow()
                                        .withSchedule(
                                            SimpleScheduleBuilder.simpleSchedule()
                                                                 .withIntervalInSeconds(10)
                                                                 .repeatForever()
                                        )
                                        .build();

        quartz.scheduleJob(job, trigger);
    }
}
