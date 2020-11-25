package com.example.xcschoolserver.service;

import java.util.List;
import java.util.Map;

/**
 * @author saodiseng
 * @date 2019/2/18
 */
public interface IWxService {

    String payBack(String resXml);

    Map doUnifiedOrder(Map<String, Object> dataplay) throws Exception;

    Map<String,Object> earnings(Map<String, Object> data);

    List<Map<String,Object>> moneyRecord(Map<String, Object> data);

    List<Map<String,Object>> earningsRecord(Map<String, Object> data);

    String remittance(Map<String, Object> data) throws Exception;

    String outMoney(Map<String, Object> data);

    String getOrderId();

    Map<String,Object> cashStart(Map<String, Object> data);

}

