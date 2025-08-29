package org.hlopes.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.hlopes.dtos.CreateSessionRequest;
import org.hlopes.dtos.Feedback;
import org.hlopes.dtos.SessionInfo;
import org.hlopes.dtos.UserMessageRequest;
import org.hlopes.helper.JSONHelper;
import org.hlopes.helper.SessionHelper;
import org.hlopes.memory.SessionChatMemoryStore;
import org.hlopes.model.Message;
import org.hlopes.model.PersonalityType;
import org.hlopes.model.Scenario;
import org.hlopes.model.Session;
import org.hlopes.service.FeedbackAssistant;
import org.hlopes.service.ManagerAssistant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Path("/api")
public class SessionResource {

    private static final String USER_ROLE = "user";
    private static final String AI_ROLE = "ai";

    // in-memory active sessions store
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Inject
    ManagerAssistant managerAssistant;

    @Inject
    FeedbackAssistant feedbackAssistant;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    SessionChatMemoryStore memoryStore;

    @GET
    @Path("/personalities")
    public PersonalityType[] getPersonalities() {
        return PersonalityType.values();
    }

    @GET
    @Path("/scenarios")
    public Scenario[] getScenarios() {
        return Scenario.values();
    }

    /**
     * Create a new conversation session with specified personality and scenario.
     */
    @POST
    @Path("/sessions")
    public Session startSession(CreateSessionRequest request) {
        Log.infof("Creating new session with userId: %s, personality: %s, scenario: %s",
                request != null ? request.userId() : null,
                request != null ? request.personality() : null,
                request != null ? request.scenario() : null);

        checkValidRequest(request);

        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, request.userId().trim(), request.personality(), request.scenario());

        sessions.put(sessionId, session);

        Log.infof("Session created successfully. ID: %s, User: %s, Total sessions: %d",
                sessionId,
                request.userId(),
                sessions.size());
        Log.infof("Active sessions in memory store: %d", memoryStore.getActiveSessions().size());

