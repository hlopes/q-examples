package org.hlopes.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.hlopes.events.UserRegistrationEvent;

@ApplicationScoped
public class EmailService {

    @Retry(maxRetries = 3, delay = 2000)
    public void sendWelcomeEmail(@ObservesAsync final UserRegistrationEvent event) {
        // Simulate sending an email
        Log.infof("Async Sending welcome email to %s" + event.username());
    }
}
