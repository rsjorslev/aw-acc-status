package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Host could not be found")
public class HostNotFoundException extends RuntimeException {

    public HostNotFoundException() {
        super();
    }

    public HostNotFoundException(String message) {
        super(message);
    }

    public HostNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
