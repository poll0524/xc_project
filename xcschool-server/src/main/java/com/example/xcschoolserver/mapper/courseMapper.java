package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.Course;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface courseMapper {
    //分页随机查询所有课程
    List<Course> selectRandCourse(Integer pag);

    //根据分类名称查询课程列表
    List<Course> selectClassify(Integer classify, Integer pag);

    //根据id增加阅读量
    int updataQuantity(Integer courseId);

    //根据id查询课程详情
    Course selectCourseId(Integer courseId);

    //查询课程数量
    List<Course> selectCourseSum();

    //查询热门课程
    List<Course> selectTopCourse();
}
