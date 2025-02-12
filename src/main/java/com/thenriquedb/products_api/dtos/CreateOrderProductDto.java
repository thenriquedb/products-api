package com.thenriquedb.products_api.dtos;

import java.util.UUID;

public record CreateOrderProductDto(UUID productId, int quantity) {
}
