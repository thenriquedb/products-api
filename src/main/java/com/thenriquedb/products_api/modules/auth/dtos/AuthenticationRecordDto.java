package com.thenriquedb.products_api.modules.auth.dtos;

import jakarta.validation.constraints.NotEmpty;

public record AuthenticationRecordDto(@NotEmpty String login, @NotEmpty String password) {
}
