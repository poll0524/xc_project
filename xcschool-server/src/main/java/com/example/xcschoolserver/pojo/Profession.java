package com.example.xcschoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Profession {
    private Integer profession_id;
    private String profession_name;
    private Integer industry_id;
}
