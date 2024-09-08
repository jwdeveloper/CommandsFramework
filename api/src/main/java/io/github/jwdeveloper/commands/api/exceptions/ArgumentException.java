package io.github.jwdeveloper.commands.api.exceptions;


/**
 * Exception triggered when argument value is invalid
 */
public class ArgumentException extends RuntimeException {

    /**
     * @param message exception message
     */
    public ArgumentException(String message) {
        super(message);
    }

    /**
     * @param message exception message
     * @param cause   the cause
     */
    public ArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause the cause
     */
    public ArgumentException(Throwable cause) {
        super(cause);
    }

}
