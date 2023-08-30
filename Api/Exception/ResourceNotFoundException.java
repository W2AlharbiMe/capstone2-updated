package com.example.capstone2updated.Api.Exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message + " not found.");
    }
}
