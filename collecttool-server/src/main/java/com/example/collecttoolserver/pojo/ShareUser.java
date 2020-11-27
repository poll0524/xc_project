package com.example.collecttoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ShareUser {
    //主键id
    private Integer share_id;

    //用户id
    private String share_open_id;

    //阅读用户id
    private String read_open_id;

    //分享文章id
    private Integer content_id;

    //阅读时间
    private String read_time;

    //0为文章1为视频
    private int start;

    //是否拨打过该电话:0为未拨打,1为已经拨打
    private int tell;

    //文章标题
    private String title;

}
