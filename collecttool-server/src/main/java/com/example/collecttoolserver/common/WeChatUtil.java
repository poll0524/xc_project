package com.example.collecttoolserver.common;

public class WeChatUtil {
    //小程序
    public static final String APPID = "wxabeccfbc3bf8f54d";
    public static final String APPSECRET = "3fb85621871eb9f9eb0e501ba30de362";

    //抖音爬取appid
    public static final String VID_APPID = "zmpbSjjN6k9OOlW1";
    public static final String VID_SECRET = "Tgm7umv2ZM7c3ttt";

    //本地写入文本内容地址
    public static final String CONTENT_PATH ="E:/content/";

    //证书路径
//    public static final String CERT_PATH = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"weixin/apiclient_cert.p12";
    public static final String CERT_PATH = "/weixin/apiclient_cert.p12";

    public static final String KEY = "A1070ADDFB422AB8691F744B9909BCAD";
    //微信商户号
    public static final String MCH_ID = "1591773071";

    //用户授权地址
    public static final String GET_OPENID = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";

    //调用抖音下载api接口
    public static final String DOUYIN = "https://api-sv.videoparse.cn/api/video/normalParse";
}
