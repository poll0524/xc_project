package com.example.xcschoolserver.service;

import java.util.List;
import java.util.Map;

public interface IVideoService {
    String alterVideo(Map<String, Object> video);

    List<Map<String,Object>> selectVideo(Map<String, Object> video);

    Map<String,Object> videoInfo(Map<String, Object> video);

    Integer videoLike(Map<String, Object> data);
}
