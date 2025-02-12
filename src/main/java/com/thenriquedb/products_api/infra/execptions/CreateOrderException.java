package com.thenriquedb.products_api.infra.execptions;

import org.springframework.http.HttpStatus;

public class CreateOrderException extends BaseException {
    public CreateOrderException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
