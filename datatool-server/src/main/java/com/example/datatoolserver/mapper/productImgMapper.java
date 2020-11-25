package com.example.datatoolserver.mapper;


import com.example.datatoolserver.pojo.ProductImg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface productImgMapper {
    int insertProductImg(String openId, String coverUrl, Integer start, Integer productId);

//    查询封面图片
    ProductImg selectProductImg(String openId, Integer productId);

    // 查询产品封面图
    List<ProductImg> selectProductImgList(String openId, Integer productId);

    //删除产品图片
    int deleteProductImg(String openId, Integer productId);

    int deleteProductImgs(String openId, String coverUrl, Integer productId);
}
