package com.example.musinsatask.controller;

import com.example.musinsatask.service.ProductService;
import com.example.musinsatask.service.impl.ProductServiceImplTest;
import com.example.musinsatask.vo.ProductSelectVo;
import com.google.gson.Gson;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:/application.properties")
class ProductControllerTest {

  @Autowired MockMvc mockMvc;
  @Autowired ProductService productService;
  ProductServiceImplTest productServiceImplTest = new ProductServiceImplTest();
  Gson gson = new Gson();

  @Test
  @Rollback
  @DisplayName("카테고리, 브랜드 선택 API - 선택한 상품 최저가 조회")
  void lowestPrice() throws Exception {
    ClassPathResource resource = new ClassPathResource("required01.json");
    Path path = Paths.get(resource.getURI());
    String json = Files.readString(path);

    mockMvc
        .perform(get("/products").content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.message").value("모든 카테고리의 상품을 브랜드 별로 자유롭게 선택해서 모든 상품을 구매할 때 최저가 조회 API"))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("카테고리, 브랜드 선택 API - 상품 한 건만 조회")
  void lowestPrice_one_product() throws Exception {
    ProductSelectVo productSelectVo = ProductSelectVo.builder().category("top").brand("A").build();
    List<ProductSelectVo> list = List.of(productSelectVo);

    mockMvc
        .perform(
            get("/products").content(gson.toJson(list)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.message").value("모든 카테고리의 상품을 브랜드 별로 자유롭게 선택해서 모든 상품을 구매할 때 최저가 조회 API"))
        .andExpect(jsonPath("$.data[0].brand").value("A"))
        .andExpect(jsonPath("$.data[0].category").value("top"))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("카테고리, 브랜드 선택 API - 데이터 공백 또는 없음")
  void lowestPrice_no_data() throws Exception {
    mockMvc
        .perform(get("/products").content("").contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("필수 요청 값인 body가 존재하지 않습니다."))
        .andExpect(status().is4xxClientError())
        .andDo(print());

    mockMvc
        .perform(get("/products").contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("필수 요청 값인 body가 존재하지 않습니다."))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("카테고리, 브랜드 선택 API - 브랜드가 공백 또는 존재하지 않음")
  void lowestPrice_no_brand() throws Exception {
    ProductSelectVo productSelectVo = ProductSelectVo.builder().category("top").build();
    List<ProductSelectVo> list = List.of(productSelectVo);

    mockMvc
        .perform(
            get("/products").content(gson.toJson(list)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("브랜드가 존재하지 않습니다."))
        .andExpect(status().is4xxClientError())
        .andDo(print());

    productSelectVo = ProductSelectVo.builder().category("top").brand("").build();
    list = List.of(productSelectVo);

    mockMvc
        .perform(
            get("/products").content(gson.toJson(list)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("브랜드가 존재하지 않습니다."))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("카테고리, 브랜드 선택 API - 카테고리가 공백 또는 존재하지 않음")
  void lowestPrice_no_category() throws Exception {
    ProductSelectVo productSelectVo = ProductSelectVo.builder().brand("A").build();
    List<ProductSelectVo> list = List.of(productSelectVo);

    mockMvc
        .perform(
            get("/products").content(gson.toJson(list)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("브랜드가 존재하지 않습니다."))
        .andExpect(status().is4xxClientError())
        .andDo(print());

    productSelectVo = ProductSelectVo.builder().category("").brand("A").build();
    list = List.of(productSelectVo);

    mockMvc
        .perform(
            get("/products").content(gson.toJson(list)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("브랜드가 존재하지 않습니다."))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("카테고리, 브랜드 선택 API - 브랜드, 카테고리 공백 또는 존재하지 않음")
  void lowestPrice_no_brand_no_category() throws Exception {
    ProductSelectVo productSelectVo = ProductSelectVo.builder().category("").brand("").build();
    List<ProductSelectVo> list = List.of(productSelectVo);

    mockMvc
        .perform(
            get("/products").content(gson.toJson(list)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("카테고리가 존재하지 않습니다."))
        .andExpect(status().is4xxClientError())
        .andDo(print());

    productSelectVo = ProductSelectVo.builder().build();
    list = List.of(productSelectVo);

    mockMvc
        .perform(
            get("/products").content(gson.toJson(list)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("카테고리가 존재하지 않습니다."))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("브랜드 최저가 조회 API - 브랜드 조회 성공")
  void _lowestPriceBrandItems() throws Exception {
    mockMvc.perform(get("/products/A/brand")).andExpect(status().is2xxSuccessful()).andDo(print());
    mockMvc.perform(get("/products/B/brand")).andExpect(status().is2xxSuccessful()).andDo(print());
    mockMvc.perform(get("/products/C/brand")).andExpect(status().is2xxSuccessful()).andDo(print());
    mockMvc.perform(get("/products/D/brand")).andExpect(status().is2xxSuccessful()).andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("브랜드 최저가 조회 API - 브랜드 path 없거나 공백")
  void lowestPriceBrandItems_no_brand() throws Exception {
    mockMvc.perform(get("/products/brand")).andExpect(status().is4xxClientError()).andDo(print());
    mockMvc
        .perform(get("/products/ /brand"))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("브랜드가 존재하지 않습니다."))
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("브랜드 추가")
  void saveBrand() throws Exception {
    mockMvc
        .perform(post("/products/brand").contentType(MediaType.APPLICATION_JSON).content("I"))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }
}
