package com.example.musinsatask.service.impl;

import com.example.musinsatask.exception.BadRequestException;
import com.example.musinsatask.exception.DuplicateException;
import com.example.musinsatask.exception.SQLBadParameterException;
import com.example.musinsatask.mapper.ProductMapper;
import com.example.musinsatask.redis.RedisCacheData;
import com.example.musinsatask.redis.RedisService;
import com.example.musinsatask.service.ProductService;
import com.example.musinsatask.vo.ProductSelectVo;
import com.example.musinsatask.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@Validated
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductMapper productMapper;
  private final RedisCacheData redisCacheData;
  private final RedisService<Object> redisService;

  @Override
  public List<ProductVo> findBrandLowestPriceItems(final List<ProductSelectVo> productVoList) {
    final List<ProductVo> resultProductItems = new ArrayList<>();

    for (ProductSelectVo productSelectVo : productVoList) {
      ProductVo lowestPriceProduct =
          (ProductVo)
              redisService.getValue(
                  productSelectVo.getCategory() + "_" + productSelectVo.getBrand());

      if (lowestPriceProduct == null) {
        lowestPriceProduct =
            productMapper.lowestPriceProduct(
                productSelectVo.getCategory(), productSelectVo.getBrand());
        redisCacheData.lowestPriceProductAdd(
            productSelectVo.getCategory(), productSelectVo.getBrand(), lowestPriceProduct);
      }

      resultProductItems.add(
          ProductVo.builder()
              .category(lowestPriceProduct.getCategory())
              .brand(lowestPriceProduct.getBrand())
              .price(lowestPriceProduct.getPrice())
              .build());
    }

    return resultProductItems;
  }

  @Override
  public Integer findBrandLowestPriceItems(final String brandName) {
    final Integer brandPriceTotal =
        (Integer) redisService.getValue("product_" + brandName + "_total");

    if (brandPriceTotal == null) {
      final Integer brandTotalSum = productMapper.brandTotalSum(brandName);
      redisCacheData.brandCategoryTotalPriceAdd(brandName, brandTotalSum);
      return brandTotalSum;
    }

    return brandPriceTotal;
  }

  @Override
  public Set<ProductVo> findCategoryHighestAndLowestPriceItems(final String category) {
    final Set<ProductVo> categoryProduct =
        (Set<ProductVo>) redisService.getValue("product_" + category + "_brand_min_max");

    if (categoryProduct == null) {
      final Set<ProductVo> findCategoryProduct =
          Set.of(
              productMapper.productData(category).stream()
                  .min(Comparator.comparingInt(ProductVo::getPrice))
                  .orElseThrow(
                      () -> {
                        throw new BadRequestException("해당 카테고리는 존재하지 않습니다.");
                      }),
              productMapper.productData(category).stream()
                  .max(Comparator.comparingInt(ProductVo::getPrice))
                  .orElseThrow(
                      () -> {
                        throw new BadRequestException("해당 카테고리는 존재하지 않습니다.");
                      }));
      redisCacheData.categoryProductMinMaxAdd(category, findCategoryProduct);

      return findCategoryProduct;
    }

    return categoryProduct;
  }

  @Override
  public int saveBrand(String brand) {
    try {
      return productMapper.saveBrand(brand);
    } catch (DuplicateKeyException e) {
      throw new DuplicateException("해당 브랜드는 이미 존재합니다.");
    } catch (DataIntegrityViolationException e) {
      throw new SQLBadParameterException("브랜드가 존재하지 않습니다.");
    }
  }

  @Override
  public int deleteBrand(String brand) {
    /*
    TODO :: 사용자가 브랜드, 카테고리 선택시 저장한 캐시데이터 초기화
    TODO :: 브랜드 삭제시 브랜드 총 가격 초기화
    TODO :: 브랜드 삭제시 카테고리 별 최소, 최대 값 초기화
     */
    if (productMapper.deleteBrandProductAll(brand) == 0) {
      return 0;
    }

    if (productMapper.deleteBrand(brand) == 0) {
      return 0;
    }

    final List<String> categoryList = productMapper.categoryList();
    redisCacheData.lowestPriceProductRemove(categoryList, brand);
    redisCacheData.brandCategoryTotalPriceRemove(brand);
    redisCacheData.categoryProductMinMaxRemove(categoryList);

    return 1;
  }

  @Override
  public int saveProduct(ProductVo productVo) {
    /*
    TODO :: 상품 등록시 브랜드 총 가격 초기화
    TODO :: 상품 등록시 카테고리 별 최소, 최대 값 초기화
     */

    if (productMapper.saveProduct(productVo) == 0) {
      return 0;
    }

    redisCacheData.brandCategoryTotalPriceRemove(productVo.getBrand());
    redisCacheData.categoryProductMinMaxRemove(productMapper.categoryList());

    return 1;
  }

  @Override
  public int updateProductCategory(String brand, String category, String updateCategory) {
    /*
    TODO :: 사용자가 브랜드, 카테고리 선택시 저장한 캐시데이터 초기화
    TODO :: 카테고리 이름 수정시 카테고리 별 최소, 최대 값 초기화
     */

    if (productMapper.updateProductCategory(brand, category, updateCategory) == 0) {
      return 0;
    }

    final List<String> categoryList = productMapper.categoryList();
    redisCacheData.lowestPriceProductRemove(categoryList, brand);
    redisCacheData.categoryProductMinMaxRemove(categoryList);

    return 1;
  }

  @Override
  public int updateProductPrice(String brand, String category, Integer price) {
    /*
    TODO :: 사용자가 브랜드, 카테고리 선택시 저장한 캐시데이터 초기화
    TODO :: 상품 가격 수정시 브랜드 별 총 가격 초기화
    TODO :: 상품 가격 수정시 카테고리 별 최소, 최대 값 초기화
     */

    if (productMapper.updateProductPrice(brand, category, price) == 0) {
      return 0;
    }

    final List<String> categoryList = productMapper.categoryList();
    redisCacheData.lowestPriceProductRemove(categoryList, brand);
    redisCacheData.brandCategoryTotalPriceRemove(brand);
    redisCacheData.categoryProductMinMaxRemove(categoryList);

    return 1;
  }

  @Override
  public int deleteProduct(String brand, String category) {
    /*
    TODO :: 사용자가 브랜드, 카테고리 선택시 저장한 캐시데이터 초기화
    TODO :: 상품 삭제시 브랜드 별 총 가격 초기화
    TODO :: 상품 삭제시 카테고리 별 최소, 최대 값 초기화
     */

    if (productMapper.deleteProduct(brand, category) == 0) {
      return 0;
    }

    final List<String> categoryList = productMapper.categoryList();
    redisCacheData.lowestPriceProductRemove(categoryList, brand);
    redisCacheData.brandCategoryTotalPriceRemove(brand);
    redisCacheData.categoryProductMinMaxRemove(categoryList);

    return 1;
  }
}
