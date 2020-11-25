package com.example.datatoolserver.mapper;

import com.example.datatoolserver.pojo.UserShopping;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface userShoppingMapper {
    //写入一条记录
    int insertUserShopping(String openId, String codeSite);

    //根据openId查询
    UserShopping selectOpenId(String openId);

    //根据openId跟新
    int updataOpenId(String openId, String codeSite);
}
