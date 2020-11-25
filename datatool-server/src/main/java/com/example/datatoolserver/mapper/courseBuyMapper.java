package com.example.datatoolserver.mapper;


import com.example.datatoolserver.pojo.CourseBuy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 课程支付表
 */
@Mapper
public interface courseBuyMapper {
    //写入一天课程支付记录
    Integer insertCourseBuy(String openId, Integer courseId, Integer courseHourId, Double price, String buyTime, Integer start, Integer tell);

    //查询购买记录
    List<CourseBuy> selectCourseBuy(String openId, Integer courseId, Integer start);

    //根据openid查询
    List<CourseBuy> selectBuyOpenId(String openId, Integer pag);

    //查询购买记录
    CourseBuy selectCourseBuys(String openId, Integer courseId, Integer courseHourId, Integer start, Integer tell);
}
