package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.Cash;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface cashMapper {
    //按照openId和提现状态查询
    List<Cash> selectCash(String openId, Integer start);

    //根据orderId查询
    Cash selectOrderId(String orderId);

    Cash selectOrderIds(Integer id);

    //按照openId查询
    List<Cash> selectCashOpenId(String openId);

    //按照openId查询
    List<Cash> selectCashOpenIdA(List openId, Integer start, Integer pag);
    List<Cash> selectCashOpenIdAs(List openId, Integer start);

    List<Cash> selectCashOpenIdNo(List openId, Integer start, Integer pag);
    List<Cash> selectCashOpenIdNos(List openId, Integer start);

    //写入提现记录
    int insertCash(String orderId, String openId, String cashName, Double cashQuantity, String cashTime, Integer start);

    //更改提现状态
    int updataOrderId(String orderId);

    //根据订单状态查询
    List<Cash> selectCashAdmin(Integer start, Integer pag);
    List<Cash> selectCashAdmins(Integer start);
    //根据订单状态查询处理后的数据
    List<Cash> selectCashAdminNo(Integer start, Integer pag);
    List<Cash> selectCashAdminNos(Integer start);

    //根据时间和名称查询
    List<Cash> selectCashAdminTimeName(String time, List openId, Integer start, Integer pag);
    List<Cash> selectCashAdminTimeNames(String time, List openId, Integer start);

    //根据时间和名称查询
    List<Cash> selectCashAdminTimeNameNo(String time, List openId, Integer start, Integer pag);
    List<Cash> selectCashAdminTimeNameNos(String time, List openId, Integer start);

    //根据时间查询
    List<Cash> selectCashAdminTime(String time, Integer start, Integer pag);
    List<Cash> selectCashAdminTimes(String time, Integer start);

    //根据时间查询
    List<Cash> selectCashAdminTimeNo(String time, Integer start, Integer pag);
    List<Cash> selectCashAdminTimeNos(String time, Integer start);

    //根据主键id跟新记录
    int updataCash(Integer cashId, Integer start, String reason, String disposeTime);


}
