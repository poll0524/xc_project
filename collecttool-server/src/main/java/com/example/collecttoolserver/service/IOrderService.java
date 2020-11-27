package com.example.collecttoolserver.service;

import java.util.Map;

public interface IOrderService {
    Map doUnifiedOrder(Map<String, Object> dataplay) throws Exception;

    String payBack(String resXml);
}
