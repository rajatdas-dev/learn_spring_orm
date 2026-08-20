package com.example.springmvc.exception;

import org.springframework.http.HttpStatus;

public class OrderStateException extends BusinessException{

    public OrderStateException(String message) {
        super(ErrorCode.ORDER_STATE_ERROR, HttpStatus.CONFLICT, message);
    }
}
