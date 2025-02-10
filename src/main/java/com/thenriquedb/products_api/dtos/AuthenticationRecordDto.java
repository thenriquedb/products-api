package com.thenriquedb.products_api.dtos;

import jakarta.validation.constraints.NotEmpty;

public record AuthenticationRecordDto(@NotEmpty String login, @NotEmpty String password) {
}
