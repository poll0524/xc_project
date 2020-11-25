package com.example.datatoolserver.mapper;


import com.example.datatoolserver.pojo.BrandCourseHour;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface brandCourseHourMapper {
    //根据课程id查询课时
    List<BrandCourseHour> selectHour(Integer courseId);
    BrandCourseHour selectHourInfo(Integer courseHourId);
}
