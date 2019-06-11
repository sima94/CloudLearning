package com.cloudlearning.cloud.exeptions.entity;

import lombok.Getter;

@Getter
public class EntityException extends RuntimeException {

    private String message;

    public EntityException(String message){
        this.message = message;
    }

}
