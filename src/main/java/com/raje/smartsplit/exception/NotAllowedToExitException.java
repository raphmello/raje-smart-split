package com.raje.smartsplit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotAllowedToExitException extends RuntimeException {
    public NotAllowedToExitException() {
        super("User cannot exit a group that was created by himself");
    }
}
