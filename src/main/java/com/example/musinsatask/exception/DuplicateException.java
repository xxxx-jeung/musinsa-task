package com.example.musinsatask.exception;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateException extends DuplicateKeyException {
    public DuplicateException(String reason) {
        super(reason);
    }
}
