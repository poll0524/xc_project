package com.example.datatoolserver.mapper;

import com.example.datatoolserver.pojo.CountDown;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface countDownMapper {
    //根据openId查询
    CountDown selectCountDown(String openId);

    //写入一条数据
    int insertCountDown(String openId, String beginTime, String endTime);
}
