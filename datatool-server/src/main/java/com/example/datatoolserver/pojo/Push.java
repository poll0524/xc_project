package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Push {
    private int push_id;
    private int push_matinal;
    private int push_morning;
    private int push_nooning;
    private int push_afternoon;
    private int push_evening;
    private int push_night;
    private String open_id;
}
