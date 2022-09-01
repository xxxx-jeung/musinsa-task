package com.example.musinsatask.service.impl;

import com.example.musinsatask.exception.DuplicateException;
import com.example.musinsatask.exception.SQLBadParameterException;
import com.example.musinsatask.mapper.ProductMapper;
import com.example.musinsatask.redis.RedisCacheData;
import com.example.musinsatask.redis.RedisService;
import com.example.musinsatask.service.ProductService;
import com.example.musinsatask.vo.ProductVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@Slf4j
@Transactional
@SpringBootTest
@TestPropertySource("classpath:/application.properties")
public class ProductServiceImplTest {
  @Autowired ProductService productService;
  @Autowired ProductMapper productMapper;
  @Autowired RedisCacheData redisCacheData;
  @Autowired RedisService<Object> redisService;

  @Test
  void duplicate() {
    Set<ProductVo> data = new HashSet<>();

    ProductVo product1 = ProductVo.builder().brand("A").category("top").price(2500).build();
    ProductVo product2 = ProductVo.builder().brand("A").category("top").price(2500).build();

    data.add(product1);
    data.add(product2);

    System.out.println(data);
  }

  @Test
  void tree_set_duplicate() {
    Set<ProductVo> listSet = new TreeSet<>();

    ProductVo product1 = ProductVo.builder().brand("A").category("top").price(2500).build();
    ProductVo product2 = ProductVo.builder().brand("B").category("top").price(2500).build();

    if (product1.getPrice().equals(product2.getPrice())) {
      List<ProductVo> random = new ArrayList<>(List.of(product1, product2));
      log.info("before random : {}", random);

      Collections.shuffle(random);
      log.info("after random : {}", random);

      listSet.add(random.get(0));
    }

    System.out.println(listSet);
  }

  @Test
  @Rollback
  @DisplayName("브랜드 추가 - 브랜드 성공")
  void saveBrand() {
    assertThat(productService.saveBrand("P")).isEqualTo(1);
  }

  @Test
  @Rollback
  @DisplayName("브랜드 추가 - 브랜드 저장 중복")
  void saveBrand_duplicate() {
    assertThatExceptionOfType(DuplicateException.class)
        .isThrownBy(
            () -> {
              productService.saveBrand("A");
            })
        .withMessage("%s", "해당 브랜드는 이미 존재합니다.");
  }

  @Test
  @Rollback
  @DisplayName("브랜드 추가 - 브랜드 null")
  void saveBrand_brand_null() {
    assertThatExceptionOfType(SQLBadParameterException.class)
        .isThrownBy(
            () -> {
              productService.saveBrand(null);
            })
        .withMessage("%s", "브랜드가 존재하지 않습니다.");
  }

  @Test
  @Rollback
  @DisplayName("브랜드 삭제 - 브랜드 삭제")
  void deleteBrand() {
    assertThat(productService.deleteBrand("A")).isEqualTo(1);

    final List<String> categoryList = productMapper.categoryList();
    for(String category : categoryList){
      assertThat(redisService.getValue(category+"_A")).isNull();
      assertThat(redisService.getValue("product_A_total")).isNull();
      assertThat(redisService.getValue("product_"+category+"_brand_min_max")).isNull();
    }
  }

  @Test
  @Rollback
  @DisplayName("브랜드 삭제 - null")
  void deleteBrand_null() {
    assertThat(productService.deleteBrand(null)).isEqualTo(0);

    final List<String> categoryList = productMapper.categoryList();
    for(String category : categoryList){
      assertThat(redisService.getValue(category+"_A")).isNotNull();
      assertThat(redisService.getValue("product_A_total")).isNotNull();
      assertThat(redisService.getValue("product_"+category+"_brand_min_max")).isNotNull();
    }
  }

  @Test
  @Rollback
  void saveProduct() {}

  @Test
  @Rollback
  void updateProductCategory() {}

  @Test
  @Rollback
  void updateProductPrice() {}

  @Test
  @Rollback
  void deleteProduct() {}
}
