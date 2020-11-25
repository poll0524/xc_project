package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PosterClassify {
    private Integer classify_id;
    private String poster_classify_name;
    private Integer start;
    private Integer tell;
}
