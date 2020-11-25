package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.ShineUpon;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface shineUponMapper {
    //写入一条记录
    int insertShine(String openId, String readOpenId);

    //根据openId查询
    ShineUpon selectOpenId(String openId);

    int updataShine(Integer shineId, String openId, String readOpenId);

    //根据readOpenId查询
    List<ShineUpon> selectReadOpenId(String readOpenId);

    //查询对应关系是否存在
    ShineUpon selectShine(String openId, String readOpenId);


}
