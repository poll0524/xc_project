package com.example.xcschoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ShareCollect {
    private Integer collect_id;
    private String open_id;
    private Integer material;
    private Integer share;
}
