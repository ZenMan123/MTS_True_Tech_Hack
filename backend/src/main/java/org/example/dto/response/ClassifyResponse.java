package org.example.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClassifyResponse(
        @JsonProperty("status")
        String status,
        @JsonProperty("category")
        String category
) {
}
