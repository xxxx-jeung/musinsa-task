package com.example.musinsatask.controller;

import com.example.musinsatask.service.ProductService;
import com.example.musinsatask.utils.HttpResponse;
import com.example.musinsatask.vo.ProductSelectVo;
import com.example.musinsatask.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {
  private final ProductService productService;

  @GetMapping
  public HttpResponse<List<ProductVo>> lowestPriceItems(
      @RequestBody @Valid @NotNull(message = "카테고리가 존재하지 않습니다.")
          final List<ProductSelectVo> productSelectList) {
    final var items = productService.findBrandLowestPriceItems(productSelectList);

    return HttpResponse.toResponse(
        HttpStatus.OK,
        "브랜드, 카테고리 선택 별 최저가 상품 조회.",
        items,
        items.stream().map(ProductVo::getPrice).reduce(0, Integer::sum));
  }

  @GetMapping("/{brandName}/brand")
  public HttpResponse<List<ProductVo>> lowestPriceBrandItems(
      @PathVariable("brandName") @NotBlank(message = "브랜드가 존재하지 않습니다.") final String brandName) {
    final var items = productService.findBrandLowestPriceItems(brandName);
    final var total = items.stream().map(ProductVo::getPrice).reduce(0, Integer::sum);
    final var message = String.format("%s 브랜드 -> %d 원", brandName, total);

    return HttpResponse.toResponse(HttpStatus.OK, message, items, total);
  }

  @GetMapping("/{category}/max-min")
  public HttpResponse<List<ProductVo>> highestAndLowestPriceBrandItems(
      @PathVariable("category") @NotBlank(message = "브랜드가 존재하지 않습니다.") final String category) {
    final var foundHighestAndLowestPriceProductVo =
        productService.findCategoryHighestAndLowestPriceItems(category);
    final List<ProductVo> convertSetToList = new ArrayList<>(foundHighestAndLowestPriceProductVo);
    final String message;
    if (convertSetToList.size() > 1) {
      message =
          String.format(
              "최소 : %s 브랜드 -> %d 원, 최대 : %s 브랜드 -> %d 원",
              convertSetToList.get(0).getBrand(),
              convertSetToList.get(0).getPrice(),
              convertSetToList.get(1).getBrand(),
              convertSetToList.get(1).getPrice());
    } else {
      message =
          String.format(
              "%s 브랜드 -> %d 원",
              convertSetToList.get(0).getBrand(), convertSetToList.get(0).getPrice());
    }
    return HttpResponse.toResponse(HttpStatus.OK, message, convertSetToList);
  }
}
