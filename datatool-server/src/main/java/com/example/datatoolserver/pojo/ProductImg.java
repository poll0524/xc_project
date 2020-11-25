package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductImg {
    private Integer cover_id;
    private String open_id;
    private String cover_url;
    private Integer start;
    private Integer product_id;
}
