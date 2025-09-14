package org.hlopes.helper;

import io.quarkus.logging.Log;
import jakarta.ws.rs.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hlopes.model.Session;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionHelper {

    public static Session getSessionOrThrow(final Map<String, Session> sessions, final String sessionId) {
        Session session = sessions.get(sessionId);

        if (session == null) {
            throw new NotFoundException("Session not found: " + sessionId);
        }

        return session;
    }

    public static ReadWriteLock getSessionLock(final Map<String, ReadWriteLock> sessionLocks, final String sessionId) {
        return sessionLocks.computeIfAbsent(sessionId, k -> {
            Log.debugf("Creating new lock for session: %s", sessionId);

            return new ReentrantReadWriteLock();
        });
    }
}
