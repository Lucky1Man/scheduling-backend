package com.online.scheduling.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicatesFoundException extends RuntimeException{
    public DuplicatesFoundException(String message) {
        super(message);
    }
}
