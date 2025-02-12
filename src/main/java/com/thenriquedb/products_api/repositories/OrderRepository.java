package com.thenriquedb.products_api.repositories;

import com.thenriquedb.products_api.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
   Order findAllByUserId(UUID userId);
}
