package com.example.collecttoolserver.service.Impl;

import com.example.collecttoolserver.common.ReturnVO;
import com.example.collecttoolserver.common.WeChatUtil;
import com.example.collecttoolserver.pojo.*;
import com.example.collecttoolserver.service.IContentService;
import com.example.collecttoolserver.util.AliyunOsskeyUtil;
import com.example.collecttoolserver.util.FileUtil;
import com.example.collecttoolserver.util.UrlAgingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.collecttoolserver.mapper.*;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.collecttoolserver.service.IDataCollectionService;


import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ContentServiceImpl implements IContentService {
    @Autowired
    private articleMapper articleMapper;
    @Autowired
    private videoMapper videoMapper;
    @Autowired
    private xcuserMapper xcuserMapper;
    @Autowired
    private IDataCollectionService dataCollectionService;
    @Autowired
    private industryMapper industryMapper;

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
        String token = getOrderId();
        //将文章写入数据库并返回文章id
        articleMapper.insertArticle(openId,title,digest,coverImg,originalUrl,author,collectTime,popCode,popImg,0,phone,token);
        Article article = articleMapper.selectArticleToken(token);
        //将文章内容写入本地文件
        //创建TXT文件写入内容
        FileUtil.createFile(article.getArticle_id().toString(),content);
        Map<String,Object> datas = new HashMap<>();
        datas.put("id",article.getArticle_id());
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
        //视频分类
        String industryName = (String) data.get("industryName");
        //采集时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String collectTime = df.format(new Date()); // new Date()为获取当前系统时间
        //获取原始链接
        String originalUrl = (String) data.get("originalUrl");

        String phone = (String) data.get("phone");

        String token = getOrderId();

        videoMapper.insertVideo(openId,title,digest,coverImg,author,downUrl,popImg,popCode,collectTime,originalUrl,0,0,phone,token,industryName);
        Video video = videoMapper.selectVideoToken(token);
        Map<String,Object> datas = new HashMap<>();
        datas.put("id",video.getVideo_id());
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
        //判断视频播放地址是否有效
        Integer code = UrlAgingUtil.isValid(video.getDown_url());




        //创建map封装数据
        Map<String,Object> datas = new HashMap<>();
        datas.put("id",video.getVideo_id());
        datas.put("openId",video.getOpen_id());
        String title = video.getTitle();
        if (title == null){
            datas.put("title",null);
        }else {
            datas.put("title",title);
        }
        String digest = video.getDigest();
        if (digest == null){
            datas.put("digest",null);
        }else {
            datas.put("digest",digest);
        }
        datas.put("author",video.getAuthor());
        datas.put("coverImg",video.getCover_img());
        if (code != 200){
            //重新获取播放地址
            Map<String,Object> a = new HashMap<>();
            a.put("spiderUrl",video.getOriginal_url());
            a.put("start","1");
            Map<String,Object> b = dataCollectionService.spiderContent(a);
            String downUrl = (String) b.get("videoUrl");
            videoMapper.updataDownUrl(videoId, downUrl);
            datas.put("downUrl",downUrl);
        }else {
            datas.put("downUrl",video.getDown_url());
        }
        datas.put("popImg",video.getPop_img());
        datas.put("popCode",video.getPop_code());
        datas.put("userHeadimgurl",xcuser.getUser_headimgurl());
        datas.put("userName",xcuser.getUser_name());
        datas.put("userPhone",video.getPhone());

        return datas;
    }

    /**
     * 根据openId查询视频列表
     */
    public List<Map<String,Object>> selectContentOpenId(Map<String,Object> data){
        String openId = (String) data.get("openId");
        Integer pag = (Integer) data.get("pag")*10;
        List<Video> videos = videoMapper.selectContentOpenId(openId,pag);
        List<Map<String,Object>> dataList = new ArrayList<>();
        for (Video video : videos) {
            Map<String,Object> datas = new HashMap<>();
            datas.put("id",video.getVideo_id());
            datas.put("title",video.getTitle());
            datas.put("coverImg",video.getCover_img());
            datas.put("collectTime",video.getCollect_time());
            dataList.add(datas);
        }
        return dataList;
    }

    public String deleteContent(Map<String,Object> data){
        Integer videoId = (Integer) data.get("id");
        Video video = videoMapper.selectVideoId(videoId);
        //删除封面图片
        if (video.getCover_img().indexOf("poster") != -1){
            String url = video.getDown_url();
            String[] a = url.split("/");
            File file = new File("/poster/userVideo/"+a[5]);
            file.delete();
        }
        //删除oss静态资源
        if (video.getDown_url().indexOf("xiaocisw-video.oss-accelerate.aliyuncs.com") != -1){
            List<String> a = new ArrayList<>();
            String str = "http://xiaocisw-video.oss-accelerate.aliyuncs.com/";

            String imgUrl = video.getDown_url().replaceAll(str,"");
            a.add(imgUrl);

            //将图片在oss中删除
            AliyunOsskeyUtil.deleteObjects(WeChatUtil.BUCKETNAME,a);
        }
        videoMapper.deleteVideo(videoId);
        return "删除成功";
    }

    public String updateContent(Map<String,Object> data){
        Integer videoId = (Integer) data.get("id");
        String title = (String) data.get("title");
        String digest = (String) data.get("digest");
        String coverImg = (String) data.get("coverImg");
        String downUrl = (String) data.get("downUrl");
        String popImg = (String) data.get("popImg");
        String phone = (String) data.get("phone");
        String industryName = (String) data.get("industryName");
        videoMapper.updataVideoId(videoId,title,digest,coverImg,downUrl,popImg,phone,industryName);
        return "更新成功";
    }

    /**
     * 查询视频分类
     */
    public List<Map<String,Object>> selectClassify(){
        List<Map<String,Object>> dataList = new ArrayList<>();
        List<Industry> industries = industryMapper.selectIndustry();
        for (Industry industry : industries) {
            Map<String,Object> datas = new HashMap<>();
            datas.put("industryId",industry.getIndustry_id());
            datas.put("industryName",industry.getIndustry_name());
            dataList.add(datas);
        }

        return dataList;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------

    public String getOrderId() {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar calendar = Calendar.getInstance();
        String dateName = df.format(calendar.getTime());

        Random ne=new Random();//实例化一个random的对象ne
        int x = ne.nextInt(999-100+1)+100;//为变量赋随机值100-999
        String random_order = String.valueOf(x);
        String orderId = dateName+random_order;
        return orderId;
    }


}
