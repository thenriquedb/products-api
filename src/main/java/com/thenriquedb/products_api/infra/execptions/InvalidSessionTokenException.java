package com.thenriquedb.products_api.infra.execptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidSessionTokenException extends BaseException {
    public InvalidSessionTokenException() {
        super("Invalid session token", HttpStatus.UNAUTHORIZED);
    }
}
