package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.CourseRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface courseRecordMapper {
    //写入一条课程记录
    int insertcourseRecord(String openId, Integer courseId, Integer start, Integer record, Integer tell);
    //根据openId和courseId和start查询观看记录
    CourseRecord selectCourseRecord(String openId, Integer courseId, Integer start, Integer tell);
    //写入一条课程记录
    int updatacourseRecord(String openId, Integer courseId, Integer start, Integer record);
    //根据openid查询观看记录
    List<CourseRecord> selectRecordOpenId(String openId, Integer pag);

}
