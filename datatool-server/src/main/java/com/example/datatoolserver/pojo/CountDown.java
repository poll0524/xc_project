package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CountDown {
    private Integer down_id;
    private String open_id;
    private String begin_time;
    private String end_time;
}
