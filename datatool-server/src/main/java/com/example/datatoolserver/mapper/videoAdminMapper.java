package com.example.datatoolserver.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface videoAdminMapper {
    //写入一条视频
    int insertVideo(String videoName, String coverUrl, String downUrl, Integer videoClassify, Integer quantity, String digest, Integer videoLike, String author, Integer special);
}
