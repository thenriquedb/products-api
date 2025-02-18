package com.thenriquedb.products_api.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void calculateTotal() {
        Product product1 = new Product(UUID.randomUUID(), "Product 1", new BigDecimal(100), null, null);
        Product product2 = new Product(UUID.randomUUID(), "Product 1", new BigDecimal(500), null, null);

        OrderItem orderItem1 = new OrderItem(product1,2);
        OrderItem orderItem2 = new OrderItem(product2, 3);

        Order order = new Order();
        order.setItems(Set.of(orderItem1, orderItem2));
        order.calculateTotal();

        assertEquals(
                order.getTotal(),
                orderItem1.getSubtotal().add(orderItem2.getSubtotal())
        );
    }
}