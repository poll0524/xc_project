package com.example.xcschoolserver.service;

import com.example.xcschoolserver.pojo.Xcuser;

import java.util.Map;

public interface IXcuserService {
    Map<String,Object> shopping(Map<String, Object> data);

    Map<String,Object> updataCode(Map<String, Object> data);

    Map<String,Object> selectUser(Map<String, Object> data, String userToken);

    Xcuser userInfo(Map<String, Object> data, String userToken);

    Map<String,Object> userSearch(Map<String, Object> data, String userToken);

    String updataUser(Map<String, Object> data, String userToken);
}
