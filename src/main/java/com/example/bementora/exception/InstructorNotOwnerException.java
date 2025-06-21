package com.example.bementora.exception;

public class InstructorNotOwnerException extends RuntimeException {
    public InstructorNotOwnerException(String message) {
        super(message);
    }
}
