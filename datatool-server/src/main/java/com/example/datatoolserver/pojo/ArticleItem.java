package com.example.datatoolserver.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * 图文格式
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class ArticleItem {
    //标题
    private String Title;
    //描述
    private String Description;
    //图片url
    private String PicUrl;
    //文章url
    private String Url;
}
