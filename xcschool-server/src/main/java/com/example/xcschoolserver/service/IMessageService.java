package com.example.xcschoolserver.service;

import java.text.ParseException;
import java.util.Map;

public interface IMessageService {

    Map<String,Object> selectShineUpon(Map<String, Object> data) throws ParseException;

    String updataStart(Map<String, Object> data);

    Map<String,Object> endTime(Map<String, Object> data) throws ParseException;
}
