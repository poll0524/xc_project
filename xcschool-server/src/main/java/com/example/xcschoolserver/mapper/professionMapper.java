package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.Profession;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface professionMapper {
    //根据行业id查询职业
    List<Profession> selectProfession(Integer industryId);
}
