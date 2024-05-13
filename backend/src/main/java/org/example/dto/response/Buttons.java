package org.example.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Buttons(
        @JsonProperty("button_text")
        String text,
        @JsonProperty("button_id")
        String id
) {
}
