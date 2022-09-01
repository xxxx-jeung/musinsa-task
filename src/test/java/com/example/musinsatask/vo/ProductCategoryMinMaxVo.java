package com.example.musinsatask.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductCategoryMinMaxVo {
    private String priceStatus;
    private ProductVo productVo;

    public ProductCategoryMinMaxVo() {
    }

    @Builder
    public ProductCategoryMinMaxVo(String priceStatus, ProductVo productVo) {
        this.priceStatus = priceStatus;
        this.productVo = productVo;
    }
}
