package com.example.musinsatask.service.impl;

import com.example.musinsatask.mapper.ProductMapper;
import com.example.musinsatask.service.ProductDataInit;
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
  private final ProductDataInit productDataInit;

  @Override
  public List<ProductVo> findBrandLowestPriceItems(final List<ProductSelectVo> productVoList) {
    final Map<String, List<ProductVo>> listMap = productDataInit.productDataInit();
    final var resultProductItems = new ArrayList<ProductVo>();

    for (var productSelectVo : productVoList) {
      final ProductVo brandMinItem =
          findBrandMinItem(listMap.get(productSelectVo.getCategory()), productSelectVo.getBrand());

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
    final Map<String, List<ProductVo>> listMap = productDataInit.productDataInit();
    final List<String> categoryList = productMapper.categoryList();
    final var resultProductItems = new ArrayList<ProductVo>();

    for (String category : categoryList) {
      final ProductVo brandMinItem = findBrandMinItem(listMap.get(category), brandName);

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
    final Map<String, List<ProductVo>> listMap = productDataInit.productDataInit();
    final ProductVo categoryMinItem = findCategoryMinItem(listMap.get(category));
    final ProductVo categoryMaxItem = findCategoryMaxItem(listMap.get(category));

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
    final Map<String, List<ProductVo>> listMap = productDataInit.productDataInit();
    return findCategoryMaxItem(listMap.get(category));
  }

  @Override
  public ProductVo findCategoryLowestPriceItem(final String category) {
    final Map<String, List<ProductVo>> listMap = productDataInit.productDataInit();
    return findCategoryMinItem(listMap.get(category));
  }
}
