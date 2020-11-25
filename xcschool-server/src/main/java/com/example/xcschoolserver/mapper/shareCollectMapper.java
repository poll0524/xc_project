package com.example.xcschoolserver.mapper;

import com.example.xcschoolserver.pojo.ShareCollect;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface shareCollectMapper {
    //根据openId查询
    ShareCollect selectOpenId(String openId);
    //根据openId更新
    int updataOpenId(String openId, Integer material, Integer share);
    //写入一条记录
    int insertCollect(String openId, Integer material, Integer share);
}
