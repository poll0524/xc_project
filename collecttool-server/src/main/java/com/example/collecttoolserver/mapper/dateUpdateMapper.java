package com.example.collecttoolserver.mapper;

import com.example.collecttoolserver.pojo.DateUpdate;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface dateUpdateMapper {
    DateUpdate selectDateUpdate(String openId);
    Integer insertDateUpdate(String openId,Integer dateQuantity);
    Integer updateDateUpdate(String openId,Integer dateQuantity);
}
