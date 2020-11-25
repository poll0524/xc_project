package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BrandClassify {
    private Integer classify_id;
    private String open_id;
    private String brand_classify_name;
    private Integer brand_id;
    private Integer billing_classify;
}
