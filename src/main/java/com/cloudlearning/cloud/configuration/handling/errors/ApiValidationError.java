package com.cloudlearning.cloud.configuration.handling.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiValidationError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

}
