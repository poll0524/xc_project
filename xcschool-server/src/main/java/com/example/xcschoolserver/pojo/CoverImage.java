package com.example.xcschoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CoverImage {
    private Integer img_id;
    private String media_id;
    private String img_name;
    private String update_time;
    private String url;
}
