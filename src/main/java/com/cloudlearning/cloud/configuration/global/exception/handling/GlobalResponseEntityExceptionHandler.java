package com.cloudlearning.cloud.configuration.global.exception.handling;

import com.cloudlearning.cloud.configuration.global.exception.handling.errors.ApiError;
import com.cloudlearning.cloud.configuration.global.exception.handling.errors.ApiValidationError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    // error handle for @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        status = HttpStatus.UNPROCESSABLE_ENTITY;

        //Get all validation errors
        List validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> new ApiValidationError(x.getObjectName(), x.getField(), x.getRejectedValue(),x.getDefaultMessage()))
                .collect(Collectors.toList());

        ApiError error = new ApiError(status);
        error.setMessage("api.error.validation");

        error.setValidationErrors(validationErrors);

        return new ResponseEntity<>(error, headers, status);

    }

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            ResponseStatusException ex) {
        ApiError apiError = new ApiError(ex.getStatus());
        apiError.setMessage(ex.getReason());
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

}