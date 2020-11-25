package com.example.xcschoolserver.service;

import org.json.JSONException;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface IShareService {

    String insertShareUser(Map<String, Object> share) throws JSONException;

    Map<String,Object> shareUserList(Map<String, Object> datac);

    Map<String,Object> readUser(Map<String, Object> users) throws ParseException;

    List<Map<String,Object>> selectGroupList(Map<String, Object> data);

    Map<String,Object> selectShareCourse(Map<String, Object> data) throws JSONException;

}
