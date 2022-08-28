package com.example.musinsatask.utils;

import com.example.musinsatask.vo.ProductVo;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;

public class ProductCategoryFindUtils {

  public static ProductVo findBrandMinItem(final List<ProductVo> productVoList, final String brandName) {

    return productVoList.stream()
        .filter(s -> StringUtils.equals(brandName, s.getBrand()))
        .min(Comparator.comparingInt(ProductVo::getPrice))
        .orElseThrow(
            () -> {
              throw new RuntimeException("존재하지 않습니다.");
            });
  }

  public static ProductVo findCategoryMinItem(final List<ProductVo> productVoList) {

    return productVoList.stream()
        .min(Comparator.comparingInt(ProductVo::getPrice))
        .orElseThrow(
            () -> {
              throw new RuntimeException("존재하지 않습니다.");
            });
  }

  public static ProductVo findCategoryMaxItem(final List<ProductVo> productVoList) {

    return productVoList.stream()
        .max(Comparator.comparingInt(ProductVo::getPrice))
        .orElseThrow(
            () -> {
              throw new RuntimeException("존재하지 않습니다.");
            });
  }
}
