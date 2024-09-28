package com.leopoldhsing.digitalhippo.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class VerificationTokenExpiredException extends RuntimeException {
    public VerificationTokenExpiredException(String token) {
        super("Verification link expired");
    }
}
