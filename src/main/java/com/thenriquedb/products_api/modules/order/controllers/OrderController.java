package com.thenriquedb.products_api.modules.order.controllers;

import com.thenriquedb.products_api.domain.Order;
import com.thenriquedb.products_api.domain.OrderItem;
import com.thenriquedb.products_api.domain.User;
import com.thenriquedb.products_api.modules.order.dtos.CreateOrderRequestDto;
import com.thenriquedb.products_api.modules.order.dtos.CreateOrderResponse;
import com.thenriquedb.products_api.modules.order.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequestDto createOrderRequestDto, @AuthenticationPrincipal User user) {
        Order createdOrder = orderService.createOrder(user, createOrderRequestDto.products());
        CreateOrderResponse response = new CreateOrderResponse();

        response.setTotal(createdOrder.getTotal());

        for (OrderItem orderItem : createdOrder.getItems()) {
            response.addOrderItem(orderItem);
        }

        return ResponseEntity.ok(response);
    }
}
