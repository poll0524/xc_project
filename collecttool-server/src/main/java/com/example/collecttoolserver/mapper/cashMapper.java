package com.example.collecttoolserver.mapper;

import com.example.collecttoolserver.pojo.Cash;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface cashMapper {
    int insertCash(String orderId,String openId,String cashName,Double cashMoney,String cashTime);
    List<Cash> selectCash(String openId,Integer pag);
}
