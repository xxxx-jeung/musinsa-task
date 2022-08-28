package com.example.musinsatask.mapper;

import com.example.musinsatask.vo.ProductVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<ProductVo> productData(final String category);

    List<String> categoryList();
}
