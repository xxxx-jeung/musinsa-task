package com.example.musinsatask.utils;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class HttpResponse<T> {
  @NotNull(message = "응답상태가 존재하지 않습니다.")
  private final HttpStatus status;

  @NotBlank(message = "메세지가 존재하지 않습니다.")
  private final String message;

  private final T data;

  private final Integer total;

  @Builder
  HttpResponse(HttpStatus status, String message, T data, Integer total) {
    this.status = status;
    this.message = message;
    this.data = data;
    this.total = total;
  }

  public static <T> HttpResponse<T> toResponse(HttpStatus status, String message) {
    return toResponse(status, message, null);
  }

  public static <T> HttpResponse<T> toResponse(HttpStatus status, String message, T data) {
    return toResponse(status, message, data, null);
  }

  public static <T> HttpResponse<T> toResponse(HttpStatus status, String message, Integer total) {
    return toResponse(status, message, null, total);
  }

  public static <T> HttpResponse<T> toResponse(HttpStatus status, String message, T data, Integer total) {
    return HttpResponse.<T>builder().status(status).message(message).data(data).total(total).build();
  }
}
