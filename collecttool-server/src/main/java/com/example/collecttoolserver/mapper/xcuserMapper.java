package com.example.collecttoolserver.mapper;

import com.example.collecttoolserver.pojo.Xcuser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface xcuserMapper {
    //根据openId查询用户信息
    Xcuser selectOpenId(String openId);
    //根据openId更新用户信息
    int updataOpenId(String openId,String userPhone,String userName,String userSex,String userProvince,String userCity,String userCountry,String userHeadimgurl);
    //添加用户信息
    int insertUserInfo(String openId,String userName,String userPhone,String userSex,String userProvince,String userCity,String userCountry,String userHeadimgurl);
    //根据openId更新会员等级时间
    int updataVip(String openId,Integer start,String beginTime,String endTime);
    //根据openId续费会员等级时间
    int updataVipGo(String openId,Integer start,String endTime);

    //查询所有用户
    List<Xcuser> selectXcuser();


}
