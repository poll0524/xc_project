package com.example.xcschoolserver.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleAdmin {
    private int id;
    private String title;//图文消息的标题-上传也需要
    private String thumb_media_id;//图文消息的封面图片素材id（必须是永久mediaID）-上传也需要
    private String author;//作者 -上传也需要
    private String digest;//图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
    private String content;//图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS-上传也需要
    private String cover_img;//封面图片
    private int article_classify;

    //是否属于专题
    private Integer special;

    //阅读量
    private Integer quantity;

    //图片路径
    private String content_img;
}
