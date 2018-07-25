package com.kotor4j.resourcemanager.exceptions;

/**
 * @author Dmitry
 */
public class ParsingException extends RuntimeException {

    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParsingException(Throwable cause) {
        super(cause);
    }

    public ParsingException(String message) {
        super(message);
    }

}
