package com.example.musinsatask.vo;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ProductSelectVo {
  @NotBlank(message = "카테고리가 존재하지 않습니다.")
  private String category;
  @NotBlank(message = "브랜드가 존재하지 않습니다.")
  private String brand;
}
