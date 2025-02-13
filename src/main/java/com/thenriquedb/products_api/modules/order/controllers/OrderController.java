package com.thenriquedb.products_api.modules.order.controllers;

import com.thenriquedb.products_api.configurations.SecurityConfiguration;
import com.thenriquedb.products_api.domain.Order;
import com.thenriquedb.products_api.domain.User;
import com.thenriquedb.products_api.modules.order.dtos.CreateOrderRequestDto;
import com.thenriquedb.products_api.modules.order.dtos.CreateOrderResponse;
import com.thenriquedb.products_api.modules.order.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@Tag(name ="Order", description = "Order operations")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order")
    @ApiResponse(responseCode = "201", description = "Order created")
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody @Valid CreateOrderRequestDto createOrderRequestDto, @AuthenticationPrincipal User user) {
        Order createdOrder = orderService.createOrder(user, createOrderRequestDto.products());
        CreateOrderResponse response = new CreateOrderResponse();

        response.fromOrder(createdOrder);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List all orders")
    public ResponseEntity<List<CreateOrderResponse>> listOrders(@AuthenticationPrincipal User user) {
        List<Order> orders = orderService.listAllOrdersFromUser(user);
        List<CreateOrderResponse> response = new ArrayList<>();


        for (Order order : orders) {
            CreateOrderResponse createOrderResponse = new CreateOrderResponse();
            createOrderResponse.fromOrder(order);
            response.add(createOrderResponse);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by id")
    @ApiResponse(responseCode = "200", description = "Order found")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<Order> getOrder(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(orderService.getById(id));
    }
}
