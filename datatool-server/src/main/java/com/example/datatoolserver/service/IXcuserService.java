package com.example.datatoolserver.service;

import com.example.datatoolserver.pojo.Xcuser;
import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface IXcuserService {
    Map<String,Object> shopping(Map<String, Object> data);

    Map<String,Object> updataCode(Map<String, Object> data);

    Map<String,Object> selectUser(Map<String, Object> data, String userToken);

    Xcuser userInfo(Map<String, Object> data, String userToken);

    Map<String,Object> userSearch(Map<String, Object> data, String userToken);

    String updataUser(Map<String, Object> data, String userToken);

    String shineUpon(Map<String, Object> data);

    String message(@RequestBody Map<String, Object> data) throws JSONException;

    Map<String,Object> selectShineUpon(Map<String, Object> data);

    Map<String,Object> twoShineUpon(Map<String, Object> data);
}
