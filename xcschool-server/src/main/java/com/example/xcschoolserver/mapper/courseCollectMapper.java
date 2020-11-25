package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.CourseCollect;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 收藏表
 */
@Mapper
public interface courseCollectMapper {
    //写入一天
    int insertCourseCollect(String openId, Integer courseId, Integer start, Integer tell);

    //根据openId课程/音频
    List<CourseCollect> selectCourseCollectList(String openId, Integer pag);

    //根据openId和courseId和start查询课程或者音频
    CourseCollect selectCollect(String openId, Integer courseId, Integer start, Integer tell);

    //根据主键id取消收藏
    int deleteCollect(Integer collectId);
}
