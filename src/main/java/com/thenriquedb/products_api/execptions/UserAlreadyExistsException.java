package com.thenriquedb.products_api.execptions;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException{
    private String login;

    public UserAlreadyExistsException(String login){
        super(String.format("User with login %s already exists", login));
        this.login = login;
    }
}
