package com.example.musinsatask.redis;

import com.example.musinsatask.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class RedisServiceImpl<V> implements RedisService<V> {
  private final RedisTemplate<String, V> redisTemplate;

  @Override
  public void setValue(String key, V data) {
    if (StringUtils.isBlank(key) || data == null) {
      throw new RuntimeException(
          String.format("키와 값이 존재하지 않습니다. - {key: %s, value: %s}", key, data));
    }

    ValueOperations<String, V> valueOperations = redisTemplate.opsForValue();
    valueOperations.set(key, data);
  }

  @Override
  public void deleteValue(String key) {
    if (StringUtils.isBlank(key)) {
      throw new RuntimeException(String.format("키가 존재하지 않습니다. - {key: %s}", key));
    }
    redisTemplate.delete(key);
  }

  @Override
  public V getValue(String key) {
    if (StringUtils.isBlank(key)) {
      throw new RuntimeException(String.format("키가 존재하지 않습니다. - {key: %s}", key));
    }
    ValueOperations<String, V> valueOperations = redisTemplate.opsForValue();
    return valueOperations.get(key);
  }
}
