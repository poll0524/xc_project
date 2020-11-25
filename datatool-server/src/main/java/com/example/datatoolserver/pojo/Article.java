package com.example.datatoolserver.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Article {
    private int id;
    private String title;//图文消息的标题-上传也需要
    private String thumb_media_id;//图文消息的封面图片素材id（必须是永久mediaID）-上传也需要
    private String author;//作者 -上传也需要
    private String digest;//图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
    private String content;//图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS-上传也需要
    private String url;//图文页的URL，或者，当获取的列表是图片素材列表时，该字段是图片的URL
    private String cover_img;//封面图片
    private String show_cover_pic;//是否显示封面，0为false，即不显示，1为true，即显示-上传也需要
    private int article_classify;

    //是否属于专题
    private Integer special;

    //阅读量
    private Integer quantity;

    //图片路径
    private String content_img;

    //发布时间
    private String publish_time;
}
