package com.example.xcschoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BrandArticle {
    private Integer article_id;
    private String open_id;
    private String title;
    private String digest;
    private String cover_img;
    private String url;
    private Integer quantity;
    private Integer brand_id;
    private String content;
    private String token;
    private String author;
    private String classify;
//    private String article_time;
    private String publish_time;
}
