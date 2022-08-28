package com.example.musinsatask.service.impl;

import com.example.musinsatask.mapper.ProductMapper;
import com.example.musinsatask.redis.RedisService;
import com.example.musinsatask.service.ProductDataInit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDataInitImpl implements ProductDataInit {

  private final ProductMapper productMapper;
  private final RedisService redisService;

  @Override
  public void productDataInit() {
    final List<String> categoryList = productMapper.categoryList();

    for (String category : categoryList) {
      if (redisService.getValue(category) == null) {
        redisService.setValue(category, productMapper.productData(category));
      }
    }
  }
}
