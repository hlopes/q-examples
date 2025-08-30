package org.hlopes.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import org.hlopes.events.UserRegistrationEvent;

@ApplicationScoped
public class AuditService {

    public void onUserRegistered(@Observes(during = TransactionPhase.AFTER_SUCCESS) UserRegistrationEvent event) {
        Log.infof("Auditing: user registered %s", event.username());
    }
}
