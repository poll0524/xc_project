package com.example.datatoolserver.service;

import com.example.datatoolserver.pojo.InTestMessage;
import com.example.datatoolserver.pojo.Xcuser;
import com.nimbusds.jose.JOSEException;
import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface IWeixinInertfaceService {
    Map login(String code) throws JSONException;

//    Map logins(Map<String,Object> data) throws JSONException;

    List<Xcuser> selectxcuser();

    int updataxcuser(String openId, Integer sign);

    Xcuser selectUser(String openId);

    String updataUserPhone(Map<String, Object> userPhone);

    Map<String,Object> insetGroup(Map<String, Object> group);

    Map<String, Object> selectGroup(String openId);

    List selectGroupName(Map<String, Object> data);

    Map<String, Object> switchGroup(Map<String, Object> group);

    String insertGroupMember(@RequestBody Map<String, Object> group);

    Map<String, Object> startGroup(Map<String, Object> start);

    String deleteGroup(List<Map<String, Object>> groups);

    String exitGroup(Map<String, Object> data);

    String quitGroup(Map<String, Object> group);

    Map<String,Object> sign(String openId);

    Map<String,Object> selectGroupInfo(Map<String, Object> data);

    Object handleMessage(InTestMessage inTestMessage) throws JSONException, JOSEException;

    String updataUserEmail(Map<String, Object> userPhone);

    String updataUserWeChat(Map<String, Object> userPhone);

    String updataUserSelfPortrait(Map<String, Object> userPhone);

    String updataUserCompany(Map<String, Object> userPhone);

    String updataUserBusiness(Map<String, Object> userPhone);

    String updataUserPosition(Map<String, Object> userPhone);



    List<Map<String,Object>> profession();
}
