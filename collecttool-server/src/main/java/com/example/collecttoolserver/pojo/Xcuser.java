package com.example.collecttoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Xcuser {

    //用户主键
    private int user_id;
    //openid用户的唯一标识
    private String open_id;
    //用户昵称
    private String user_name;
    //用户电话
    private String user_phone;
    //用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
    private String user_sex;
    //用户个人资料填写的省份
    private String user_province;
    //普通用户个人资料填写的城市
    private String user_city;
    //国家，如中国为CN
    private String user_country;
    //头像地址
    private String user_headimgurl;

    private Integer start;

    //vip开始时间
    private String begin_time;
    //vip结束时间
    private String end_time;

    //余额
    private Double balance;

}
