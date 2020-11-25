package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.BillingBrandClassify;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface billingBrandClassifyMapper {
    //查询全部品牌快捷分类
    List<BillingBrandClassify> selectClassify();

    //根据名字查询
    BillingBrandClassify selectClassifyName(String brandClassifyName);
}
