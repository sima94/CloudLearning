package com.cloudlearning.cloud.services.user.exceptions;

import com.cloudlearning.cloud.exeptions.entity.EntityNotExistException;

public class AuthorityEntityNotExistException extends EntityNotExistException {

    public AuthorityEntityNotExistException(String message) {
        super(message);
    }
}
