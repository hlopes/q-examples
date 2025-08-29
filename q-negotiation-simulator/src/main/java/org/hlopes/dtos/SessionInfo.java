package org.hlopes.dtos;

import java.time.Instant;

public record SessionInfo(String id,
                          String userName,
                          String personality,
                          String scenario,
                          String status,
                          int messageCount,
                          Instant createdAt) {
}
