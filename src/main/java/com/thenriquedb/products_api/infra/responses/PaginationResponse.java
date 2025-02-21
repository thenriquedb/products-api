package com.thenriquedb.products_api.infra.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PaginationResponse<T> {
    private List<T> data;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;

   public PaginationResponse() {
        this.data = new ArrayList<>();
        this.pageNumber = 0;
        this.pageSize = 0;
        this.totalElements = 0;
        this.totalPages = 0;
    }

    public void add(T t) {
        data.add(t);
    }
}
