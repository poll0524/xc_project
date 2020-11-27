package com.example.collecttoolserver.service;

import java.util.List;
import java.util.Map;

public interface IShareService {
    String insertShare(Map<String,Object> data);

    List<Map<String,Object>> selectShare(Map<String,Object> data);
}
