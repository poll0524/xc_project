package com.example.datatoolserver.mapper;

import com.example.datatoolserver.pojo.VideoLike;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface videoLikeMapper {
    //写入一条点赞记录
    int insertVideoLike(String openId, Integer videoId, Integer brandVideoId, Integer shareVideoId, String readOpenId);

    //更新点赞记录
    int updataVideoLike(Integer likeId, String openId, Integer videoId, Integer brandVideoId, Integer shareVideoId);

    //查询点赞记录
    VideoLike selectVideoLike(Integer likeId);

    //根据open,videoId查询
    VideoLike selectVideoLikeVideoId(String openId, Integer videoId);

    //根据open,brandVideoId查询
    VideoLike selectVideoLikeBrandVideoId(String openId, Integer brandVideoId);

    //删除点赞记录
    int deleteVideoLike(Integer likeId);

    //根据open,videoId删除
    int deleteVideoLikeVideoId(String openId, Integer videoId);

    //根据open,brandVideoId删除
    int deletetVideoLikeBrandVideoId(String openId, Integer brandVideoId);

    int deleteVideoLikeVideoIdRead(String openId, Integer videoId, String readOpenId);

    int deleteVideoLikeBrandVideoIdRead(String openId, Integer brandVideoId, String readOpenId);
}
