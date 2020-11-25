package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Product {
   private Integer product_id;
   private String open_id;
   private Integer brand_id;
   private String product_title;
   private String product_describe;
   private String url;
   private String price;
   private Integer start;
   private String describe_img;
   private String vip_price;
}
