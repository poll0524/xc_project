package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BillingBrandClassify {
    private Integer classify_id;
    private String brand_classify_name;
    private Integer start;
}
