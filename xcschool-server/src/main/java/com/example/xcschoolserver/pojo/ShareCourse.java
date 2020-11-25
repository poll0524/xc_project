package com.example.xcschoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ShareCourse {
    private Integer share_course_id;
    private String open_id;
    private Integer course_id;
    private Integer start;
}
