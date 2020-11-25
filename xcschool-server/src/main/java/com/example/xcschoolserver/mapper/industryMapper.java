package com.example.xcschoolserver.mapper;



import com.example.xcschoolserver.pojo.Industry;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface industryMapper {
    //查询行业
    List<Industry> selectIndustry();
}
