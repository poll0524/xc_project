package com.example.xcschoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Video {
//    主键
    private Integer video_id;
    //视频名称
    private String video_name;
    //封面图片
    private String cover_url;
    //视频链接
    private String down_url;
    //视频分类
    private Integer video_classify;
    //查看数量
    private Integer quantity;
    //视频描述
    private String digest;
    //视频点赞
    private Integer video_like;
    //作者
    private String author;
    //是否属于专题
    private Integer special;

    private String show_cover_pic;

    private String thumb_media_id;

    //发布时间
    private String publish_time;
}
