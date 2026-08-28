package com.example.springmvc.exception;

import org.springframework.http.HttpStatus;

public class InventoryAlreadyExistsException extends BusinessException{

    public InventoryAlreadyExistsException( String message) {
        super(ErrorCode.INVENTORY_ALREADY_EXISTS, HttpStatus.INSUFFICIENT_STORAGE, message);
    }
}
