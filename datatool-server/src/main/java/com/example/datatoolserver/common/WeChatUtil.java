package com.example.datatoolserver.common;

import org.springframework.util.ClassUtils;

public class WeChatUtil {
    public static final String TOKEN = "pollfeng";

    public static final String AUTHOR = "来找客";

    //抖音爬取appid
    public static final String VID_APPID = "zmpbSjjN6k9OOlW1";
    public static final String VID_SECRET = "Tgm7umv2ZM7c3ttt";


    //测试号
//    public static final String APPID = "wx8ee656699cb2c84d";
//    public static final String APPSECRET = "c82093ea8fdffd9178cee6152c40cf90";

    //正式号
    public static final String APPID = "wx03cab7644b8bddb8";
    public static final String APPSECRET = "803dd8cc93a68f1c0db2e8121534003e";

    //创建菜单请求地址
    public static final String CREATE_MENU_URL = " https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    //调用基本access_token请求地址
    public static final String GET_ACCESSTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    //调用网页授权access_token请求地址
    public static final String GET_WEBACCESSTOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    //刷新网页授权access_token请求地址
    public static final String GET_REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
    //删除菜单请求地址
    public static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

    //发送模板请求地址
    public static final String SEND_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    //拉取用户信息请求地址
    public static final String GET_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    //页面使用jssdk的凭据
    public static final String GET_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    //获取图文素材的数量
    public static final String GET_ARTICLEQTL_URL = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token=ACCESS_TOKEN";

    //获取图文素材信息
    public static final String POST_ARTICLE_URL = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN";

    //新增图文素材
    public static final String POST_ADDARTICLE_URL = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=ACCESS_TOKEN";

    //获取素材信息
    public static final String POST_MATERIAL_URL = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=ACCESS_TOKEN";

    //添加客服
    public static final String POST_ADDSTAFF_URL = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token=ACCESS_TOKEN";

    //发送客服消息
    public static final String POST_STAFFTXT_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

    //发起微信支付
    public static final String POST_WX_PAY = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    //测试环境
    public static final String POST_WX_PAY_CE = "https://api.mch.weixin.qq.com/sandboxnew/pay/micropay";

    //沙箱获取秘钥
    public static final String POST_KEY_CE = "https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey";

    //微信转账地址
    public static final String POST_TRANSFERS = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";


    //调用抖音下载api接口
    public static final String DOUYIN = "https://api-sv.videoparse.cn/api/video/normalParse";

    //获取unionid
    public static final String get_UNIONTID = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    //基本access_token调用接口凭证
    public static final String ACCESSTOKEN = "AccessToken";
    //网页access_token调用接口凭证
    public static final String WEBACCESSTOKEN = "WebAccessToken";

    public static final String REFRESHTOKEN = "RefreshToken";

    public static final String KEY = "A1070ADDFB422AB8691F744B9909BCAD";

    public static final String MCH_ID = "1591773071";

    //证书路径
//    public static final String CERT_PATH = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"weixin/apiclient_cert.p12";
    public static final String CERT_PATH = "/weixin/apiclient_cert.p12";

    //oss参数
    public static final String ENDPOINT = "oss-accelerate.aliyuncs.com";
    public static final String KEYID = "LTAI4GJcedWzZ1XuCijsFhkL";
    public static final String KEYSECRET = "7XdOEyTjpMOpA58uOaGWQ55zIpuTCZ";
    public static final String BUCKETNAME = "xiaocisw-video";
}
