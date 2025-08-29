package org.hlopes.dtos;

import java.util.List;

public record Feedback(Integer overallScore, List<String> strengths, List<String> improvements) {
    public Feedback(List<String> strengths, List<String> improvements) {
        this(null, strengths, improvements);
    }
}
