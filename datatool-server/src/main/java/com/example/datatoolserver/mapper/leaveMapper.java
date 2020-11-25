package com.example.datatoolserver.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface leaveMapper {
    //写入一条留言
    int insertLeave(String userName, String userPhone, Integer brandId, String openId, String textInfo);
}
