package com.example.xcschoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Cash {
    //主键id
    private Integer cash_id;
    //openId
    private String open_id;
    //提现名称
    private String cash_name;
    //提现数量
    private Double cash_quantity;
    //提现时间
    private String cash_time;
    //提现状态
    private Integer start;
    //唯一值
    private String order_id;
    //拒绝理由
    private String reason;
    //处理时间
    private String dispose_time;
}
