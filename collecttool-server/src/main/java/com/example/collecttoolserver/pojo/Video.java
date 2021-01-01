package com.example.collecttoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Video {
    //视频主键id
    private Integer video_id;
    //用户openId
    private String open_id;
    //视频标题
    private String title;
    //视频简介
    private String digest;
    //视频封面图片
    private String cover_img;
    //作者
    private String author;
    //采集时间
    private String collect_time;
    //视频播放地址
    private String down_url;
    //广告图片
    private String pop_img;
    //二维码图片
    private String pop_code;
    //原始视频地址
    private String original_url;
    //视频点赞量
    private Integer video_like;
    //视频观看量
    private Integer read_quantity;
    private String phone;
    private String token;

    private String industry_name;
}
