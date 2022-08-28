package com.example.musinsatask.redis;

import com.example.musinsatask.vo.ProductVo;

import java.util.List;

public interface RedisService {
  List<ProductVo> getValue(String key);

  void setValue(String key, List<ProductVo> data);
}
