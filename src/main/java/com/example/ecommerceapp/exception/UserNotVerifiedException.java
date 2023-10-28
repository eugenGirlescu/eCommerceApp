package com.example.ecommerceapp.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserNotVerifiedException extends RuntimeException {

    private final Boolean newEmailSent;

    public Boolean isNewEmailSent() {
        return newEmailSent;
    }
}
