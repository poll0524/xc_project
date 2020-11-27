package com.example.collecttoolserver.service;

import java.util.Map;

public interface IContentService {
    Map<String,Object> insertContent(Map<String,Object> data);

    Map<String,Object> selectContentTitle(Map<String,Object> data);

    Map<String,Object> selectContentInfo(Map<String,Object> data);
}
