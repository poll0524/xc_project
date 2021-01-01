package com.example.collecttoolserver.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlAgingUtil {
    public static Integer isValid(String strLink) {
        URL realUrl = null;
        try {
            realUrl = new URL(strLink);
        } catch (MalformedURLException e) {
            System.out.println("构造URL发生错误.");
            return 40000;
        }
        // 打开和URL之间的连接
        HttpURLConnection connection;
        int code = 0;
        try {
            connection = (HttpURLConnection) realUrl.openConnection();
            connection.connect();
            code = connection.getResponseCode();
        } catch (IOException e) {
            System.out.println("网络访问异常,可能是URL异常.");
        }

        return code;
    }

    public static void main(String[] args) {
        URL realUrl = null;
        try {
            realUrl = new URL("http://xiaocisw-video.oss-accelerate.aliyuncs.com/collecttool/pop-image/1606731421681.jpg");
        } catch (MalformedURLException e) {
            System.out.println("构造URL发生错误.");
        }
        // 打开和URL之间的连接
        HttpURLConnection connection;
        int code = 0;
        try {
            connection = (HttpURLConnection) realUrl.openConnection();
            connection.connect();
            code = connection.getResponseCode();
        } catch (IOException e) {
            System.out.println("网络访问异常,可能是URL异常.");
        }

        if(code == 200){
            System.out.println("URL可以正常访问.");
        }else{
            System.out.println("URL异常.HTTP STATUS CODE="+code);
        }
    }
}
