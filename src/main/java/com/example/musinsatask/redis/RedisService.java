package com.example.musinsatask.redis;

public interface RedisService<V> {
  V getValue(String key);

  void setValue(String key, V data);

  void deleteValue(String key);
}
