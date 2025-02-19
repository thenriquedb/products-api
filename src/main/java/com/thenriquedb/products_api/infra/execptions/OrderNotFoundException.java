package com.thenriquedb.products_api.infra.execptions;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class OrderNotFoundException extends BusinessException {
    UUID id;
    public OrderNotFoundException(UUID id) {
        super(String.format("Order with id %s not found", id), HttpStatus.NOT_FOUND);
        this.id = id;
    }
}
