package com.thenriquedb.products_api.infra.execptions;

import lombok.Getter;
import java.util.UUID;

@Getter
public class ProductNotFoundExecption extends RuntimeException {
    private UUID id;
    public ProductNotFoundExecption(UUID id) {
        super(String.format("Product with id %s not found", id));
        this.id = id;
    }
}
