package com.example.collecttoolserver.service;

import com.example.collecttoolserver.pojo.ShineUpon;

import java.util.List;
import java.util.Map;

public interface IEarningsService {
    //判断下级用户是否已经有上级了
    String insertShineUpon(Map<String,Object> data);

    Map<String,Object> selectShineUpon(Map<String,Object> data);

    List<Map<String,Object>> selectCash(Map<String,Object> data);

    List<Map<String,Object>> twoUsers(Map<String,Object> data);
}
