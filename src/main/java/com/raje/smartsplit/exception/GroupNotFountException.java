package com.raje.smartsplit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GroupNotFountException extends RuntimeException {
    public GroupNotFountException(String message) {
        super(message);
    }
}
