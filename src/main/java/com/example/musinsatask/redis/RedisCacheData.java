package com.example.musinsatask.redis;

import com.example.musinsatask.exception.BadRequestException;
import com.example.musinsatask.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Component
@Validated
@RequiredArgsConstructor
public class RedisCacheData {
  private final RedisService<Object> redisService;

  /**
   * 사용자가 브랜드와 카테고리를 선택한 상품 캐시 저장
   *
   * @param category
   * @param brand
   * @param lowestProductVo
   */
  public void lowestPriceProductAdd(
      @NotBlank(message = "카테고리가 존재하지 않습니다.") final String category,
      @NotBlank(message = "브랜드가 존재하지 않습니다.") final String brand,
      final ProductVo lowestProductVo) {

    if (lowestProductVo == null) {
      throw new BadRequestException(String.format("%s 브랜드에 %s 카테고리는 존재하지 않습니다.", brand, category));
    }
    redisService.setValue(category + "_" + brand, lowestProductVo);
  }

  /** 사용자가 브랜드와 카테고리를 선택한 상품 캐시 삭제 */
  public void lowestPriceProductRemove(
      final List<String> categoryList, @NotBlank(message = "브랜드가 존재하지 않습니다.") final String brand) {

    for (String category : categoryList) {
      redisService.deleteValue(category + "_" + brand);
    }
  }

  /**
   * 브랜드 카테고리 총 가격 캐시 저장
   *
   * @param brand
   * @param brandTotalSum
   */
  public void brandCategoryTotalPriceAdd(
      @NotBlank(message = "브랜드가 존재하지 않습니다.") final String brand, final Integer brandTotalSum) {

    if (brandTotalSum == null) {
      throw new BadRequestException(String.format("%s 브랜드에 카테고리가 존재하지 않습니다.", brand));
    }

    redisService.setValue("product_" + brand + "_total", brandTotalSum);
  }

  /** 브랜드 카테고리 총 가격 캐시 삭제 */
  public void brandCategoryTotalPriceRemove(
      @NotBlank(message = "브랜드가 존재하지 않습니다.") final String brand) {

    redisService.deleteValue("product_" + brand + "_total");
  }

  /**
   * 카테고리 별 최저, 최고가 캐시 저장
   *
   * @param category
   * @param minMaxProductSet
   */
  public void categoryProductMinMaxAdd(
      @NotBlank(message = "카테고리가 존재하지 않습니다.") final String category,
      Set<ProductVo> minMaxProductSet) {

    if (minMaxProductSet == null) {
      throw new BadRequestException(String.format("%s 는 카테고리에서 존재하지 않습니다.", category));
    }

    redisService.setValue("product_" + category + "_brand_min_max", minMaxProductSet);
  }

  /** 카테고리 별 최저, 최고가 캐시 삭제 */
  public void categoryProductMinMaxRemove(
      @NotNull(message = "카테고리 목록이 존재하지 않습니다.") final List<String> categoryList) {

    for (String category : categoryList) {
      redisService.deleteValue("product_" + category + "_brand_min_max");
    }
  }
}
