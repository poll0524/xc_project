package com.example.collecttoolserver.service;

import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface ILoginService {
    Map<String, Object> jsCode(Map<String, Object> data) throws JSONException;

    String userPhoneLogin(Map<String,Object> data) throws Exception;

    Map<String,Object> userInfoLogin(Map<String, Object> data);

    Map<String,Object> ossInfo(Map<String,Object> data);

    String dataUpdate(Map<String,Object> data);
}
