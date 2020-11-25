package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ShareUser {
    //主键id
    private int share_id;

    //用户id
    private String open_id;

    //阅读用户id
    private String read_open_id;

    //分享文章id
    private Integer article_id;

    //分享视频id
    private Integer video_id;

    //分享课程id
    private Integer course_id;

    //阅读开始时间

    private String read_time_begin;


    //0为文章1为视频
    private int start;

    //记录阅读或观看次数
    private int deg;


    private int tell;

    private String read_time;
}
