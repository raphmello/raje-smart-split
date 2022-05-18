package com.raje.smartsplit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyParticipantException extends RuntimeException {
    public UserAlreadyParticipantException() {
        super("User is already in this group");
    }
}
