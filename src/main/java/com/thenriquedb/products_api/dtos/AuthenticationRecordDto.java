package com.thenriquedb.products_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record AuthenticationRecordDto(String login, String password) {
}
