package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.PopImage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface popImageMapper {
    //写入弹框图片
    int insertPopimg(String openId, String imageUrl);
    //根据openid查询数据库
    List<PopImage> selectPop(String openId);
    //根据id删除数据
    int deletePop(Integer popId);
    //根据id查询
    PopImage selectPopId(Integer popId);
}
