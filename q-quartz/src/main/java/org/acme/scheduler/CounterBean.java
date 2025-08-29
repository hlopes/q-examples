package org.acme.scheduler;

import io.quarkus.logging.Log;
import io.quarkus.scheduler.ScheduledExecution;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class CounterBean {
    private AtomicInteger counter = new AtomicInteger();

    public int get() {
        return counter.get();
    }

    //    @Scheduled(every = "10s")
    void increment() {
        counter.incrementAndGet();
        Log.info("### Every 10 s");
    }

    //    @Scheduled(cron = "0 15 10 * * ?")
    void cronJob(ScheduledExecution execution) {
        counter.incrementAndGet();
        Log.info("### " + execution.getScheduledFireTime());
    }

    //    @Scheduled(cron = "{cron.expr}")
    void cronJobWithExpressionInConfig() {
        counter.incrementAndGet();
        Log.info("### Cron expression configured in application.properties");
    }
}
