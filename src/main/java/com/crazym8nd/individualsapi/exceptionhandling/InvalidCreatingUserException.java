package com.crazym8nd.individualsapi.exceptionhandling;

public class InvalidCreatingUserException extends RuntimeException{
    public InvalidCreatingUserException(String message) {
        super(message);
    }
}
