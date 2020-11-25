package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.BrandClassify;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface brandClassifyMapper {
    //写入一条品牌二级分类
    int insertBrandClassify(String openId, String brandClassifyName, Integer brandId, Integer billingClassify);
    //根据品牌id删除分类
    int deleteBrandClassify(Integer brandId);

    //根据品牌id查询二级分类
    List<BrandClassify> selectBrandClassify(Integer brandId);

    //根据品牌id与二级分类名称查询
    BrandClassify selectBrandName(Integer brandId, String brandClassifyName);

    //根据品牌id与二级分类名称删除
    int deleteBrandName(Integer brandId, String brandClassifyName);
}
