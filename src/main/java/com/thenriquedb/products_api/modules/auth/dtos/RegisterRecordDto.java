package com.thenriquedb.products_api.modules.auth.dtos;

import com.thenriquedb.products_api.domain.UserRole;
import jakarta.validation.constraints.NotEmpty ;
import org.hibernate.validator.constraints.Length;

public record RegisterRecordDto(
        @NotEmpty String login,
        @NotEmpty @Length(min = 6) String password,
        @NotEmpty String name,
        UserRole role) {
}
