package com.example.collecttoolserver.service.Impl;


import com.example.collecttoolserver.pojo.Xcuser;
import com.example.collecttoolserver.service.ILoginService;
import com.example.collecttoolserver.util.WeChatFinalUtil;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.collecttoolserver.mapper.xcuserMapper;

import java.util.Map;

@Service
public class LoginServiceImpl implements ILoginService {
    @Autowired
    private WeChatFinalUtil weChatFinalUtil;
    @Autowired
    private xcuserMapper xcuserMapper;

    public Map<String, Object> jsCode(Map<String,Object> data) throws JSONException {
        //获取code
        String code = (String) data.get("code");

        return weChatFinalUtil.userOpenId(code);
    }

    /**
     * 解密用户信息
     */
    public String userLogin(Map<String,Object> data) throws Exception {
        String encryptedData = (String) data.get("encryptedData");
        String iv = (String) data.get("iv");
        String sessionKey = (String) data.get("sessionKey");
        String userName = (String) data.get("userName");
        String userHeadimgurl = (String) data.get("userHeadimgurl");
        String openId = (String) data.get("openId");
        Integer sex = (Integer) data.get("userSex");//性别
        String userSex = sex.toString();
        String userProvince = (String) data.get("userProvince");//省份
        String userCity = (String) data.get("userCity");//城市
        String userCountry = (String) data.get("userCountry");//国家
        String userPhones = weChatFinalUtil.decryptUserInfo(encryptedData,iv,sessionKey);
        String[] phone = userPhones.split("\"");
        String userPhone = phone[3];
        //根据openId查询数据库
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);
        if (xcuser != null){
            //如果头像和手机号和昵称不一致是更新数据
            if (xcuser.getUser_name().equals(userName) || xcuser.getUser_phone().equals(userPhone) || xcuser.getUser_headimgurl().equals(userHeadimgurl)){
                xcuserMapper.updataOpenId(openId,userPhone,userName,userSex,userProvince,userCity,userCountry,userHeadimgurl);
                return "登录成功";
            }else {
                return "登录成功";
            }
        }
        //将数据写入数据库
        xcuserMapper.insertUserInfo(openId,userPhone,userName,userSex,userProvince,userCity,userCountry,userHeadimgurl);
        return "注册成功";
    }
}
