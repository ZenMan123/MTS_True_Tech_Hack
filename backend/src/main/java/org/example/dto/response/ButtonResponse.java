package org.example.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ButtonResponse(
        @JsonProperty("recommended_button_id")
        String id
) {
}
