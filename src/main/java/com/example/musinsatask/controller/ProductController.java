package com.example.musinsatask.controller;

import com.example.musinsatask.exception.BadRequestException;
import com.example.musinsatask.service.ProductService;
import com.example.musinsatask.utils.HttpResponse;
import com.example.musinsatask.vo.ProductSelectVo;
import com.example.musinsatask.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {
  private final ProductService productService;

  @GetMapping
  public HttpResponse<List<ProductVo>> lowestPriceItems(
      @RequestBody @Valid @NotNull(message = "선택한 항목이 없습니다.")
          final List<ProductSelectVo> requestProduct) {
    final var foundProductList = productService.findBrandLowestPriceItems(requestProduct);

    return HttpResponse.toResponse(
        HttpStatus.OK,
        "모든 카테고리의 상품을 브랜드 별로 자유롭게 선택해서 모든 상품을 구매할 때 최저가 조회 API",
        foundProductList,
        foundProductList.stream().map(ProductVo::getPrice).reduce(0, Integer::sum));
  }

  @GetMapping("/{brandName}/brand")
  public HttpResponse<List<ProductVo>> lowestPriceBrandItems(
      @PathVariable("brandName") @NotBlank(message = "브랜드가 존재하지 않습니다.") final String brandName) {

    return HttpResponse.toResponse(
        HttpStatus.OK, brandName + " 브랜드", productService.findBrandLowestPriceItems(brandName));
  }

  @GetMapping("/{category}/max-min")
  public HttpResponse<Set<ProductVo>> highestAndLowestPriceBrandItems(
      @PathVariable("category") @NotBlank(message = "브랜드가 존재하지 않습니다.") final String category) {

    return HttpResponse.toResponse(
        HttpStatus.OK,
        "카테고리 이름으로 최소, 최대 가격 조회 API",
        productService.findCategoryHighestAndLowestPriceItems(category));
  }

  @PostMapping("/brand")
  public HttpResponse<Void> saveBrand(
          @RequestBody @NotBlank(message = "브랜드가 존재하지 않습니다.") final String brand) {
    if (productService.saveBrand(brand) == 0) {
      return HttpResponse.toResponse(HttpStatus.BAD_REQUEST, "브랜드가 생성되지 않았습니다.");
    }
    return HttpResponse.toResponse(HttpStatus.CREATED, "브랜드가 생성되었습니다.");
  }

  @DeleteMapping("/brand")
  public HttpResponse<Void> deleteBrand(
          @RequestBody @NotBlank(message = "브랜드가 존재하지 않습니다.") final String brand) {
    if (productService.deleteBrand(brand) == 0) {
      return HttpResponse.toResponse(HttpStatus.BAD_REQUEST, "브랜드가 삭제되지 않았습니다.");
    }
    return HttpResponse.toResponse(HttpStatus.CREATED, "브랜드가 삭제되었습니다.");
  }

  @PostMapping
  public HttpResponse<Void> saveProduct(
      @RequestBody @Valid @NotNull(message = "상품이 존재하지 않습니다.") final ProductVo productVo) {
    if (productService.saveProduct(productVo) == 0) {
      return HttpResponse.toResponse(HttpStatus.BAD_REQUEST, "상품이 생성되지 않았습니다.");
    }
    return HttpResponse.toResponse(HttpStatus.CREATED, "상품이 생성되었습니다.");
  }

  @PatchMapping("/category-name")
  public HttpResponse<Void> updateProductCategory(
      @RequestBody @NotNull(message = "변경할 상품이 존재하지 않습니다.")
          final ConcurrentHashMap<String, String> productData) {
    if (StringUtils.isBlank(productData.get("brand"))) {
      throw new BadRequestException("브랜드가 존재하지 않습니다.");
    }

    if (StringUtils.isBlank(productData.get("category"))) {
      throw new BadRequestException("카테고리가 존재하지 않습니다.");
    }

    if (StringUtils.isBlank(productData.get("updateCategory"))) {
      throw new BadRequestException("수정할 카테고리 이름이 존재하지 않습니다.");
    }

    if (productService.updateProductCategory(
            productData.get("brand"),
            productData.get("category"),
            productData.get("updateCategory"))
        == 0) {
      return HttpResponse.toResponse(HttpStatus.BAD_REQUEST, "상품이 수정되지 않았습니다.");
    }

    return HttpResponse.toResponse(HttpStatus.OK, "상품이 수정되었습니다.");
  }

  @PatchMapping("/category-price")
  public HttpResponse<Void> updateProductPrice(
      @RequestBody @NotNull(message = "변경할 상품이 존재하지 않습니다.")
          final ConcurrentHashMap<String, Object> productData) {
    if (StringUtils.isBlank(String.valueOf(productData.get("brand")))) {
      throw new BadRequestException("브랜드가 존재하지 않습니다.");
    }

    if (StringUtils.isBlank(String.valueOf(productData.get("category")))) {
      throw new BadRequestException("카테고리가 존재하지 않습니다.");
    }

    if (!StringUtils.isNumeric(String.valueOf(productData.get("price")))) {
      throw new BadRequestException("가격이 존재하지 않습니다.");
    }

    if (productService.updateProductPrice(
            (String) productData.get("brand"),
            (String) productData.get("category"),
            (Integer) productData.get("price"))
        == 0) {
      return HttpResponse.toResponse(HttpStatus.BAD_REQUEST, "상품이 수정되지 않았습니다.");
    }

    return HttpResponse.toResponse(HttpStatus.OK, "상품이 수정되었습니다.");
  }

  @DeleteMapping
  public HttpResponse<Void> deleteProduct(
      @RequestBody @NotNull(message = "삭제할 상품이 존재하지 않습니다.")
          final ConcurrentHashMap<String, String> productData) {
    if (StringUtils.isBlank(productData.get("brand"))) {
      throw new BadRequestException("브랜드가 존재하지 않습니다.");
    }

    if (StringUtils.isBlank(productData.get("category"))) {
      throw new BadRequestException("카테고리가 존재하지 않습니다.");
    }

    if (productService.deleteProduct(productData.get("brand"), productData.get("category")) == 0) {
      return HttpResponse.toResponse(HttpStatus.BAD_REQUEST, "상품이 삭제되지 않았습니다.");
    }

    return HttpResponse.toResponse(HttpStatus.OK, "상품이 삭제되었습니다.");
  }
}
