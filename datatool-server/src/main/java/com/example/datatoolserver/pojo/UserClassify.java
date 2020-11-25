package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserClassify {
    private int id;
    private String open_id;
    private int classify_id;
}
