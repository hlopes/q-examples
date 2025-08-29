package org.hlopes.model;

import java.time.Instant;

public record Message(String sender, String content, Instant instant) {
    public Message(String sender, String content) {
        this(sender, content, Instant.now());
    }
}
