package com.online.scheduling.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ValidationExceptionGivenBadTime extends RuntimeException{
    public ValidationExceptionGivenBadTime(String message) {
        super(message);
    }
}
