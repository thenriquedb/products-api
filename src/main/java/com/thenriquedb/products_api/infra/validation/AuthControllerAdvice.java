package com.thenriquedb.products_api.infra.validation;


import com.thenriquedb.products_api.infra.execptions.ApiErrorMessage;
import com.thenriquedb.products_api.infra.execptions.CreateJwtTokenException;
import com.thenriquedb.products_api.infra.execptions.InvalidSessionTokenException;
import com.thenriquedb.products_api.infra.execptions.UserAlreadyExistsException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class AuthControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiErrorMessage> handleUserAlreadyExistsExeception(UserAlreadyExistsException exception) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(exception.getHttpStatus(), exception.getMessage());
        return ResponseEntity.status(apiErrorMessage.getStatus()).body(apiErrorMessage);
    }

    @ExceptionHandler(InvalidSessionTokenException.class)
    public ResponseEntity<ApiErrorMessage> handleTokenExpiredException(InvalidSessionTokenException exception) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(exception.getHttpStatus(), exception.getMessage());
        return ResponseEntity.status(apiErrorMessage.getStatus()).body(apiErrorMessage);
    }

    @ExceptionHandler(CreateJwtTokenException.class)
    public ResponseEntity<ApiErrorMessage> handleCreateJwtTokenException(CreateJwtTokenException exception) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(exception.getHttpStatus(), exception.getMessage());
        return ResponseEntity.status(apiErrorMessage.getStatus()).body(apiErrorMessage);
    }
}
