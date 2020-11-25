package com.example.xcschoolserver.mapper;

import com.example.xcschoolserver.pojo.Xcuser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface xcuserMapper {
    //添加用户信息
    int insertUser(String openId, String userName, String userSex, String userProvince, String userCity, String userCountry, String userHeadimgurl, Integer sign, Integer start, Double userBalance, Integer brandQuantity);

    //根据phone查询记录
    Xcuser selectPhone(String userphone);

    //更新用户信息
    int updataXcuser(String openId, Integer sign);

    //查询所有
    List<Xcuser> selectxcuser();
    //查询所有
    List<Xcuser> selectxcusers(Integer pag);

    //根据openId查询
    Xcuser selectOpenId(String openId);

    //根据openid查询头像和姓名
    Xcuser selectOpenIdName(String openId);

    //根据openId更新电话号码
    int updataUserPhone(String openId, String phone);

    //根据openId更新加好友地址
    int updataUserCodeurl(String openId, String codeUrl);
    int updataUserTikTok(String openId, String codeUrl);
    int updataUserKuaiShou(String openId, String codeUrl);

    //根据openId更新权值
    int updataUserStart(String openId, Integer start);

    int updataUserStarts(String openId, Integer start, String beginTime, String endTime);

    //设置vip信息
    int updataUserVip(String beginTime, String endTime, String vipOrderId, String openId, Integer start, Integer brandQuantity);

    int updataUser(String openId, String userName, String userPhone, String userSex, String userProvince, String userCity, String userCountry, String userHeadimgUrl, String codeUrl, Integer start, String beginTime, String endTime, String vipOrderId, Integer brandQuantity, String email, String wechat, String selfPortrait, String company);

    int updataBrandQuantity(String openId);

    int updataUserVipTime(String endTime, String vipOrderId, String openId, Integer start);

    //更新佣金余额
    int updataUserBalance(String openId, Double userBalance);

    int updataBalance(String openId, Double userBalance);

    //根据用户名称模糊查询
    List<Xcuser> selectNameLike(String userName);

    //根据用户名称模糊查询
    List<Xcuser> selectNameLikes(String userName, Integer pag);


    int updataUserSelfPortrait(String openId, String selfPortrait);
    int updataUserWeChat(String openId, String weChat);
    int updataUserEmail(String openId, String email);
    int updataUserBusiness(String openId, String business);
    int updataUserPosition(String openId, String position);
    int updataUserprofession(String openId, String profession);

    int updataUserCompany(String openId, String company);

    int updataUserH5(String openId, String h5Name, String company, String weChat, String email, String phone, Integer h5);

    Xcuser updataUserInfo(String openId, String userName, String userSex, String userHeadimgUrl);
}
