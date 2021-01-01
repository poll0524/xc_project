package com.example.collecttoolserver.mapper;

import com.example.collecttoolserver.pojo.ShineUpon;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface shineUponMapper {
    ShineUpon selectTowOpenId(String twoOpenId);

    Integer insertShineUpon(String oneOpenId,String twoOpenId);

    List<ShineUpon> selectOneOpenId(String oneOpenId);

    List<ShineUpon> selectOneOpenIds(String oneOpenId,Integer pag);

    ShineUpon selectOneTowOpenId(String oneOpenId,String twoOpenId);
}
