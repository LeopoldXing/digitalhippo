package com.leopoldhsing.digitalhippo.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String userId, String email) {
        super("We don't recognize this email and password! Please try again.");
    }
}
