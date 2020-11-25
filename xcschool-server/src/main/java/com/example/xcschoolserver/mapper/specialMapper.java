package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.Special;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface specialMapper {
    List<Special> selectSpecial();
}
