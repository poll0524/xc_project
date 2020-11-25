package com.example.xcschoolserver.mapper;

import com.example.xcschoolserver.pojo.Teacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface teacherMapper {
    Teacher selectTeacher(String openId);
}
