package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Poster {
    private Integer poster_id;
    private String poster_url;
    private String poster_title;
    private Integer start;
    private Integer poster_classify;
}
