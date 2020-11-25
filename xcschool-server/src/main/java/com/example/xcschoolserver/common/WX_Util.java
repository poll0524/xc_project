package com.example.xcschoolserver.common;


import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Created by Administrator on 2018/9/18.
 * @author Administrator
 * @class WX_Util 类；
 */
public class WX_Util {
    //验证服务器地址
    public static String check_Url(HttpServletRequest request){
        //获取参数配置
        String signature = request.getParameter("signature");
        //获取时间托
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        //获取echostr 字符
        String echostr   = request.getParameter("echostr");
        //获取token 此token跟需跟微信公众号的token一致；
        String token = WeChatUtil.TOKEN;
        String str = "";
        // try ---- catch 捕捉异常
        try {
            //判断是否为空
            if (null != signature) {
                //声明一个存储数据字符数组
                String[] ArrTmp = { token, timestamp, nonce };
                Arrays.sort(ArrTmp);
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < ArrTmp.length; i++) {
                    sb.append(ArrTmp[i]);
                }
                //获取消息对象
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                //声明一个字节流数组；
                byte[] bytes = md.digest(new String(sb).getBytes());
                //声明一个字符流
                StringBuffer buf = new StringBuffer();
                for (int i = 0; i < bytes.length; i++) {
                    if (((int) bytes[i] & 0xff) < 0x10) {
                        buf.append("0");
                    }
                    buf.append(Long.toString((int) bytes[i] & 0xff, 16));
                }
                if (signature.equals(buf.toString())) {
                    str = echostr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回消息
        return str;
    }
}

