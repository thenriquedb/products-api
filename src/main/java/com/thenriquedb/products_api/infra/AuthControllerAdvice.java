package com.thenriquedb.products_api.infra;


import com.thenriquedb.products_api.infra.execptions.ApiErrorMessage;
import com.thenriquedb.products_api.infra.execptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AuthControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiErrorMessage>  handleUserAlreadyExistsExeception(UserAlreadyExistsException exception) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(HttpStatus.CONFLICT, exception.getMessage());
        return ResponseEntity.status(apiErrorMessage.getStatus()).body(apiErrorMessage);
    }
}
