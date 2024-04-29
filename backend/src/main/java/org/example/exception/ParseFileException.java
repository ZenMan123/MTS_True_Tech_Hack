package org.example.exception;

public class ParseFileException extends RuntimeException{
    public ParseFileException() {
    }

    public ParseFileException(String message) {
        super(message);
    }

    public ParseFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
