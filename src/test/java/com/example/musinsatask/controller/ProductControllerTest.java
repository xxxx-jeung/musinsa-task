package com.example.musinsatask.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:/application.properties")
class ProductControllerTest {

  @Autowired MockMvc mockMvc;

  @Test
  @Rollback
  @DisplayName("선택한 상품 최저가 조회")
  void lowestPrice() throws Exception {
    ClassPathResource resource = new ClassPathResource("required01.json");
    Path path = Paths.get(resource.getURI());
    String json = Files.readString(path);

    mockMvc
        .perform(get("/products").content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("브랜드 최저가 조회")
  void lowestPriceBrandItems() throws Exception {
    mockMvc
        .perform(get("/products/brand").param("brandName", "D"))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("같은 상품 브랜드별 최고가 최저가")
  void highestAndLowestPriceBrandItems() throws Exception {
    mockMvc
        .perform(get("/products/top/max-min"))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());
  }
}
