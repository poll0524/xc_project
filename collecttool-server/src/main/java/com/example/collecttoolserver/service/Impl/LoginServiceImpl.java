package com.example.collecttoolserver.service.Impl;


import com.example.collecttoolserver.service.ILoginService;
import com.example.collecttoolserver.util.WeChatFinalUtil;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginServiceImpl implements ILoginService {
    @Autowired
    private WeChatFinalUtil weChatFinalUtil;

    public Map<String, Object> jsCode(Map<String,Object> data) throws JSONException {
        //获取code
        String code = (String) data.get("code");

        return weChatFinalUtil.userOpenId(code);
    }

    /**
     * 解密用户信息
     */
    public Map<String,Object> userInfo(Map<String,Object> data) throws Exception {
        String encryptedData = (String) data.get("encryptedData");
        String iv = (String) data.get("iv");
        String sessionKey = (String) data.get("sessionKey");
        System.out.println(weChatFinalUtil.decryptUserInfo(encryptedData,iv,sessionKey));
        return null;
    }
}
