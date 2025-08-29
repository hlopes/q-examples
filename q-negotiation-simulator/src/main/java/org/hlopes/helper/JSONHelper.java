package org.hlopes.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JSONHelper {
    public static String extractJsonFromResponse(final String response) {
        if (response == null) {
            return "{}";
        }

        // Find the first opening brace
        int startIndex = response.indexOf("{");

        if (startIndex == -1) {
            throw new IllegalArgumentException("No JSON object found in the response");
        }

        // Find the matching closing brace
        int braceCount = 0;
        int endIndex = -1;

        for (int i = startIndex; i < response.length(); i++) {
            char c = response.charAt(i);

            if (c == '{') {
                braceCount++;
            } else if (c == '}') {
                braceCount--;

                if (braceCount == 0) {
                    endIndex = i;

                    break;
                }
            }
        }

        if (endIndex == -1) {
            throw new IllegalArgumentException("No complete JSON object found in response");
        }

        return response.substring(startIndex, endIndex + 1);
    }
}
