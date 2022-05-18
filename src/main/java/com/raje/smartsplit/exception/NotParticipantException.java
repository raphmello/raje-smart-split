package com.raje.smartsplit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotParticipantException extends RuntimeException {
    public NotParticipantException() {
        super("User is not a participant in this group");
    }
}
