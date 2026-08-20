package com.example.springmvc.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends BusinessException{

    public InvalidRequestException(ErrorCode errorCode, String message) {
        super(errorCode,HttpStatus.BAD_REQUEST, message);
    }
}
