package com.thenriquedb.products_api.modules.order.services;

import com.thenriquedb.products_api.domain.Order;
import com.thenriquedb.products_api.domain.OrderItem;
import com.thenriquedb.products_api.domain.Product;
import com.thenriquedb.products_api.domain.User;
import com.thenriquedb.products_api.modules.order.dtos.CreateOrderProductDto;
import com.thenriquedb.products_api.infra.execptions.CreateOrderException;
import com.thenriquedb.products_api.infra.execptions.ProductNotFoundExecption;
import com.thenriquedb.products_api.repositories.OrderRepository;
import com.thenriquedb.products_api.repositories.ProductRepository;
import com.thenriquedb.products_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public Order createOrder(User user, List<CreateOrderProductDto> productsDto) {
        try {
            List<OrderItem> orderItems = new ArrayList<OrderItem>();
            Order order = new Order();

            BigDecimal total = new BigDecimal(0);

            for (CreateOrderProductDto productDto : productsDto) {
                Product product = productRepository.findById(productDto.productId()).orElse(null);

                if (product == null) {
                    throw new ProductNotFoundExecption(productDto.productId());
                }

                total = total.add(product.getValue().multiply(BigDecimal.valueOf(productDto.quantity())));

                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(productDto.quantity());
                orderItem.setOrder(order);

                orderItem.calculateSubtotal();
                orderItems.add(orderItem);
            }

            order.setUser(user);
            order.setTotal(total);
            order.setItems(new HashSet<>(orderItems));

            return orderRepository.save(order);
        } catch (ProductNotFoundExecption e) {
            throw new CreateOrderException("Error creating order");
        }
    }
}
