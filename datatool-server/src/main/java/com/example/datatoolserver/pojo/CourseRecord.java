package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * 课程记录表
 */
@Data
@ToString
public class CourseRecord {
    private Integer record_id;
    private String open_id;
    private Integer course_id;
    private Integer start;
    private Integer record;
    private Integer tell;
}
