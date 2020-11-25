package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.UserClassify;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface userClassifyMapper {
    //根据openId查询
    List<UserClassify> selectUserClassify(String openId);

    //根据openId删除数据
    int deleteUserClassify(String openId);

    //插入一条记录
    int insertUserClassify(String openId, int classifyId);
}
