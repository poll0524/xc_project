package com.example.xcschoolserver.mapper;

import com.example.xcschoolserver.pojo.Push;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface pushMapper {
    //根据openId查询
    Push selectPush(String openId);
    //插入数据
    int insertPush(Integer pushMatinal, Integer pushMorning, Integer pushNooning, Integer pushAfternoon, Integer pushEvening, Integer pushNight, String openId);
    //查询所有
    List<Push> selectPushAll();
    //更新数据
    int updataPush(Integer pushMatinal, Integer pushMorning, Integer pushNooning, Integer pushAfternoon, Integer pushEvening, Integer pushNight, String openId);
}
