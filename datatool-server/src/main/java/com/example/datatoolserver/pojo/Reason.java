package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Reason {
    private Integer reason_id;
    private String reason_text;
    private Integer start;
}
