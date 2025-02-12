package com.thenriquedb.products_api.modules.order.dtos;

import java.util.List;

public record CreateOrderRequestDto(List<CreateOrderProductDto> products) {
}
