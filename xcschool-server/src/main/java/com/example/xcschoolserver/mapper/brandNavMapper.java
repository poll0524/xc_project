package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.BrandNav;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface brandNavMapper {
    //查询所有的品牌
    List<BrandNav> selectBrands(String openId);

    //增加我的品牌
    int insertBrand(String openId, String brandName, Integer show);
    int insertBrands(String openId, String brandName, Integer show, Integer quantity);

    //查询我的品牌
    List<BrandNav> selectBrand(String openId);

    List<BrandNav> selectBrandss(String openId);

    //根据id查询
    BrandNav selectBrandId(Integer brandId);

    BrandNav selectBrandName(String openId, String brandName);

    //查询品牌
    BrandNav selectBrandNames(String brandName);

    //更新我的品牌
    int updataBrand(String openId, String brandName);

    //跟新品牌显示
    int updataBrandShow(String openId, Integer show, Integer start);

    //跟新第一条
    int updataBrandOne(String openId);

    int updataBrandOnes(String openId);

    //根据openid和名字更改状态
    int updataBrandstart(String openId, String brandName, Integer start);

    //根据Id更新简介
    int updataTextInfo(Integer brandId, String textInfo);

    //删除我的品牌
    int deleteBrand(String openId, String brandName);

    //根据brandId进行删除
    int deleteBrandId(Integer brandId);
    //根据brandId进行清空
    int updataBrandId(Integer brandId);
    //根据brandid修改品牌名字
    int updataBrandName(Integer brandId, String brandName);

    //更新品牌数据
    int updataBrandInfo(Integer brandId, String brandName, Integer start, String brandTime);

    //查询品牌总数
    List<BrandNav> selectBrandNav();

    List<BrandNav> selectBrandUser(String openId);

    List<BrandNav> selectTopTeacher();
}
