package com.example.collecttoolserver.service;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public interface IShareService {
    String insertShare(Map<String,Object> data);

    List<Map<String,Object>> selectShare(Map<String,Object> data);

    Map<String,Object> updataPhoneTell(Map<String,Object> data);

    Map<String,Object> selectShareUser(Map<String,Object> data);

    Map<String,Object> getCode(Map<String,Object> data);
}
