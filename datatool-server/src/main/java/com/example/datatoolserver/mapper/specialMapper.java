package com.example.datatoolserver.mapper;


import com.example.datatoolserver.pojo.Special;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface specialMapper {
    List<Special> selectSpecial();
}
