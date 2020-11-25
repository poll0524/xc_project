package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Course {
    private Integer course_id;
    private String title;
    private String author;
    private String digest;
    private String cover_img;
    private Integer quantity;
    private Integer classify;
    private String publish_time;
    private Double price;
    private Integer tell;
}
