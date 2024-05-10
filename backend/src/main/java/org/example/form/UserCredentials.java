package org.example.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UserCredentials {

    @NotBlank
    @Pattern(regexp = "\\+7\\d{10}")
    @JsonProperty("phone_number")
    private String phoneNumber;

}