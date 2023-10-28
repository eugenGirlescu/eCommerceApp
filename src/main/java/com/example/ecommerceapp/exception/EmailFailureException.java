package com.example.ecommerceapp.exception;

public class EmailFailureException extends RuntimeException {
    private String message;

    public EmailFailureException(String message) {
        this.message = message;
    }
}
