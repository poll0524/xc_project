package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BrandVideo {
    private Integer video_id;
    private String open_id;
    private String title;
    private String digest;
    private String cover_img;
    private Integer quantity;
    private Integer brand_id;
    private Integer video_like;
    private String token;
    private String author;
    private String classify;
    private String video_time;
    private String down_url;
    private String pop_img;
    private Integer pop_code;
}
