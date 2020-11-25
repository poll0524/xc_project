package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.BillingImage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface billingImageMapper {
    //分页查询分享有礼
    List<BillingImage> selectBillingImage(Integer pag);
}
