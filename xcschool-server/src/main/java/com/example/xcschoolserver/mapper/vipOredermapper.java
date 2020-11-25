package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.VipOreder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface vipOredermapper {
    //写入一条订单记录
    int insetVipOrder(String openId, String readOpenId, String orderNumber, Double orderMoney, Double brokerage, String orderTime, String payType, Double readBalance);
    //根据OpenId查询第一条
    VipOreder selectOpenId(String openId);
    //根据readOpenId查询
    List<VipOreder> selectReadOpenId(String readOpenId);

    //根据openId查询
    List<VipOreder> selectOpenIdList(String openId);

    //查询所有
    List<VipOreder> selectVipOrder();

}
