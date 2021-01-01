package com.example.collecttoolserver.mapper;

import com.example.collecttoolserver.pojo.Industry;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface industryMapper {
    List<Industry> selectIndustry();
}
