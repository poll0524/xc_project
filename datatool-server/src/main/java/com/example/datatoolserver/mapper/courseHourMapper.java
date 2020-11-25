package com.example.datatoolserver.mapper;


import com.example.datatoolserver.pojo.CourseHour;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface courseHourMapper {
    //根据课程id查询课时
    List<CourseHour> selectCourseId(Integer courseId);

    int updataCourseHour(Integer hourId);
    //查询课时详情
    CourseHour selectCourse(Integer courseHourId);
}
