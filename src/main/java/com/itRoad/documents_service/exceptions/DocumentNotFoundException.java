package com.itRoad.documents_service.exceptions;

public class DocumentNotFoundException extends RuntimeException {

    /**
     * Constructor with custom message
     * @param message The error message
     */
    public DocumentNotFoundException(String message) {
        super(message);
    }
}
