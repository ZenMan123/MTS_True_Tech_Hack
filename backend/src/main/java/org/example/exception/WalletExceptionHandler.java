package org.example.exception;

import jakarta.validation.ConstraintViolationException;
import org.example.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WalletExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> invalidArgumentHandle(ConstraintViolationException ex) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(badRequest)
                .body(
                        ApiErrorResponse.buildResponse(
                                "Invalid arguments",
                                badRequest.toString(),
                                ex
                        )
                );
    }

    @ExceptionHandler(PlaySoundException.class)
    public ResponseEntity<ApiErrorResponse> playSoundHandler(PlaySoundException ex) {
        HttpStatus iAmATeapot = HttpStatus.I_AM_A_TEAPOT;
        return ResponseEntity
                .status(iAmATeapot)
                .body(
                        ApiErrorResponse.buildResponse(
                                "Cannot play sound",
                                iAmATeapot.toString(),
                                ex.getCause()
                        )
                );
    }

    @ExceptionHandler(ParseFileException.class)
    public ResponseEntity<ApiErrorResponse> parseAudioHandler(ParseFileException ex) {
        HttpStatus iAmATeapot = HttpStatus.I_AM_A_TEAPOT;
        return ResponseEntity
                .status(iAmATeapot)
                .body(
                        ApiErrorResponse.buildResponse(
                                "Cannot parse file",
                                iAmATeapot.toString(),
                                ex.getCause()
                        )
                );
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiErrorResponse> invalidRequestHandler(InvalidRequestException ex) {
        HttpStatus iAmATeapot = HttpStatus.I_AM_A_TEAPOT;
        return ResponseEntity
                .status(iAmATeapot)
                .body(
                        ApiErrorResponse.buildResponse(
                                "Invalid request",
                                iAmATeapot.toString(),
                                ex.getCause()
                        )
                );
    }
}
