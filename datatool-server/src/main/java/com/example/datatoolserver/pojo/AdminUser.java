package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AdminUser {
    private Integer admin_id;
    private String admin_user_name;
    private String admin_password;
    private String admin_name;
    private String admin_head;
    private String admin_position;
    private String admin_token;
    private Integer start;
    private String open_id;
}
