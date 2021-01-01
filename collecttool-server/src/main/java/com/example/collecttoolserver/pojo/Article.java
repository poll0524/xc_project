package com.example.collecttoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Article {
    //文章主键
    private Integer article_id;
    //用户对应主键
    private String open_id;
    //文章标题
    private String title;
    //文章简介
    private String digest;
    //文章封面图片
    private String cover_img;
    //原文链接
    private String original_url;
    //文章作者
    private String author;
    //采集时间
    private String collect_time;
    //微信二维码
    private String pop_code;
    //广告图片
    private String pop_img;
    //文章阅读量
    private Integer read_quantity;
    private String phone;
    private String token;

}
