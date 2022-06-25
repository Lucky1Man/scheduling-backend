package com.online.scheduling.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ValidationExceptionUserAccountException extends RuntimeException{
    public ValidationExceptionUserAccountException(String message) {
        super(message);
    }
}
