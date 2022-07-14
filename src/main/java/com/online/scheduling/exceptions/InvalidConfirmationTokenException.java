package com.online.scheduling.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidConfirmationTokenException extends RuntimeException{
    public InvalidConfirmationTokenException(String message) {
        super(message);
    }
}
