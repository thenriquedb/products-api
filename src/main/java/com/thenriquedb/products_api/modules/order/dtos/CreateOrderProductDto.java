package com.thenriquedb.products_api.modules.order.dtos;

import java.util.UUID;

public record CreateOrderProductDto(UUID productId, int quantity) {
}
