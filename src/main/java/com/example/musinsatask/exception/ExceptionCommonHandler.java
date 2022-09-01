package com.example.musinsatask.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionCommonHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = {DuplicateException.class})
  public ExceptionVo duplicate(RuntimeException e) {
    e.printStackTrace();
    return ExceptionVo.builder()
        .status(HttpStatus.BAD_REQUEST)
        .message(e.getLocalizedMessage())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = {SQLBadParameterException.class})
  public ExceptionVo sqlBadParameter(RuntimeException e) {
    e.printStackTrace();
    return ExceptionVo.builder()
        .status(HttpStatus.BAD_REQUEST)
        .message(e.getLocalizedMessage())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = {BadRequestException.class})
  public ExceptionVo badRequest(RuntimeException e) {
    log.error(e.toString());
    return ExceptionVo.builder()
        .status(HttpStatus.BAD_REQUEST)
        .message(e.getLocalizedMessage())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = {HttpMessageNotReadableException.class})
  public ExceptionVo httpMessageNotReadableException(HttpMessageNotReadableException e) {
    log.error(e.toString());
    return ExceptionVo.builder()
        .status(HttpStatus.BAD_REQUEST)
        .message("필수 요청 값인 body가 존재하지 않습니다.")
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ConstraintViolationException.class})
  public ExceptionVo constraintViolation(final ConstraintViolationException e) {
    log.error(e.toString());
    return ExceptionVo.<Void>builder()
        .status(HttpStatus.BAD_REQUEST)
        .message(
            e.getConstraintViolations().stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .stream()
                .collect(Collectors.joining()))
        .build();
  }
}
