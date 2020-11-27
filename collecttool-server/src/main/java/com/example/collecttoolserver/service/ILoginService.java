package com.example.collecttoolserver.service;

import org.json.JSONException;

import java.util.Map;

public interface ILoginService {
    Map<String, Object> jsCode(Map<String, Object> data) throws JSONException;

    String userLogin(Map<String, Object> data) throws Exception;
}
