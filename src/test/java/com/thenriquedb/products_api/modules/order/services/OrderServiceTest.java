package com.thenriquedb.products_api.modules.order.services;

import com.thenriquedb.products_api.domain.*;
import com.thenriquedb.products_api.infra.execptions.CreateOrderException;
import com.thenriquedb.products_api.infra.execptions.OrderNotFoundException;
import com.thenriquedb.products_api.modules.order.dtos.CreateOrderProductDto;
import com.thenriquedb.products_api.repositories.OrderRepository;
import com.thenriquedb.products_api.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    OrderService orderService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_success() {
        User user = new User("John Doe", "john.doe", "password", UserRole.ADMIN);

        Product product1 = new Product(UUID.randomUUID(), "Product 1", new BigDecimal(100), null, null);
        Product product2 = new Product(UUID.randomUUID(), "Product 2", new BigDecimal(200), null, null);

        List<CreateOrderProductDto> productsDto = List.of(
                new CreateOrderProductDto(product1.getId(), 2),
                new CreateOrderProductDto(product2.getId(), 3)
        );

        OrderItem orderItem1 = new OrderItem(product1, 2);
        OrderItem orderItem2 = new OrderItem(product2, 3);

        Order expectedOrder = new Order();
        expectedOrder.setUser(user);
        expectedOrder.setItems(Set.of(orderItem1, orderItem2));
        expectedOrder.calculateTotal();

        when(productRepository.findById(product1.getId())).thenReturn(Optional.of(product1));
        when(productRepository.findById(product2.getId())).thenReturn(Optional.of(product2));
        when(orderRepository.save(any())).thenReturn(expectedOrder);

        Order createdOrder = orderService.createOrder(user, productsDto);

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getUser()).isEqualTo(user);
        assertThat(createdOrder.getItems()).hasSize(productsDto.size());
        assertThat(createdOrder.getTotal()).isEqualTo(expectedOrder.getTotal());

        verify(productRepository, times(productsDto.size())).findById(any());
    }

    @Test
    void createOrder_duplicatedProduct() {
        Product product = new Product(UUID.randomUUID(), "Product", new BigDecimal(100), null, null);
        product.setId(UUID.randomUUID());

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        assertThrows(CreateOrderException.class, () -> {
            orderService.createOrder(
                    new User(),
                    List.of(
                            new CreateOrderProductDto(product.getId(), 1),
                            new CreateOrderProductDto(product.getId(), 2)
                    )
            );
        });
    }

    @Test
    void createOrder_productNotFound() {
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(CreateOrderException.class, () -> {
            orderService.createOrder(
                    new User(),
                    List.of(new CreateOrderProductDto(UUID.randomUUID(), 1))
            );
        });
    }

    @Test
    void listAllOrdersFromUser() {
        User user = new User();
        user.setId(UUID.randomUUID());

        Order order1 = new Order();
        Order order2 = new Order();

        when(orderRepository.findAllByUserId(user.getId())).thenReturn(List.of(order1, order2));

        List<Order> orders = orderService.listAllOrdersFromUser(user);

        assertThat(orders).isNotNull();
        assertThat(orders).hasSize(2);
    }

    @Test
    void getById_success() {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setTotal(new BigDecimal(100));
        order.setItems(Set.of(new OrderItem()));

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        Order foundOrder = orderService.getById(order.getId());

        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getId()).isEqualTo(order.getId());
    }

    @Test
    void getById_throwsOrderNotFoundException() {
        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getById(UUID.randomUUID());
        });
    }
}