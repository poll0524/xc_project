package com.example.datatoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserShopping {
    public Integer shopping_id;

    public String open_id;

    public String shopping_site;

    public String code_site;
}
