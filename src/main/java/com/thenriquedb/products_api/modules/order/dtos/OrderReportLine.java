package com.thenriquedb.products_api.modules.order.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Data
@AllArgsConstructor
public class OrderReportLine {
    private String order_id;
    private String user_name;
    private BigDecimal total;
    private Date created_at;
}
