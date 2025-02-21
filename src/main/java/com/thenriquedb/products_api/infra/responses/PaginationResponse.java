package com.thenriquedb.products_api.infra.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PaginationResponse<T> {
    private List<T> data = List.of();
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
