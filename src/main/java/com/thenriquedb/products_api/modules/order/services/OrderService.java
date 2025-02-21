package com.thenriquedb.products_api.modules.order.services;

import com.thenriquedb.products_api.domain.Order;
import com.thenriquedb.products_api.domain.OrderItem;
import com.thenriquedb.products_api.domain.Product;
import com.thenriquedb.products_api.domain.User;
import com.thenriquedb.products_api.infra.execptions.OrderNotFoundException;
import com.thenriquedb.products_api.infra.responses.PaginationResponse;
import com.thenriquedb.products_api.modules.order.dtos.CreateOrderProductDto;
import com.thenriquedb.products_api.infra.execptions.CreateOrderException;
import com.thenriquedb.products_api.infra.execptions.ProductNotFoundExecption;
import com.thenriquedb.products_api.repositories.OrderRepository;
import com.thenriquedb.products_api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Transactional
    public Order createOrder(User user, List<CreateOrderProductDto> productsDto) {
        try {
            List<OrderItem> orderItems = new ArrayList<OrderItem>();
            Set<UUID> productIds = new HashSet<>();

            Order order = new Order();

            for (CreateOrderProductDto productDto : productsDto) {
                Product product = productRepository.findById(productDto.productId()).orElse(null);

                if(!productIds.add(productDto.productId())) {
                    throw new CreateOrderException("Error creating order. Duplicated product.");
                }

                if (product == null) {
                    throw new ProductNotFoundExecption(productDto.productId());
                }

                OrderItem orderItem = new OrderItem(product, productDto.quantity(), order);

                orderItems.add(orderItem);
            }

            order.setUser(user);
            order.setItems(new HashSet<>(orderItems));
            order.calculateTotal();

            return orderRepository.save(order);
        } catch (ProductNotFoundExecption e) {
            throw new CreateOrderException("Error creating order. One of the products was not found.");
        }
    }

    public PaginationResponse<Order> listAllOrdersFromUser(User user, int pageNumber, int pageSize, String sortBy, boolean ascending) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<Order> orders = orderRepository.findAllByUserId(
                user.getId(),
                PageRequest.of(pageNumber, pageSize,sort)
        );

        return new PaginationResponse<>(
                orders.getContent(),
                orders.getNumber(),
                orders.getSize(),
                orders.getTotalElements(),
                orders.getTotalPages()
        );
    }

    public Order getById(UUID id) {
      Optional<Order> order = orderRepository.findById(id);

      if(order.isEmpty()) {
        throw new OrderNotFoundException(id);
      }

      return order.get();
    }
}
