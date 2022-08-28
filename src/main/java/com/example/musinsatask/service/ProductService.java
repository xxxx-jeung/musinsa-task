package com.example.musinsatask.service;

import com.example.musinsatask.vo.ProductSelectVo;
import com.example.musinsatask.vo.ProductVo;

import java.util.List;
import java.util.Set;

public interface ProductService {
    List<ProductVo> findBrandLowestPriceItems(final List<ProductSelectVo> productVoList);
    List<ProductVo> findBrandLowestPriceItems(final String brandName);
    ProductVo findCategoryHighestPriceItem(final String category);
    ProductVo findCategoryLowestPriceItem(final String category);
    Set<ProductVo> findCategoryHighestAndLowestPriceItems(final String category);
}
