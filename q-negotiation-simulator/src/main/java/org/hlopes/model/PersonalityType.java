package org.hlopes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PersonalityType {
    SUPPORTIVE("Supportive", "S", "You are a supportive manager. You are encouraging, ask how you can help, and focus" +
            " on collaborative solutions."),
    ANALYTICAL("Analytical", "C", "You are an analytical manager. You are data-driven, ask for specific numbers and " +
            "evidence, and want logical arguments."),
    DIRECT("Direct", "D", "You are a direct manager. You are blunt, time-conscious, and cut to the chase. Keep your " +
            "responses short and to the point."),
    ACCOMMODATING("Accommodating", "S", "You are an accommodating manager. You are agreeable and polite but cautious " +
            "about making commitments. You often say you need to 'think about it' or 'check with others'.");

    private final String personalityName;
    private final String discType;
    private final String systemPrompt;

    public String getFullDiscType() {
        return switch (discType) {
            case "D" -> "Dominance (D)";
            case "I" -> "Influence (I)";
            case "S" -> "Steadiness (S)";
            case "C" -> "Conscientiousness (C)";
            default -> discType;
        };
    }

}
