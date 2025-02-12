package com.thenriquedb.products_api.modules.order.dtos;

import com.thenriquedb.products_api.domain.Order;
import com.thenriquedb.products_api.domain.OrderItem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreateOrderResponse {
    private BigDecimal total;
    private List<OrderItemDto> items = new ArrayList<>();
    private Instant createdAt;
    private Instant updatedAt;
    public void addItem(OrderItem orderItem) {
        this.items
                .add(new OrderItemDto(
                        orderItem.getProduct().getId().toString(),
                        orderItem.getProduct().getValue(),
                        orderItem.getQuantity(),
                        orderItem.getSubtotal())
                );
    }

    public void fromOrder(Order order) {
        BeanUtils.copyProperties(order, this);
        order.getItems().forEach(this::addItem);
    }
}
