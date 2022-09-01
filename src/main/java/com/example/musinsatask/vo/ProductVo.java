package com.example.musinsatask.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class ProductVo implements Comparable<ProductVo> {
  @NotBlank(message = "브랜드가 존재하지 않습니다.")
  private String brand;

  @NotBlank(message = "카테고리가 존재하지 않습니다.")
  private String category;

  @NotNull(message = "가격이 존재하지 않습니다.")
  private Integer price;

  public ProductVo() {}

  @Builder
  public ProductVo(String brand, String category, Integer price) {
    this.brand = brand;
    this.category = category;
    this.price = price;
  }

  @Override
  public int compareTo(ProductVo o) {
    return this.getPrice() - o.getPrice();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ProductVo productVo = (ProductVo) o;

    if (brand != null ? !brand.equals(productVo.brand) : productVo.brand != null) return false;
    if (category != null ? !category.equals(productVo.category) : productVo.category != null)
      return false;
    return price != null ? price.equals(productVo.price) : productVo.price == null;
  }

  @Override
  public int hashCode() {
    int result = brand != null ? brand.hashCode() : 0;
    result = 31 * result + (category != null ? category.hashCode() : 0);
    result = 31 * result + (price != null ? price.hashCode() : 0);
    return result;
  }
}
