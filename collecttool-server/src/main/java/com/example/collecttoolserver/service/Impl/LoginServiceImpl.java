package com.example.collecttoolserver.service.Impl;


import com.example.collecttoolserver.common.ReturnVO;
import com.example.collecttoolserver.common.WeChatUtil;
import com.example.collecttoolserver.pojo.DateUpdate;
import com.example.collecttoolserver.pojo.ShareUser;
import com.example.collecttoolserver.pojo.Xcuser;
import com.example.collecttoolserver.service.ILoginService;
import com.example.collecttoolserver.util.AesCbcUtil;
import com.example.collecttoolserver.util.AliyunOsskeyUtil;
import com.example.collecttoolserver.util.WeChatFinalUtil;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.collecttoolserver.mapper.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginServiceImpl implements ILoginService {
    @Autowired
    private WeChatFinalUtil weChatFinalUtil;
    @Autowired
    private xcuserMapper xcuserMapper;
    @Autowired
    private AliyunOsskeyUtil aliyunOsskeyUtil;
    @Autowired
    private shareUserMapper shareUserMapper;
    @Autowired
    private dateUpdateMapper dateUpdateMapper;

    public Map<String, Object> jsCode(Map<String,Object> data) throws JSONException {
        //获取code
        String code = (String) data.get("code");
        Map<String,Object> map = weChatFinalUtil.userOpenId(code);
        return map;
    }

    /**
     * 获取用户电话号码
     */
    public String userPhoneLogin(Map<String,Object> data) throws Exception {
        String encryptedData = (String) data.get("encryptedData");
        String iv = (String) data.get("iv");
        String sessionKey = (String) data.get("sessionKey");
        String openId = (String) data.get("openId");
        String userPhones = weChatFinalUtil.decryptUserInfo(encryptedData,iv,sessionKey);
        System.out.println(userPhones);
//        String result = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
//        System.out.println(result);

        String[] phone = userPhones.split("\"");
        String userPhone = phone[3];
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);
        String getPhone = xcuser.getUser_phone();
        if (userPhone.equals(getPhone)){
            return "手机号未改变";
        }
        xcuserMapper.updataUserPhone(openId,userPhone);

        return "手机号录入成功";
    }

    /**
     * 解密用户信息
     */
    public Map<String,Object> userInfoLogin(Map<String,Object> data){
        String userName = (String) data.get("userName");
        String userHeadimgurl = (String) data.get("userHeadimgurl");
        String openId = (String) data.get("openId");
        Integer sex = (Integer) data.get("userSex");//性别
        String userSex = sex.toString();
        String userProvince = (String) data.get("userProvince");//省份
        String userCity = (String) data.get("userCity");//城市
        String userCountry = (String) data.get("userCountry");//国家
        //根据openId查询数据库
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);
        Map<String,Object> datas = new HashMap<>();
        if (xcuser != null){
            //是否有手机号
            String phone = xcuser.getUser_phone();
            //如果头像和昵称不一致是更新数据
            if (!xcuser.getUser_name().equals(userName) || !xcuser.getUser_headimgurl().equals(userHeadimgurl)){
                xcuserMapper.updataOpenId(openId,userName,userSex,userProvince,userCity,userCountry,userHeadimgurl);
                if(phone != null){
                    datas.put("userPhone",xcuser.getUser_phone());
                    datas.put("describe","登录成功,有手机号");
                    return datas;
                }else {
                    datas.put("userPhone",xcuser.getUser_phone());
                    datas.put("describe","登录成功,无手机号");
                    return datas;
                }
            }else {
                if(phone != null){
                    datas.put("userPhone",xcuser.getUser_phone());
                    datas.put("describe","登录成功,有手机号");
                    return datas;
                }else {
                    datas.put("userPhone","");
                    datas.put("describe","登录成功,无手机号");
                    return datas;
                }
            }
        }
        //将数据写入数据库
        xcuserMapper.insertUserInfo(openId,userName,userSex,userProvince,userCity,userCountry,userHeadimgurl,0.0);
        datas.put("userPhone","");
        datas.put("describe","注册成功,无手机号");
        return datas;
    }

    /**
     * oss配置
     */
    public Map<String,Object> ossInfo(Map<String,Object> data){
        String nostr = (String) data.get("nostr");
        Map<String,Object> datas = aliyunOsskeyUtil.getSignature(nostr);
        datas.put("key",nostr);
        datas.put("endpoint", WeChatUtil.ENDPOINT);
        datas.put("bucketName",WeChatUtil.BUCKETNAME);
        return datas;
    }

    /**
     * 数据红点
     */
    public String dataUpdate(Map<String,Object> data){
        //获取openId
        String openId = (String) data.get("openId");
        //根据openId查询数据库分享数据的数量
        List<ShareUser> shareUsers = shareUserMapper.selectShareUserSum(openId);
        //根据openid查询数据统计统计表
        DateUpdate dateUpdate = dateUpdateMapper.selectDateUpdate(openId);
        if (dateUpdate == null){
            dateUpdateMapper.insertDateUpdate(openId,0);
            if (shareUsers.size() > 0){
                return "有红点";
            }else {
                return "无红点";
            }
        }else {
            if (shareUsers.size() > dateUpdate.getDate_quantity()){
                dateUpdateMapper.updateDateUpdate(openId,shareUsers.size());
                return "有红点";
            }else {
                return "无红点";
            }
        }
    }
}
