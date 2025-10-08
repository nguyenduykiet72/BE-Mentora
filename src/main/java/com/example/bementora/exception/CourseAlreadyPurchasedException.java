package com.example.bementora.exception;

public class CourseAlreadyPurchasedException extends RuntimeException {
    public CourseAlreadyPurchasedException(String message) {
        super(message);
    }
}
