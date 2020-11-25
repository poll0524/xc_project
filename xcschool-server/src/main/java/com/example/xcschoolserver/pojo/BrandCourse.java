package com.example.xcschoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BrandCourse {
    private Integer course_id;
    private String title;
    private String author;
    private String digest;
    private String cover_img;
    private Integer quantity;
    private String classify;
    private Integer brand_id;
    private String publish_time;
    private Double price;
    private String open_id;
    private Integer tell;
}
