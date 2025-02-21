package com.thenriquedb.products_api.repositories;

import com.thenriquedb.products_api.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
   List<Order> findAllByUserId(UUID userId);
   Page<Order> findAllByUserId(UUID userId, Pageable pageable);
}
