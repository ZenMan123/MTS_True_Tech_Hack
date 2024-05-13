package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record TransferMoneyRequest(
        @JsonProperty("from_id")
        @Positive
        long fromId,
        @JsonProperty("to_phone_number")
        @NotBlank
        @Pattern(regexp = "\\+7\\d{10}")
        String toPhoneNumber,
        @JsonProperty("value")
        double value
) {
}
