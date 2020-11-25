package com.example.xcschoolserver.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ShareArticle {
    //主键
    private int share_id;

    //用户id
    private String open_id;

    private String title;
    //文章id
    private String thumb_media_id;

    private String author;

    private String digest;

    private String content;//图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS-上传也需要
    private String url;//图文页的URL，或者，当获取的列表是图片素材列表时，该字段是图片的URL
    private String cover_img;//封面图片
    private int show_cover_pic;//是否显示封面，0为false，即不显示，1为true，即显示-上传也需要

    //文章分类
    private int article_classify;

    //文章产品
    private String advertising1;

    private Integer brand_article_id;

    private Integer article_id;
}
