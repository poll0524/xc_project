package com.example.collecttoolserver.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface vipOrederMapper {
    //写入订单信息
    Integer insertOrder(String openId,String orderNumber,Double orderMoney,String orderTime,String payType);
}
