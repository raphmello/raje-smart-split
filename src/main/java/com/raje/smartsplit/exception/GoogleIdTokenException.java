package com.raje.smartsplit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GoogleIdTokenException extends RuntimeException {
    public GoogleIdTokenException() {
        super("Not possible to validate Google token. Maybe it is expired.");
    }
}
