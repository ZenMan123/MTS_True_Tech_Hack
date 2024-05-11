package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.example.model.Payment;

import java.util.List;

public record UserDto(
        @JsonProperty("userId")
        long userId,
        @JsonProperty("name")
        @NotBlank
        String name,
        @JsonProperty("surname")
        @NotBlank
        String surname,
        @JsonProperty("phone_number")
        @NotBlank
        @Pattern(regexp = "\\+7\\d{10}")
        String phoneNumber,
        @JsonProperty("balance")
        @NotBlank
        double balance,
        List<PaymentDto> payments
) {
}
