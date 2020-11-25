package com.example.xcschoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BillingImage {
    private Integer image_id;
    private String image_url;
    private String image_title;
    private Integer start;
}
