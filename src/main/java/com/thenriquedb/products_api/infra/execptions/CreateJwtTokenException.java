package com.thenriquedb.products_api.infra.execptions;

import org.springframework.http.HttpStatus;

public class CreateJwtTokenException extends BaseException {
    public CreateJwtTokenException() {
        super("Error creating JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
