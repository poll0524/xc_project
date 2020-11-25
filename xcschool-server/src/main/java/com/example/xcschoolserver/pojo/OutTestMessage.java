package com.example.xcschoolserver.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@Getter
@Setter
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class OutTestMessage {
    //用户openid
    private String ToUserName;
    //测试号
    private String FromUserName;
    //消息创建时间 （整型）
    private Long CreateTime;
    //消息类型，文本为text
    private String MsgType;
    //文本消息内容
    private String Content;

    //	图片消息媒体id，可以调用获取临时素材接口拉取数据。
    @XmlElementWrapper(name = "Image")
    private String[] MediaId;


    /**
     * 图文消息
      */
    //图文消息的个数
    private Integer ArticleCount;

    //图文列表容器
    @XmlElementWrapper(name = "Articles")
    private ArticleItem[] item;


}

