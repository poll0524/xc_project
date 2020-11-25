package com.example.datatoolserver.service;

import com.example.datatoolserver.pojo.BrandClassify;
import com.example.datatoolserver.pojo.BrandNav;
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

    Map<String,Object> adminBrandData(Map<String, Object> data, String userToken);

    Map<String,Object> adminBrandArticle(Map<String, Object> data, String userToken);

    Map<String,Object> adminBrandVideo(Map<String, Object> data, String userToken);

    List<BrandNav> adminBrandNav(Map<String, Object> data, String userToken);

    List<BrandClassify> adminBrandClassify(Map<String, Object> data, String userToken);
}
