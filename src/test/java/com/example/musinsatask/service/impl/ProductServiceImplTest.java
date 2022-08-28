package com.example.musinsatask.service.impl;

import com.example.musinsatask.service.ProductDataInit;
import com.example.musinsatask.service.ProductService;
import com.example.musinsatask.vo.ProductVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.*;

import static com.example.musinsatask.utils.ProductCategoryFindUtils.findCategoryMaxItem;
import static com.example.musinsatask.utils.ProductCategoryFindUtils.findCategoryMinItem;

@Slf4j
@SpringBootTest
@TestPropertySource("classpath:/application.properties")
class ProductServiceImplTest {
  @Autowired ProductService productService;
  @Autowired ProductDataInit productDataInit;

  @Test
  void lowestPrice() throws IOException {}

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
  void sort() {
    String category = "top";
    final Map<String, List<ProductVo>> listMap = productDataInit.productDataInit();
    Set<ProductVo> listSet = new TreeSet<>();
    listSet.add(findCategoryMinItem(listMap.get(category)));
    listSet.add(findCategoryMaxItem(listMap.get(category)));
  }
}
