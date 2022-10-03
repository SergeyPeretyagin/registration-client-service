package com.userservice.domain.exception;

/**
 * При удалении из базы не существующего пользователя
 */
public class NotExistsUserException extends RuntimeException{
    public NotExistsUserException(String message) {
        super(message);
    }
}
