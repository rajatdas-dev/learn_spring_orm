package com.example.springmvc.exception;

import org.springframework.http.HttpStatus;

public class ProductOutOfStockException extends BusinessException{

    public ProductOutOfStockException(String message) {
        super(ErrorCode.PRODUCT_OUT_OF_STOCK, HttpStatus.CONFLICT, message);
    }
}
