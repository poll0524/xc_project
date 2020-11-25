package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AccessToken {
    private Integer token_id;
    private String token_name;
    private String token_info;
    private String token_time;
}
