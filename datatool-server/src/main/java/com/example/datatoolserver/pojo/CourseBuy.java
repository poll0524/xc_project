package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * 购买课程表
 */
@ToString
@Data
public class CourseBuy {
    private Integer buy_id;
    private String open_id;
    private Integer course_id;
    private Integer course_hour_id;
    private Double price;
    private String buy_time;
    private Integer start;
    private Integer tell;
}
