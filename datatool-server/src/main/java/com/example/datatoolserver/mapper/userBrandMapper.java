package com.example.datatoolserver.mapper;


import com.example.datatoolserver.pojo.UserBrand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface userBrandMapper {
    //删除所有品牌
    int deleteUserBrand(String openId);
    //写入一条选择记录
    int insertUserBrand(String openId, Integer brandId);
    //根据openId
    UserBrand selectUserBrand(String openId, Integer brandId);

    //根据openId查询
    List<UserBrand> selectUserBrands(String openId);

}
