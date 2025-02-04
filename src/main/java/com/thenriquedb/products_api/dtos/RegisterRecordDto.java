package com.thenriquedb.products_api.dtos;

import com.thenriquedb.products_api.models.UserRole;
import jakarta.validation.constraints.NotEmpty ;
import org.hibernate.validator.constraints.Length;

public record RegisterRecordDto(
        @NotEmpty String login,
        @NotEmpty @Length(min = 6) String password,
        @NotEmpty String name,
        UserRole role) {
}
