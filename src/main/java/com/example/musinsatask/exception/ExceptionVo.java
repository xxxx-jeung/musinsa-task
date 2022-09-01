package com.example.musinsatask.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionVo {
    private HttpStatus status;
    private String message;

    public ExceptionVo() {
    }

    @Builder
    public ExceptionVo(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
