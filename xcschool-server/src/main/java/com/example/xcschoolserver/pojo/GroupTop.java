package com.example.xcschoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GroupTop {
    private int group_id;
    private String group_token;
    private String open_id;
    private String group_name;
    private String group_member_openid;
    private Integer group_start;
    private Integer group_show;
}
