package com.example.datatoolserver.service;

import com.example.datatoolserver.pojo.Reason;

import java.util.List;
import java.util.Map;

public interface IOrderAdminService {
    Map<String,Object> selectOrder(Map<String, Object> data);

    Map<String,Object> selectTimeOrder(Map<String, Object> data, String userToken);

    List<Reason> selectReason();

    String updataCash(Map<String, Object> data, String userToken);

    String outMoney(Map<String, Object> data) throws Exception;
}
