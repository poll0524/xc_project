package com.example.collecttoolserver.service.Impl;

import com.example.collecttoolserver.common.ReturnVO;
import com.example.collecttoolserver.service.IDataCollectionService;
import com.example.collecttoolserver.util.ArticleCollectUtil;
import com.example.collecttoolserver.util.VideoCollectUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URL;
import java.util.Map;

@Service
public class DataCollectionServiceImpl implements IDataCollectionService {
    /**
     * 通过链接爬取数据
     */
    public Map<String,Object> spiderContent(Map<String,Object> data){
        //获取采集的链接
        String spiderUrl = (String) data.get("spiderUrl");
        //获取是文章还是视频
        Integer start = Integer.parseInt((String) data.get("start"));
        //判断是视频还是文章
        if (start == 0){
            if (spiderUrl.indexOf("https://mp.weixin.qq.com") != -1){
                //调用方法获取文章
                Map<String,Object> datas = ArticleCollectUtil.getArticle(spiderUrl);
                return datas;
            }
            return null;
        }else if (start == 1){
            if (spiderUrl.indexOf("抖音") != -1 || spiderUrl.indexOf("douyin") != -1){
                Map<String,Object> datas = VideoCollectUtil.getVideo(spiderUrl);
                return datas;
            }
            return null;
        }
        return null;
    }
}
