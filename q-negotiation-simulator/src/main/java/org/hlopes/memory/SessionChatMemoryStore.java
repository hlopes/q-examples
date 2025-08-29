package org.hlopes.memory;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.hlopes.helper.SessionHelper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;

@ApplicationScoped
public class SessionChatMemoryStore implements ChatMemoryStore {

    // thread safe storage for session messages
    private final Map<String, List<ChatMessage>> sessionMessages = new ConcurrentHashMap<>();
    private final Map<String, ReadWriteLock> sessionLocks = new ConcurrentHashMap<>();

    @Override
    public List<ChatMessage> getMessages(final Object memoryId) {
        String sessionId = memoryId.toString();

        Log.infof("Getting messages for session: %s", sessionId);

        ReadWriteLock lock = SessionHelper.getSessionLock(sessionLocks, sessionId);
        lock.readLock().lock();

        try {
            List<ChatMessage> messages = sessionMessages.getOrDefault(sessionId, Collections.emptyList());
            Log.infof("Retrieved %d messages for session: %s", messages.size(), sessionId);

            return new ArrayList<>(messages);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void updateMessages(final Object memoryId, final List<ChatMessage> messages) {
        String sessionId = memoryId.toString();

        Log.infof("Updating messages for session: %s, message count: %d", sessionId, messages.size());

        ReadWriteLock lock = SessionHelper.getSessionLock(sessionLocks, sessionId);
        lock.writeLock().lock();

        try {
            sessionMessages.put(sessionId, new ArrayList<>(messages));
            Log.infof("Updated messages for session: %s. Total sessions in store: %d",
                    sessionId,
                    sessionMessages.size());
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void deleteMessages(final Object memoryId) {
        String sessionId = memoryId.toString();

        Log.infof("Deleting request for session: %s - PRESERVING session but keeping messages for LLM memory",
                sessionId);

        // DO NOT delete the session or clear messages - this preserves conversation
        // history for LLM
        // The AI framework calls this after each conversation turn, but we want to keep
        // both:
        // 1. The session entry (for /api/sessions endpoint)
        // 2. The message history (for LLM context)

        ReadWriteLock lock = SessionHelper.getSessionLock(sessionLocks, sessionId);
        lock.readLock().lock();

        try {
            int currentMessages = sessionMessages.getOrDefault(sessionId, Collections.emptyList()).size();
            Log.infof("Preserved session: %s with %d messages for continued LLM context. Total sessions: %d",
                    sessionMessages,
                    currentMessages,
                    sessionMessages);
        } finally {
            lock.readLock().unlock();
        }

    }

    public Set<String> getActiveSessions() {
        Set<String> sessions = new HashSet<>(sessionMessages.keySet());
        Log.infof("getActiveSessions() called. Found %d active sessions: %s", sessions.size(), sessions);

        return sessions;
    }

    public int getMessageCount(String sessionId) {
        ReadWriteLock lock = SessionHelper.getSessionLock(sessionLocks, sessionId);
        lock.readLock().lock();

        try {
            int count = sessionMessages.getOrDefault(sessionId, Collections.emptyList()).size();
            Log.infof("Message count for session %s: %d", sessionId, count);

            return count;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void forceDeleteSession(final String sessionId) {
        Log.infof("Force deleting session: %s", sessionId);

        ReadWriteLock lock = SessionHelper.getSessionLock(sessionLocks, sessionId);
        lock.writeLock().lock();

        try {
            sessionMessages.remove(sessionId);
            sessionLocks.remove(sessionId);
            Log.warnf("Force deleted session: %s. Remaining sessions: %d", sessionId, sessionMessages.size());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void clearAllSessions() {
        Log.infof("Clearing all sessions. Current count: %d", sessionMessages.size());
        sessionMessages.clear();
        sessionLocks.clear();
        Log.info("Cleared all sessions.");
    }
}
