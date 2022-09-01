package com.example.musinsatask.mapper;

import com.example.musinsatask.vo.ProductVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

  List<String> categoryList();

  List<String> brandList();

  ProductVo lowestPriceProduct(
      @Param("category") final String category, @Param("brand") final String brand);

  List<ProductVo> productData(final String category);

  Integer brandTotalSum(@Param("brand") final String brand);

  int saveBrand(@Param("brand") final String brand);

  int deleteBrand(@Param("brand") final String brand);

  /**
   * 브랜드가 삭제되면 해당 브랜드 상품을 모두 삭제한다.
   *
   * @param brand 삭제 대상 브랜드
   * @return 성공 1 이상, 0 삭제 상품 없음
   */
  int deleteBrandProductAll(@Param("brand") final String brand);

  int saveProduct(final ProductVo productVo);

  int updateProductCategory(
      @Param("brand") final String brand,
      @Param("category") final String category,
      @Param("updateCategory") final String updateCategory);

  int updateProductPrice(
      @Param("brand") final String brand,
      @Param("category") final String category,
      @Param("price") final Integer price);

  int deleteProduct(@Param("brand") final String brand, @Param("category") final String category);
}
