package com.example.xcschoolserver.service;

import com.example.xcschoolserver.pojo.Article;
import com.example.xcschoolserver.pojo.BrandNav;
import com.nimbusds.jose.JOSEException;
import org.json.JSONException;

import java.util.List;
import java.util.Map;

public interface IArticleService {

    String selectArticlelist() throws JSONException;

    Map addArticle(Article articles) throws JSONException;

    List<Map> classifyList(String openId);

    Map<String,Object> openidClassify(Map<String, Object> data);

    List<Map<String, Object>> openidClassifys(Map<String, Object> data);

    Map<String,Object> updataUserClassify(Map<String, Object> obj);

    List<Map<String,Object>> classifyArticle(Map<String, Object> classify);

    String insertBrand(Map<String, Object> brand);

    Map<String,Object> articleInfo(Map<String, Object> articleInfo);

    String alterArticle(Map<String, Object> article) throws JOSEException;

    List<Map<String,Object>> selectArticleName(Map<String, Object> video);

    Map<String,Object> GroupList(Map<String, Object> group);

    Map getArticle(String url);
    Map getArticles(String url, String openId) throws JSONException, JOSEException;
    Map getVideo(String url);
    Map getVideos(String url, String openId);

    List<Map<String,Object>> specialList(Map<String, Object> data);

    List<Map<String,Object>> selectBrandTop(Map<String, Object> data);

    List<BrandNav> deleteBrand(Map<String, Object> data);

    Map<String,Object> selectBrandInfo(Map<String, Object> data);

    String selectBrandName(Map<String, Object> data);
    List<BrandNav> selectBrand(Map<String, Object> data);

    List<BrandNav> selectBrandList(Map<String, Object> data);

    BrandNav getBrandId(String openId, String name);
}
