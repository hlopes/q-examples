package org.hlopes.dtos;

import org.hlopes.model.PersonalityType;
import org.hlopes.model.Scenario;

public record CreateSessionRequest(String userId, PersonalityType personality, Scenario scenario) {
}
