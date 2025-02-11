package com.thenriquedb.products_api.infra.execptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserAlreadyExistsException extends BaseException{
    private final String login;

    public UserAlreadyExistsException(String login){
        super(String.format("User with login %s already exists", login), HttpStatus.CONFLICT);
        this.login = login;
    }
}
