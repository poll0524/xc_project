package com.example.xcschoolserver.service;

import com.nimbusds.jose.JOSEException;

import java.util.List;
import java.util.Map;

public interface IContentAdminService {
    String insertArticle(Map<String, Object> data, String userToken) throws JOSEException;

    String insertVideo(Map<String, Object> data, String userToken) throws JOSEException;

    Map<String, Object> selectLogin(Map<String, Object> data) throws JOSEException;

    String updataPwd(Map<String, Object> data, String userToken);

    String selectToken(Map<String, Object> data);

    String deleteImage(Map<String, Object> data);

    String updataContent(Map<String, Object> data, String userToken);

    Map<String,Object> selectContent(Map<String, Object> data);

    Map<String,Object> selectConfirm(Map<String, Object> data);

    String deleteContent(Map<String, Object> data, String userToken);

    Map<String,Object> searchContent(Map<String, Object> data, String userToken);

    String updataArticle(Map<String, Object> data, String userToken);

    String updataVideo(Map<String, Object> data, String userToken);

    Map<String,Object> adminData(String userToken);

    List<Map<String,Object>> brandClassify();
}
