package com.thenriquedb.products_api.infra;

import com.thenriquedb.products_api.execptions.ApiErrorMessage;
import com.thenriquedb.products_api.execptions.ProductNotFoundExecption;
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
    public ResponseEntity<ApiErrorMessage> handleProductNotFoundExecption(ProductNotFoundExecption execption) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(HttpStatus.NOT_FOUND, execption.getMessage());
        return ResponseEntity.status(apiErrorMessage.getStatus()).body(apiErrorMessage);
    }
}
