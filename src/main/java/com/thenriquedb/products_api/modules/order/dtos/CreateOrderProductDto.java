package com.thenriquedb.products_api.modules.order.dtos;

import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public record CreateOrderProductDto(@NotEmpty UUID productId, @NotEmpty int quantity) {
}
