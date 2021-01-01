package com.example.collecttoolserver.mapper;

import com.example.collecttoolserver.pojo.VipOreder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface vipOrederMapper {
    //写入订单信息
    Integer insertOrder(String openId,String orderNumber,Double orderMoney,String orderTime,String payType,String brokerageOpenId,Double brokerage);
    //根据收益任查询佣金
    List<VipOreder> selectVipOrder(String openId,String brokerageOpenid);

    List<VipOreder> selectReadOpenId(String brokerageOpenid);
}
