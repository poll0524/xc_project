package com.example.xcschoolserver.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * 我的品牌表
 */
@Data
@ToString
public class BrandNav {
    private Integer brand_id;
    private String open_id;
    private String brand_name;
    private Integer start;
    private String text_info;
    private Integer quantity;
    private String brand_time;
}
