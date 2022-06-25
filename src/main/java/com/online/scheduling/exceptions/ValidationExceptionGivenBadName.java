package com.online.scheduling.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ValidationExceptionGivenBadName extends RuntimeException{
    public ValidationExceptionGivenBadName(String message) {
        super(message);
    }
}
