package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ArticleClassify {
    private Integer classify_id;
    private String classify_name;
    private Integer classify_show;
}
