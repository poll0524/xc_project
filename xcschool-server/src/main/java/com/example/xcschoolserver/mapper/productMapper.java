package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface productMapper {
    //写入一个产品
    int insertProduct(String openId, Integer brandId, String productTitle, String productDescribe, String url, String price, Integer start, String describeImg, String VipPrice);

    //更新产品信息
    int updataProduct(Integer productId, String productTitle, String productDescribe, String url, String price, Integer start, String describeImg, String VipPrice);

    //删除品牌
    int deleteProduct(Integer productId);

    //根据openid和品牌Id查询列表
    List<Product> selectProductList(String openId, Integer brandId, Integer pag, Integer start);

    //根据productId查询产品详情
    Product selectProductInfo(Integer productId);

    //查询productId
    Product selectProductId(String openId, Integer brandId, String productTitle, String productDescribe, String url, String price, Integer start);
}
