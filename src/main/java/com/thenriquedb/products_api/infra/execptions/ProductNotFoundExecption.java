package com.thenriquedb.products_api.infra.execptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Getter
public class ProductNotFoundExecption extends BusinessException {
    private final UUID id;
    public ProductNotFoundExecption(UUID id) {
        super(String.format("Product with id %s not found", id), HttpStatus.NOT_FOUND);
        this.id = id;
    }
}
