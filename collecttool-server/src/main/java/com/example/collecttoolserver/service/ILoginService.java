package com.example.collecttoolserver.service;

import org.json.JSONException;

import java.util.Map;

public interface ILoginService {
    Map<String, Object> jsCode(Map<String, Object> data) throws JSONException;

    Map<String,Object> userInfo(Map<String, Object> data) throws Exception;
}
