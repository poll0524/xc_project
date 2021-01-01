package com.example.collecttoolserver.pojo;

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
    //订单编号
    private String order_number;
    //订单金额
    private String order_money;

    //订单时间
    private String order_time;
    //支付类型
    private String pay_type;

    //抽佣人
    private String brokerage_open_id;

    //佣金
    private Double brokerage;

}
