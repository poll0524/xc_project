package com.example.datatoolserver.util;

import com.example.datatoolserver.common.WeChatUtil;
import com.example.datatoolserver.pojo.BrandNav;
import com.example.datatoolserver.pojo.Xcuser;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.datatoolserver.mapper.xcuserMapper;
import com.example.datatoolserver.mapper.brandNavMapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class UserInfo {
    @Autowired
    private xcuserMapper xcuserMapper;
    @Autowired
    private brandNavMapper brandNavMapper;
    /**
     * 写入用户信息
     */
    public Xcuser insertUser(JSONObject userInfo) throws JSONException {
        //获取用户基本信息
        String openId = userInfo.getString("openid");
        String userName =  userInfo.getString("nickname");
        String userSex = userInfo.getString("sex");
        String userProvince = userInfo.getString("province");
        String userCity = userInfo.getString("city");
        String userCountry = userInfo.getString("country");
        String userHeadimgurl = userInfo.getString("headimgurl");
        Integer sign = 0;
        //将用户信息写入数据库
        xcuserMapper.insertUser(openId,userName,userSex,userProvince,userCity,userCountry,userHeadimgurl,sign,0,0.00,0);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());// new Date()为获取当前系统时间

        //结束时间
        Calendar calendar2 = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar2.add(Calendar.DATE, 15);
        String endTime = sdf2.format(calendar2.getTime());
        xcuserMapper.updataUserStarts(openId,1,time,endTime);
        //查询该用户是否存在默认品牌
        BrandNav brandNav = brandNavMapper.selectBrandName(openId, WeChatUtil.AUTHOR);
        //如果没有默认品牌则创建默认品牌
        if (brandNav == null){
            brandNavMapper.insertBrand(openId, WeChatUtil.AUTHOR,1);
        }
        //返回该用户信息
        return xcuserMapper.selectOpenId(openId);
    }

    /**
     * 更新用户信息
     */
    public Xcuser updataUser(JSONObject userInfo,Xcuser xcuser) throws JSONException {
        //获取用户基本信息
        String openId = userInfo.getString("openid");
        String userName =  userInfo.getString("nickname");
        String userSex = userInfo.getString("sex");
        String userHeadimgurl = userInfo.getString("headimgurl");

        //如果昵称与头像与性别相同时
        if (userName.equals(xcuser.getUser_name()) && userSex.equals(xcuser.getUser_sex()) && userHeadimgurl.equals(xcuser.getUser_headimgurl())){
            return xcuser;
        }
        //如果昵称与头像与性别不相同时,更新用户信息

        xcuserMapper.updataUserInfo(openId,userName,userSex,userHeadimgurl);

        return xcuserMapper.selectOpenId(openId);

    }
}
