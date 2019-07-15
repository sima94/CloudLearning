package com.cloudlearning.cloud.global.exception.entity;

import lombok.Getter;

@Getter
public class EntityException extends RuntimeException {

    private String message;

    public EntityException(String message){
        this.message = message;
    }

}
