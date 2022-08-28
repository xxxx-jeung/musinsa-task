package com.example.musinsatask.service.impl;

import com.example.musinsatask.mapper.ProductMapper;
import com.example.musinsatask.redis.RedisService;
import com.example.musinsatask.service.ProductService;
import com.example.musinsatask.vo.ProductSelectVo;
import com.example.musinsatask.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.musinsatask.utils.ProductCategoryFindUtils.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductMapper productMapper;
  private final RedisService redisService;

  @Override
  public List<ProductVo> findBrandLowestPriceItems(final List<ProductSelectVo> productVoList) {
    final var resultProductItems = new ArrayList<ProductVo>();

    for (var productSelectVo : productVoList) {
      final ProductVo brandMinItem =
          findBrandMinItem(
              redisService.getValue(productSelectVo.getCategory()), productSelectVo.getBrand());

      resultProductItems.add(
          ProductVo.builder()
              .category(brandMinItem.getCategory())
              .brand(brandMinItem.getBrand())
              .price(brandMinItem.getPrice())
              .build());
    }

    return resultProductItems;
  }

  @Override
  public List<ProductVo> findBrandLowestPriceItems(final String brandName) {
    final List<String> categoryList = productMapper.categoryList();
    final var resultProductItems = new ArrayList<ProductVo>();

    for (String category : categoryList) {
      final ProductVo brandMinItem = findBrandMinItem(redisService.getValue(category), brandName);

      resultProductItems.add(
          ProductVo.builder()
              .category(brandMinItem.getCategory())
              .brand(brandMinItem.getBrand())
              .price(brandMinItem.getPrice())
              .build());
    }

    return resultProductItems;
  }

  @Override
  public Set<ProductVo> findCategoryHighestAndLowestPriceItems(final String category) {
    final ProductVo categoryMinItem = findCategoryMinItem(redisService.getValue(category));
    final ProductVo categoryMaxItem = findCategoryMaxItem(redisService.getValue(category));

    if (categoryMinItem.getPrice().equals(categoryMaxItem.getPrice())) {
      final List<ProductVo> productShuffleList =
          new ArrayList<>(List.of(categoryMinItem, categoryMaxItem));
      Collections.shuffle(productShuffleList);

      return Set.of(productShuffleList.get(0));
    }

    return Set.of(categoryMinItem, categoryMaxItem);
  }

  @Override
  public ProductVo findCategoryHighestPriceItem(final String category) {
    return findCategoryMaxItem(redisService.getValue(category));
  }

  @Override
  public ProductVo findCategoryLowestPriceItem(final String category) {
    return findCategoryMinItem(redisService.getValue(category));
  }
}
