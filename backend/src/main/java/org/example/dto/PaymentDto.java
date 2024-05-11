package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;

public record PaymentDto(
        @JsonProperty("user_id")
        @Positive
        long user_id,
        @JsonProperty("amount")
        double amount,
        @JsonProperty("type")
        String type,
        String date
) {
}
