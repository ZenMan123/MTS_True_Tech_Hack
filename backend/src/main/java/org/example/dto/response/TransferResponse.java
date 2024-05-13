package org.example.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TransferResponse(
        @JsonProperty("номер_телефона")
        String phoneNumber,
        @JsonProperty("сумма")
        double value
) {
}
