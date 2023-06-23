package dev.kurumiDisciples.javadex.api.exceptions;

public class RateLimitException extends Exception {
    public RateLimitException() {
        super("Rate limit exceeded. SLOW DOWN!");
    }

    public RateLimitException(String message) {
        super(message + " SLOW DOWN!");
    }
}
