package com.example.datatoolserver.mapper;


import com.example.datatoolserver.pojo.ShareCourse;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分享课程
 */
@Mapper
public interface shareCourseMapper {
    //写入分享课程
    int insertShareCourse(String openId, Integer courseId, Integer start);

    //根据openid,id,start查询
    ShareCourse selectShareCourse(String openId, Integer courseId, Integer start);

}
