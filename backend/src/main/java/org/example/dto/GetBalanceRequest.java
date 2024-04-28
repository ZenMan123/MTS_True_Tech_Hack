package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;

public record GetBalanceRequest(
        @JsonProperty("user_id")
        @Positive
        long id) {
}
