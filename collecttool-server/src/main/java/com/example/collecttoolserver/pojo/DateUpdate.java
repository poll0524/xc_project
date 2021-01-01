package com.example.collecttoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DateUpdate {
    private Integer date_id;
    private String open_id;
    private Integer date_quantity;
}
