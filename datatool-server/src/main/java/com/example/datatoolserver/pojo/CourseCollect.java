package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * 课程收藏表
 */
@Data
@ToString
public class CourseCollect {
    private Integer collect_id;
    private String open_id;
    private Integer course_id;
    private Integer start;
    private Integer tell;
}
