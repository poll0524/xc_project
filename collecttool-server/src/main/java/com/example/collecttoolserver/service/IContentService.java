package com.example.collecttoolserver.service;

import java.util.List;
import java.util.Map;

public interface IContentService {
    Map<String,Object> insertContent(Map<String,Object> data);

    Map<String,Object> selectContentTitle(Map<String,Object> data);

    Map<String,Object> selectContentInfo(Map<String,Object> data);

    List<Map<String,Object>> selectContentOpenId(Map<String,Object> data);

    String deleteContent(Map<String,Object> data);

    String updateContent(Map<String,Object> data);

    List<Map<String,Object>> selectClassify();
}
