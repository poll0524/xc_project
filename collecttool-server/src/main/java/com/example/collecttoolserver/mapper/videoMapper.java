package com.example.collecttoolserver.mapper;

import com.example.collecttoolserver.pojo.Video;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface videoMapper {
    //写入视频数据并返回id
    Integer insertVideo(String openId,String title,String digest,String coverImg,String author,String downUrl,String popImg,String popCode,String collectTime,String originalUrl,Integer videoLike,Integer readQuantity,String phone,String token,String industryName);

    //根据id查询视频
    Video selectVideoId(Integer videoId);
    Video selectVideoToken(String token);
    //根据id跟新二维码
    int updataPopCode(Integer videoId,String popCode);
    //根据openId查询视频
    List<Video> selectContentOpenId(String openId,Integer pag);
    //根据id更新播放地址
    Integer updataDownUrl(Integer videoId,String downUrl);

    //根据id删除
    Integer deleteVideo(Integer videoId);

    //根据id字段更新
    Integer updataVideoId(Integer videoId,String title,String digest,String coverImg,String downUrl,String popImg,String phone,String industryName);
}
