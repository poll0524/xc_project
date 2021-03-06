package com.example.xcschoolserver.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * vip开通人与邀请人的记录表
 */
@Data
@ToString
public class VipOreder {
    //主键
    private Integer order_id;
    //开通人openid
    private String open_id;
    //邀请人的openid
    private String read_open_id;
    //订单编号
    private String order_number;
    //订单金额
    private Double order_money;
    //佣金
    private Double brokerage;
    //订单时间
    private String order_time;
    //支付类型
    private String pay_type;
    //当前余额
    private String read_balance;
}
