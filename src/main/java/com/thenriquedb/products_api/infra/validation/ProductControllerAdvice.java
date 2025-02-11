package com.thenriquedb.products_api.infra.validation;

import com.thenriquedb.products_api.infra.execptions.ApiErrorMessage;
import com.thenriquedb.products_api.infra.execptions.ProductNotFoundExecption;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ProductControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ProductNotFoundExecption.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorMessage> handleProductNotFoundExecption(ProductNotFoundExecption exception) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(exception.getHttpStatus(), exception.getMessage());
        return ResponseEntity.status(apiErrorMessage.getStatus()).body(apiErrorMessage);
    }
}
