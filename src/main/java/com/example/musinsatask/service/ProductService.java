package com.example.musinsatask.service;

import com.example.musinsatask.vo.ProductSelectVo;
import com.example.musinsatask.vo.ProductVo;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

public interface ProductService {
    /**
     * 모든 카테고리의 상품을 브랜드별로 자유롭게 선택해서 모든 상품을 구매할 때 최저가 조회 API
     * @param productVoList 브랜드, 카테고리 선택 값
     * @return 선택한 브랜드, 카테고리별 최저가
     */
    List<ProductVo> findBrandLowestPriceItems(final List<ProductSelectVo> productVoList);

    /**
     * 한 브랜드에서 모든 카테고리의 상품을 한꺼번에 구매할 경우 최저가 및 브랜드 조회 API
     * @param brandName 해당 브랜드
     * @return 해당 브랜드의 카테고리 전체 총 가격
     */
    Integer findBrandLowestPriceItems(final String brandName);

    /**
     * 각 카테고리 이름으로 최소, 최대 가격 조회 API
     * @param category 해당 카테고리
     * @return 카테고리 최소, 최대 상품
     */
    Set<ProductVo> findCategoryHighestAndLowestPriceItems(final String category);

    /**
     * 브랜드 등록
     * @param brand 신규 브랜드
     * @return 성공 1 이상, 실패 0
     */
    int saveBrand(final String brand);

    /**
     * 브랜드 삭제
     * @param brand 해당 브랜드
     * @return 성공 1 이상, 실패 0
     */
    int deleteBrand(final String brand);

    /**
     * 상품 등록
     * @param productVo 신규 상품
     * @return 성공 1 이상, 실패 0
     */
    int saveProduct(final ProductVo productVo);

    /**
     * 상품 카테고리 수정
     * @param brand 대상 브랜드
     * @param category 대상 카테고리
     * @param updateCategory 카테고리 이름 수정
     * @return 성공 1 이상, 실패 0
     */
    int updateProductCategory(final String brand, final String category, final String updateCategory);

    /**
     * 상품 가격 수정
     * @param brand 대상 브랜드
     * @param category 대상 카테고리
     * @param price 상품 가격 변경
     * @return 성공 1 이상, 실패 0
     */
    int updateProductPrice(final String brand, final String category, final Integer price);

    /**
     * 상품 삭제
     * @param brand 대상 브랜드
     * @param category 대상 카테고리
     * @return 성공 1 이상, 실패 0
     */
    int deleteProduct(final String brand, final String category);
}
