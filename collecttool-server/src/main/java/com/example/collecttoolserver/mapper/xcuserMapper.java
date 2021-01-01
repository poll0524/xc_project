package com.example.collecttoolserver.mapper;

import com.example.collecttoolserver.pojo.Xcuser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface xcuserMapper {
    //根据openId查询用户信息
    Xcuser selectOpenId(String openId);
    //根据openId更新用户信息
    int updataOpenId(String openId,String userName,String userSex,String userProvince,String userCity,String userCountry,String userHeadimgurl);
    //添加用户信息
    int insertUserInfo(String openId,String userName,String userSex,String userProvince,String userCity,String userCountry,String userHeadimgurl,Double balance);
    //根据openId更新会员等级时间
    int updataVip(String openId,Integer start,String beginTime,String endTime);
    //根据openId续费会员等级时间
    int updataVipGo(String openId,Integer start,String endTime);
    //根据openId更新手机号
    int updataUserPhone(String openId,String userPhone);

    //查询所有用户
    List<Xcuser> selectXcuser();

    //更新添加余额
    int updataBalance(String openId,Double balance);

    //更新减少余额
    int updataBalances(String openId,Double balance);


}
