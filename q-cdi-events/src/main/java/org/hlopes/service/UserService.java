package org.hlopes.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import org.hlopes.events.UserRegistrationEvent;

@ApplicationScoped
public class UserService {

    @Inject
    Event<UserRegistrationEvent> userRegistered;

    public void registerUser(final String username) {
        Log.infof("Registering user: %s", username);

        // Fire the event
        userRegistered.fire(new UserRegistrationEvent(username));
    }
}
