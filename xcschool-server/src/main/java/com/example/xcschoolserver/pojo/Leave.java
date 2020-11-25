package com.example.xcschoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Leave {
    private Integer id;
    private String username;
    private String userphone;
    private String open_id;
    private String brand_id;
    private String text_info;
}
