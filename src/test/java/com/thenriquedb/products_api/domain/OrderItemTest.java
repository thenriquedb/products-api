package com.thenriquedb.products_api.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void orderItem_calculateSubtotal() {
        OrderItem orderItem = new OrderItem();
        Product product = new Product();
        product.setValue(BigDecimal.valueOf(10));
        orderItem.setProduct(product);
        orderItem.setQuantity(5);

        orderItem.calculateSubtotal();

        assertEquals(BigDecimal.valueOf(50), orderItem.getSubtotal());
    }
}