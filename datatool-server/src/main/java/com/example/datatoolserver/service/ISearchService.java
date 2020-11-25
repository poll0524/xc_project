package com.example.datatoolserver.service;

import java.util.List;
import java.util.Map;

public interface ISearchService {
    List<Map<String,Object>> searchList(Map<String, Object> data);

    String pushParticularly(String openId);

    String push(String openId);

    List<Map<String,Object>> selectShareNav();

    List<Map<String,Object>> selectPoster(Map<String, Object> data);

    List<Map<String,Object>> selectHelp(Map<String, Object> data);
}
