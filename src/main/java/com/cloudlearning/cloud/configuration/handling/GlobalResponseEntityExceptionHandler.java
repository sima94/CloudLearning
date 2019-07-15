package com.cloudlearning.cloud.configuration.handling;

import com.cloudlearning.cloud.configuration.handling.errors.ApiValidationError;
import com.cloudlearning.cloud.global.exception.entity.EntityAlreadyExistExeption;
import com.cloudlearning.cloud.global.exception.entity.EntityException;
import com.cloudlearning.cloud.global.exception.entity.EntityNotExistException;
import com.cloudlearning.cloud.configuration.handling.errors.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@ControllerAdvice
public class GlobalResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    // error handle for @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        status = HttpStatus.UNPROCESSABLE_ENTITY;

        ApiError error = new ApiError(status);
        error.setMessage("api.error.validation");

        //Get all validation errors
        List<ApiValidationError> validationErrors = new ArrayList<>();
        for (FieldError x : ex.getBindingResult()
                .getFieldErrors()) {
            ApiValidationError apiValidationError = new ApiValidationError(x.getObjectName(), x.getField(), x.getRejectedValue(), x.getDefaultMessage());
            validationErrors.add(apiValidationError);
        }
        error.setValidationErrors(validationErrors);

        return new ResponseEntity<>(error, headers, status);
    }

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<Object> handleResponseStatusExceptions(
            ResponseStatusException ex) {

        ApiError apiError;
        apiError = new ApiError(ex.getStatus());
        apiError.setMessage(ex.getReason());
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(EntityException.class)
    protected ResponseEntity<Object> handleEntityExceptions(
            EntityException ex) {

        ApiError apiError = null;
        if (ex instanceof EntityAlreadyExistExeption) {
            apiError = new ApiError(HttpStatus.EXPECTATION_FAILED);
        } else if (ex instanceof EntityNotExistException) {
            apiError = new ApiError(HttpStatus.NOT_FOUND);
        }

        apiError.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

}