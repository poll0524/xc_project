package com.example.xcschoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ShareVideo {
    //主键
    private Integer share_video_id;
    //openid
    private String open_id;
    //唯一id
    private Integer video_id;
    //视频名称
    private String video_name;
    //封面图片
    private String cover_url;
    //视频链接
    private String down_url;
    //视频分类
    private Integer video_classify;
    //对应id
    private Integer brand_video_id;
    //对应商品
    private String advertising1;
    //阅读数量
    private Integer quantity;
    //描述
    private String digest;
    //点赞
    private Integer video_like;
    //品牌作者
    private String author;
    private String pop_img;
    private Integer pop_code;
}
