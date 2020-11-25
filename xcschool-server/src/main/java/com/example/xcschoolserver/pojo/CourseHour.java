package com.example.xcschoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CourseHour {
    private Integer hour_id;
    private String title;
    private String down_url;
    private Integer course_id;
    private Integer hour_sort;
    private Integer hour_quantity;
    private Double price;
}
