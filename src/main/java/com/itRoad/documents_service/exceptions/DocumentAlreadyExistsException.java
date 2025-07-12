package com.itRoad.documents_service.exceptions;

public class DocumentAlreadyExistsException extends RuntimeException {

    /**
     * Constructor with custom message
     * @param message The error message
     */
    public DocumentAlreadyExistsException(String message) {
        super(message);
    }
}
