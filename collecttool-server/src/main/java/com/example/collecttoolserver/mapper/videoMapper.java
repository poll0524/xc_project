package com.example.collecttoolserver.mapper;

import com.example.collecttoolserver.pojo.Video;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface videoMapper {
    //写入视频数据并返回id
    Integer insertVideo(String openId,String title,String digest,String coverImg,String author,String downUrl,String popImg,String popCode,String collectTime,String originalUrl,Integer videoLike,Integer readQuantity,String phone);

    //根据id查询视频
    Video selectVideoId(Integer videoId);
}
