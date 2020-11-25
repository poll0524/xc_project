package com.example.xcschoolserver.service;

import com.nimbusds.jose.JOSEException;

import java.util.List;
import java.util.Map;


public interface IHomeService {
    Map<String,Object> selectHome(Map<String, Object> data);

    Map<String,Object> selectReadHome(Map<String, Object> data);

    Map<String,Object> cutHome(Map<String, Object> data);

    String updataInfo(Map<String, Object> data);

    String addProduct(Map<String, Object> data);

    Map<String,Object> selectProduct(Map<String, Object> data);

    String addArticle(Map<String, Object> data) throws JOSEException;

    Map<String,Object> selectArticle(Map<String, Object> data);

    Map<String,Object> selectPoster(Map<String, Object> data);

    Map<String, Object> selectArticleInfo(Map<String, Object> data);

    String insertLeave(Map<String, Object> data);

    String updataArticleInfo(Map<String, Object> data);

    String updataArticle(Map<String, Object> data);

    String deleteBrand(Map<String, Object> data);

    Map<String,Object> selectArticleList(Map<String, Object> data);

    List<Map<String,Object>> insertPop(Map<String, Object> data);

    List<Map<String,Object>> selectPop(Map<String, Object> data);

    List<Map<String,Object>> deletePop(Map<String, Object> data);

    String deleteOss(Map<String, Object> data);

    String insertH5(Map<String, Object> data);

    List topTeacher();

    List topCourse();

    List topArticle();
}
