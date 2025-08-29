package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.quartz.QuartzTaskBean;
import org.quartz.SchedulerException;

@Path("/quartz")
public class QuartzResource {
    QuartzTaskBean quartzTaskBean;

    public QuartzResource(QuartzTaskBean quartzTaskBean) {
        this.quartzTaskBean = quartzTaskBean;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void getQuartz() throws SchedulerException {
        quartzTaskBean.schedule("myTrigger");

    }
}
