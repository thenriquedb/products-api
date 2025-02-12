package com.thenriquedb.products_api.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItem> items;

    private BigDecimal total;

    @CreationTimestamp
    private Instant createdAt;

    @CreationTimestamp
    private Instant updatedAt;

    public void calculateTotal() {
        this.total = this.items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
