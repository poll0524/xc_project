package com.example.collecttoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Cash {
    private Integer cash_id;
    private String order_id;
    private String cash_name;
    private Double cash_money;
    private String cash_time;
}
