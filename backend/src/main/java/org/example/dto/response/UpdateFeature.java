package org.example.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record UpdateFeature(
        @JsonProperty("status")
        String status,
        @JsonProperty("dialogue_state")
        Map<String, String> dialogueState,
        @JsonProperty("predicted_feature")
        String predictedFeature
) {
}
