package com.example.musinsatask.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ProductVo implements Comparable<ProductVo> {
  private String brand;
  private String category;
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
