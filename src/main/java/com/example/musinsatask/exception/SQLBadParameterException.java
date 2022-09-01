package com.example.musinsatask.exception;


import org.springframework.dao.DataIntegrityViolationException;

public class SQLBadParameterException extends DataIntegrityViolationException {
    public SQLBadParameterException(String msg) {
        super(msg);
    }

    public SQLBadParameterException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
