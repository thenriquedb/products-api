package com.thenriquedb.products_api.modules.order.controllers;

import com.thenriquedb.products_api.configurations.SecurityConfiguration;
import com.thenriquedb.products_api.domain.Order;
import com.thenriquedb.products_api.domain.User;
import com.thenriquedb.products_api.infra.responses.PaginationResponse;
import com.thenriquedb.products_api.modules.order.dtos.CreateOrderRequestDto;
import com.thenriquedb.products_api.modules.order.dtos.CreateOrderResponse;
import com.thenriquedb.products_api.modules.order.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public ResponseEntity<PaginationResponse<CreateOrderResponse>> listOrders(@AuthenticationPrincipal User user,
                                                                @RequestParam(value = "page", defaultValue = "0") int pageNumber,
                                                                @RequestParam(value = "size", defaultValue = "10") int pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
                                                                @RequestParam(value = "ascending", defaultValue = "true") boolean ascending) {
        PaginationResponse<Order> orders = orderService.listAllOrdersFromUser(user, pageNumber, pageSize, sortBy, ascending);

        PaginationResponse<CreateOrderResponse> responseMapped = new PaginationResponse<>(
                new ArrayList<>(),
                orders.getPageNumber(),
                orders.getPageSize(),
                orders.getTotalElements(),
                orders.getTotalPages()
        );

        orders.getData().forEach(order -> {
            CreateOrderResponse createOrderResponse = new CreateOrderResponse(order);
            responseMapped.add(createOrderResponse);
        });

        return ResponseEntity.ok(responseMapped);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by id")
    @ApiResponse(responseCode = "200", description = "Order found")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<Order> getOrder(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @GetMapping("/report")
    @Operation(summary = "Generate a report of all products")
    @ApiResponse(responseCode = "200", description = "Report generated")
    public ResponseEntity<Object> generateReport() throws IOException {
        String filePath = this.orderService.generateReport();

        File file = new File(filePath);
        Path path = Paths.get(file.getAbsolutePath());
        var resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(file.length())
                .body(resource);
    }
}
