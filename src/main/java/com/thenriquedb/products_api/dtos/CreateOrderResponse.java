package com.thenriquedb.products_api.dtos;

import com.thenriquedb.products_api.domain.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreateOrderResponse {
    private BigDecimal total;
    private List<OrderItemDto> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems
                .add(new OrderItemDto(
                        orderItem.getProduct().getId().toString(),
                        orderItem.getProduct().getValue(),
                        orderItem.getQuantity(),
                        orderItem.getSubtotal())
                );
    }
}
