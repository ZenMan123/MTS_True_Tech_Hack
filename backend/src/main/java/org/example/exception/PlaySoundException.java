package org.example.exception;

public class PlaySoundException extends RuntimeException{
    public PlaySoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlaySoundException() {
    }

    public PlaySoundException(String message) {
        super(message);
    }
}
