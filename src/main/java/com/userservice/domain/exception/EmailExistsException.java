package com.userservice.domain.exception;

public class EmailExistsException extends RuntimeException{
    public EmailExistsException(String message) {
        super(message);
    }
}
