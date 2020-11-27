package com.example.collecttoolserver.service.Impl;

import com.example.collecttoolserver.common.ReturnVO;
import com.example.collecttoolserver.common.WeChatUtil;
import com.example.collecttoolserver.pojo.Article;
import com.example.collecttoolserver.pojo.Video;
import com.example.collecttoolserver.pojo.Xcuser;
import com.example.collecttoolserver.service.IContentService;
import com.example.collecttoolserver.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.collecttoolserver.mapper.*;
import org.springframework.web.bind.annotation.ResponseBody;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ContentServiceImpl implements IContentService {
    @Autowired
    private articleMapper articleMapper;
    @Autowired
    private videoMapper videoMapper;
    @Autowired
    private xcuserMapper xcuserMapper;
    /**
     * 写入编辑后的内容
     */
    public Map<String,Object> insertContent(Map<String,Object> data){
        //获取start:0为文章,1为视频
        Integer start = Integer.parseInt((String) data.get("start"));
        if (start == 0){
            return insertArticle(data);
        }else if (start == 1){
            return insertVideo(data);
        }
        return null;
    }

    /**
     * 写入文章
     */
    public Map<String,Object> insertArticle(Map<String,Object> data){
        //获取用户openId
        String openId = (String) data.get("openId");
        //获取文章标题
        String title = (String) data.get("title");
        //获取文章简介
        String digest = (String) data.get("digest");
        //获取文章封面图片
        String coverImg = (String) data.get("coverImg");
        //获取原始采集链接
        String originalUrl = (String) data.get("originalUrl");
        //获取文章作者
        String author = (String) data.get("author");
        //获取文章内容
        String content = (String) data.get("content");
        //采集时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String collectTime = df.format(new Date());// new Date()为获取当前系统时间
        //获取微信二维码
        String popCode = (String) data.get("popCode");
        //获取广告图片
        String popImg = (String) data.get("popImg");
        //获取电话号码
        String phone = (String) data.get("phone");
        //将文章写入数据库并返回文章id
        Integer articleId = articleMapper.insertArticle(openId,title,digest,coverImg,originalUrl,author,collectTime,popCode,popImg,0,phone);
        //将文章内容写入本地文件
        //创建TXT文件写入内容
        FileUtil.createFile(articleId.toString(),content);
        Map<String,Object> datas = new HashMap<>();
        datas.put("id",articleId);
        datas.put("start","0");

        return datas;
    }

    /**
     * 写入视频
     */
    public Map<String,Object> insertVideo(Map<String,Object> data){
        //获取用户openId
        String openId = (String) data.get("openId");
        //获取视频标题
        String title = (String) data.get("title");
        //获取视频简介
        String digest = (String) data.get("digest");
        //获取封面图片
        String coverImg = (String) data.get("coverImg");
        //获取作者
        String author = (String) data.get("author");
        //视频播放地址
        String downUrl = (String) data.get("downUrl");
        //广告图片
        String popImg = (String) data.get("popImg");
        //二维码图片
        String popCode = (String) data.get("popCode");
        //采集时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String collectTime = df.format(new Date()); // new Date()为获取当前系统时间
        //获取原始链接
        String originalUrl = (String) data.get("originalUrl");

        String phone = (String) data.get("phone");

        Integer videoId = videoMapper.insertVideo(openId,title,digest,coverImg,author,downUrl,popImg,popCode,collectTime,originalUrl,0,0,phone);
        Map<String,Object> datas = new HashMap<>();
        datas.put("id",videoId);
        datas.put("start","1");

        return datas;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 根据素材id查询标题内容
     */
    public Map<String,Object> selectContentTitle(Map<String,Object> data){
        //获取start区分文章和视频
        Integer start = Integer.parseInt((String) data.get("start"));
        Integer id = (Integer) data.get("id");
        if (start == 0){
            return selectArticle(id);
        }else if (start == 1){
            return selectVideo(id);
        }
        return null;
    }

    /**
     * 查询文章标题
     */
    public Map<String,Object> selectArticle(Integer articleId){
        //根据id查询
        Article article = articleMapper.selectArticleId(articleId);
        //创建map封装数据
        Map<String,Object> datas = new HashMap<>();
        datas.put("id",article.getArticle_id());
        datas.put("openId",article.getOpen_id());
        datas.put("title",article.getTitle());
        datas.put("author",article.getAuthor());
        datas.put("digest",article.getDigest());
        datas.put("coverImg",article.getCover_img());

        return datas;
    }

    /**
     * 根据id查询视频
     */
    public Map<String,Object> selectVideo(Integer videoId){
        Video video = videoMapper.selectVideoId(videoId);
        //创建map封装数据
        Map<String,Object> datas = new HashMap<>();
        datas.put("id",video.getVideo_id());
        datas.put("openId",video.getOpen_id());
        datas.put("title",video.getTitle());
        datas.put("author",video.getAuthor());
        datas.put("digest",video.getDigest());
        datas.put("coverImg",video.getCover_img());

        return datas;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 根据id查询素材详情
     */
    public Map<String,Object> selectContentInfo(Map<String,Object> data){
        //获取start:0为文章,1为视频
        Integer start = Integer.parseInt((String) data.get("start"));
        Integer id = (Integer) data.get("id");
        if (start == 0){
            return selectArticleInfo(id);
        }else if (start == 1){
            return selectVideoInfo(id);
        }
        return null;
    }

    public Map<String,Object> selectArticleInfo(Integer articleId){
        //根据id查询
        Article article = articleMapper.selectArticleId(articleId);

        //读取id对应的本地文件内容
        //创建路径
        String filePath = WeChatUtil.CONTENT_PATH+article.getArticle_id()+".txt";
        String content = FileUtil.readTxt(filePath);

        //查询用户信息
        Xcuser xcuser = xcuserMapper.selectOpenId(article.getOpen_id());

        //创建map封装数据
        Map<String,Object> datas = new HashMap<>();
        datas.put("id",article.getArticle_id());
        datas.put("openId",article.getOpen_id());
        datas.put("title",article.getTitle());
        datas.put("author",article.getAuthor());
        datas.put("digest",article.getDigest());
        datas.put("coverImg",article.getCover_img());
        datas.put("content",content);
        datas.put("popImg",article.getPop_img());
        datas.put("popCode",article.getPop_code());
        datas.put("userHeadimgurl",xcuser.getUser_headimgurl());
        datas.put("userName",xcuser.getUser_name());
        datas.put("userPhone",article.getPhone());

        return datas;
    }


    /**
     * 根据id查询视频
     */
    public Map<String,Object> selectVideoInfo(Integer videoId){
        Video video = videoMapper.selectVideoId(videoId);

        //查询用户信息
        Xcuser xcuser = xcuserMapper.selectOpenId(video.getOpen_id());

        //创建map封装数据
        Map<String,Object> datas = new HashMap<>();
        datas.put("id",video.getVideo_id());
        datas.put("openId",video.getOpen_id());
        datas.put("title",video.getTitle());
        datas.put("author",video.getAuthor());
        datas.put("digest",video.getDigest());
        datas.put("coverImg",video.getCover_img());
        datas.put("downUrl",video.getDown_url());
        datas.put("popImg",video.getPop_img());
        datas.put("popCode",video.getPop_code());
        datas.put("userHeadimgurl",xcuser.getUser_headimgurl());
        datas.put("userName",xcuser.getUser_name());
        datas.put("userPhone",video.getPhone());

        return datas;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------

}
