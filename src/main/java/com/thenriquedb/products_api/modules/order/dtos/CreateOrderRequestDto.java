package com.thenriquedb.products_api.modules.order.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequestDto(@NotNull List<CreateOrderProductDto> products) {
}
