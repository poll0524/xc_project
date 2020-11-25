package com.example.datatoolserver.mapper;

import com.example.datatoolserver.pojo.AccessToken;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface accessTokenMapper {
    AccessToken selectNameAccessToken(String tokenName);

    int updataNameAccessToken(String tokenName, String tokenInfo, String tokenTime);
}
