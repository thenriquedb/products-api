package com.thenriquedb.products_api.modules.order.dtos;

import java.math.BigDecimal;

public record OrderItemDto(String productId, BigDecimal price, int quantity, BigDecimal subtotal) {
}
