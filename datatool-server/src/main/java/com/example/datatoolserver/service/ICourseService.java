package com.example.datatoolserver.service;

import java.util.List;
import java.util.Map;

public interface ICourseService {
    Map<String,Object> courseInfo(Map<String, Object> data);

    Map<String,Object> insertCollect(Map<String, Object> data);

    List<Map<String,Object>> selectCollectList(Map<String, Object> data);

    Map<String,Object> deleteCollect(Map<String, Object> data);

    String insertRecord(Map<String, Object> data);

    List<Map<String,Object>> selectRecord(Map<String, Object> data);

    List<Map<String,Object>> selectCourseBuy(Map<String, Object> data);

    String insertShareCourse(Map<String, Object> data);

    String updataHour(Map<String, Object> data);

    List<Map<String, Object>> uploadingCourser(Map<String, Object> data);
}