        return session;
    }

    /**
     * Send a message to the AI manager and get a response.
     */
    @POST
    @Path("/sessions/{sessionId}/messages")
    public Message sendMessage(@PathParam("sessionId") String sessionId, UserMessageRequest messageRequest) {
        Log.infof("Sending message to session %s: content length: %d",
                sessionId,
                messageRequest != null && messageRequest.content() != null ? messageRequest.content().length() : 0);

        checkValidMessage(messageRequest);

        Session session = SessionHelper.getSessionOrThrow(sessions, sessionId);
        Log.infof("Found session: %s, personality: %s, scenario: %s",
                sessionId,
                session.personality().name(),
                session.scenario().name());

        // Log user message
        assert messageRequest != null;
        session.addMessage(new Message(USER_ROLE, messageRequest.content()));
        Log.infof("Added User message to session log. Total messages in session: %d", session.messages().size());

        // Get AI response with automatic memory management via @MemoryId
        Log.infof("Calling AI assistant for session: %s, user: %s", sessionId, session.userName());
        String aiResponse = managerAssistant.chat(sessionId,
                messageRequest.content(),
                session.personality().getSystemPrompt(),
                session.scenario().getPrompt(),
                session.userName());

        Log.infof("AI response received, Length: %d characters", aiResponse != null ? aiResponse.length() : 0);
        Log.infof("Active sessions in memory store after AI call: %d", memoryStore.getActiveSessions().size());

        // Log AI message
        Message aiMessage = new Message(AI_ROLE, aiResponse);
        session.addMessage(aiMessage);
        Log.infof("Added AI message to session log. Total messages in session: %d", session.messages().size());

        return aiMessage;

    }

    /**
     * Get performance feedback analysis for a completed conversation.
     */
    @GET
    @Path("/sessions/{sessionId}/feedback")
    public Feedback getFeedback(@PathParam("sessionId") String sessionId) {
        Session session = SessionHelper.getSessionOrThrow(sessions, sessionId);

        Log.infof("Getting feedback for session: %s, user: %s", sessionId, session.userName());
        Log.infof("Session messages count: %d", session.messages().size());
        Log.infof("Memory store messages count: %d", memoryStore.getMessageCount(sessionId));

        if (session.messages().isEmpty()) {
            return new Feedback(null, List.of(), List.of("No conversation to analyze"));
        }

        // Format conversation history for analysis with better role labeling
        String conversationHistory = session.messages().stream().map(msg -> {
            String role = "user".equals(msg.sender()) ? session.userName() : "Manager";

            return role + ": " + msg.content();
        }).collect(Collectors.joining("\n"));

        Log.infof("Conversation history for feedback analysis (length: %d chars): %s",
                conversationHistory.length(),
                conversationHistory.length() > 200
                ? conversationHistory.substring(0, 200) + "..."
                : conversationHistory);

        // Get AI-generated feedback with user context
        Log.infof("Calling Feedback assistant for session: %s, user: %s", sessionId, session.userName());
        String feedbackJson = feedbackAssistant.analyze(conversationHistory,
                session.scenario().name(),
                session.personality().name(),
                session.userName());

        Log.infof("Received feedback JSON (length: %d chars): %s",
                feedbackJson != null ? feedbackJson.length() : 0,
                feedbackJson != null ? feedbackJson.length() > 300
                                       ? feedbackJson.substring(0, 300) + "..."
                                       : feedbackJson : "");

        try {
            // Try to parse the response as-is first
            return objectMapper.readValue(feedbackJson, Feedback.class);
        } catch (Exception e) {
            Log.warnf("Failed to parse feedback JSON directly: %s", e.getMessage());

            // If direct parsing fails, try to extract JSON from the response
            try {
                String extractedJson = JSONHelper.extractJsonFromResponse(feedbackJson);
                Log.infof("Extracted JSON: %s", extractedJson);

                return objectMapper.readValue(extractedJson, Feedback.class);
            } catch (Exception ex) {
                Log.errorf("Failed to extract and parse JSON: %s", ex.getMessage());

                return new Feedback(null,
                        List.of("Conversation completed"),
                        List.of("Unable to generate detailed feedback. Raw response: " +
                                (feedbackJson.length() > 200 ? feedbackJson.substring(0, 200) + "..." : feedbackJson)));
            }

        }
    }

    // Session management endpoints

    /**
     * Get all active sessions with their basic information.
     */
    @GET
    @Path("/sessions")
    public List<SessionInfo> getAllSessions() {
        Set<String> activeSessions = memoryStore.getActiveSessions();
        Set<String> sessionMapKeys = sessions.keySet();

        Log.infof("GET /sessions called. Sessions in map: %d, Sessions in memory store: %d",
                sessionMapKeys.size(),
                activeSessions.size());
        Log.infof("Session map keys: %s", sessionMapKeys);
        Log.infof("Memory store session keys: %s", activeSessions);

        // Combine sessions from both sources
        Set<String> allSessionIds = new HashSet<>(sessionMapKeys);
        allSessionIds.addAll(activeSessions);

        Log.infof("Total unique sessions to return: %d", allSessionIds.size());

        return allSessionIds.stream().map(sessionId -> {
            Session session = sessions.get(sessionId);
            int messageCount = memoryStore.getMessageCount(sessionId);

            Log.debugf("Processing session %s: session exists=%s, messageCount=%d",
                    sessionId,
                    session != null,
                    messageCount);

            if (session != null) {
                return new SessionInfo(sessionId,
                        session.userName(),
                        session.personality().name(),
                        session.scenario().name(),
                        session.status(),
                        messageCount,
                        session.createdAt());
            } else {
                // Memory exists but session metadata is missing
                Log.warnf("Orphaned session found in memory store: %s", sessionId);

                return new SessionInfo(sessionId, "Unknown User", "Unknown", "Unknown", "ORPHANED", messageCount, null);
            }
        }).toList();
    }

    /**
     * Get detailed information about a specific session.
     */
    @GET
    @Path("/sessions/{sessionId}")
    public SessionInfo getSession(@PathParam("sessionId") String sessionId) {
        Session session = sessions.get(sessionId);
        int messageCount = memoryStore.getMessageCount(sessionId);

        if (session == null && messageCount == 0) {
            throw new NotFoundException("Session not found: " + sessionId);
        }

        if (session != null) {
            return new SessionInfo(sessionId,
                    session.userName(),
                    session.personality().name(),
                    session.scenario().name(),
                    session.status(),
                    messageCount,
                    session.createdAt());
        } else {
            return new SessionInfo(sessionId, "Unknown User", "Unknown", "Unknown", "ORPHANED", messageCount, null);
        }
    }

    /**
     * Delete a session and its memory.
     */
    @DELETE
    @Path("/sessions/{sessionId}")
    public void deleteSession(@PathParam("sessionId") String sessionId) {
        Log.infof("API Request to delete session: %s", sessionId);

        // Remove from both session storage and memory store
        sessions.remove(sessionId);
        memoryStore.forceDeleteSession(sessionId);

        Log.infof("Session %s deleted successfully", sessionId);
    }

    /**
     * Clear all sessions (useful for testing/maintenance).
     */
    @DELETE
    @Path("/sessions")
    public void clearAllSessions() {
        Log.infof("API Request to clear all sessions. Sessions in map: %d, Sessions in memory store: %d",
                sessions.size(),
                memoryStore.getActiveSessions().size());

        sessions.clear();
        memoryStore.clearAllSessions();

        Log.infof("All sessions cleared successfully.");
    }

    /**
     * Debug endpoint to show current session state.
     */
    @GET
    @Path("/sessions/debug")
    public Map<String, Object> getSessionDebugInfo() {
        Set<String> sessionMapKeys = sessions.keySet();
        Set<String> memoryStoreKeys = memoryStore.getActiveSessions();

        Map<String, Object> debugInfo = new HashMap<>();
        debugInfo.put("sessionInMap", sessionMapKeys.size());
        debugInfo.put("sessionMapKeys", sessionMapKeys);
        debugInfo.put("sessionInMemoryStore", memoryStoreKeys.size());
        debugInfo.put("memoryStoreKeys", memoryStoreKeys);

        // Detailed session info
        Map<String, Object> sessionDetails = new HashMap<>();

        for (String sessionId : sessionMapKeys) {
            Session session = sessions.get(sessionId);

            if (session != null) {
                sessionDetails.put(sessionId, Map.of(
                        "userName", session.userName(),
                        "personality", session.personality().name(),
                        "scenario", session.scenario().name(),
                        "status", session.status(),
                        "createdAt", session.createdAt(),
                        "messagesInSession", session.messages().size(),
                        "messagesInMemoryStore", memoryStore.getMessageCount(sessionId
                        )));
            } else {
                sessionDetails.put(sessionId, Map.of(
                        "userName", "Unknown User",
                        "personality", "Unknown",
                        "scenario", "Unknown",
                        "status", "ORPHANED",
                        "createdAt", null,
                        "messageCount", 0
                ));
            }
        }

        debugInfo.put("sessionDetails", sessionDetails);

        Log.infof("Debug info requested. Returning: %s", debugInfo);

        return debugInfo;
    }

    private void checkValidRequest(final CreateSessionRequest request) {
        if (request == null ||
                request.userId() == null ||
                request.userId().isBlank() ||
                request.personality() == null ||
                request.scenario() == null) {
            Log.error("Session creation failed: missing required parameters");
            throw new IllegalArgumentException("User ID, personality and scenario are required");
        }
    }


    private void checkValidMessage(final UserMessageRequest request) {
        if (request == null || request.content() == null || request.content().isBlank()) {
            Log.error("Message sending failed: content is required");
            throw new IllegalArgumentException("Message content is required");
        }
    }
}
