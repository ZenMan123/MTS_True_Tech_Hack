package org.example.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record ContinueDialogue(
        @JsonProperty("status")
        String status,
        @JsonProperty("dialogue_state")
        Map<String, String> dialogueState
) {
}
