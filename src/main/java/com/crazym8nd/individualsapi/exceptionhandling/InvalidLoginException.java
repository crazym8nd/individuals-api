package com.crazym8nd.individualsapi.exceptionhandling;

public class InvalidLoginException extends RuntimeException{
    public InvalidLoginException(String message) {
        super(message);
    }
}
