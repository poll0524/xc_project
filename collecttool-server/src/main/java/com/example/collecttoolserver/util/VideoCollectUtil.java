package com.example.collecttoolserver.util;

import com.example.collecttoolserver.common.HttpUtil;
import com.example.collecttoolserver.common.Patterns;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class VideoCollectUtil {
    public static Map<String,Object> getVideo(String url){
        //创建map用于返回数据
        HashMap<String, Object> map = new HashMap<>();
        try {
            //清洗链接
            Matcher matcher = Patterns.WEB_URL.matcher(url);

            String urls = "";

            if (matcher.find()){
                urls = matcher.group();
            }

            return douyinSpider(urls);
        } catch (Exception e) {
            e.printStackTrace();
            return map;
        }
    }

    /**
     * 获取抖音视频时效链接
     */
    public static Map douyinSpider(String url) throws IOException, JSONException {
        //发起爬取请求
        String content = HttpUtil.postFrom(url);
        //将爬取内容转换为json格式
        JSONObject jsonContent = new JSONObject(content);
        //爬取类型
        String type = jsonContent.getString("source");
        //获取爬取标题
        String title = jsonContent.getString("title");
        //获取爬取封面图片
        String coverImg = jsonContent.getString("cover_url");
        //视频时效链接
        String videoUrl =  jsonContent.getString("video_url");
        //整理返回路径
        Map<String,Object> data = new HashMap<>();
        data.put("type",type);
        data.put("title",title);
        data.put("coverImg",coverImg);
        data.put("videoUrl",videoUrl);
        return data;
    }
}
