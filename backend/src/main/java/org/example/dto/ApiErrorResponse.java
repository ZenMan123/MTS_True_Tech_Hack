package org.example.dto;

import jakarta.validation.constraints.NotNull;

public record ApiErrorResponse(
        @NotNull
        String description,
        @NotNull
        String code,
        @NotNull
        String exceptionName,
        @NotNull
        String exceptionMessage) {
    public static ApiErrorResponse buildResponse(String description, String code, Throwable e) {
        return new ApiErrorResponse(
                description,
                code,
                e.getClass().getName(),
                e.getMessage()
        );
    }
}
