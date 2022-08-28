package com.example.musinsatask.service.impl;

import com.example.musinsatask.mapper.ProductMapper;
import com.example.musinsatask.service.ProductDataInit;
import com.example.musinsatask.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductDataInitImpl implements ProductDataInit {

  private final ProductMapper productMapper;

  @Override
  @Cacheable(value = "productData")
  public Map<String, List<ProductVo>> productDataInit() {
    final List<String> categoryList = productMapper.categoryList();
    final Map<String, List<ProductVo>> productDataInit = new HashMap<>();

    for (String category : categoryList) {
      productDataInit.put(category, productMapper.productData(category));
    }

    return productDataInit;
  }
}
