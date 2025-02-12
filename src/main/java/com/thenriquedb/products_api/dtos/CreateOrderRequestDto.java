package com.thenriquedb.products_api.dtos;

import java.util.List;

public record CreateOrderRequestDto(List<CreateOrderProductDto> products) {
}
