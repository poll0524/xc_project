package com.example.datatoolserver.service.impl;

import com.example.datatoolserver.mapper.*;
import com.example.datatoolserver.pojo.*;
import com.example.datatoolserver.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchServiceIpml implements ISearchService {

    @Autowired
    private articleMapper articleMapper;

    @Autowired
    private brandNavMapper brandNavMapper;

    @Autowired
    private brandArticleMapper brandArticleMapper;

    @Autowired
    private videoMapper videoMapper;

    @Autowired
    private brandVideoMapper brandVideoMapper;

    @Autowired
    private billingImageMapper billingImageMapper;

    @Autowired
    private userClassifyMapper userClassifyMapper;

    @Autowired
    private articleClassifyMapper articleClassifyMapper;
    
    @Autowired
    private posterClassifyMapper posterClassifyMapper;

    @Autowired
    private posterMapper posterMapper;


    /**
     * 模糊查询
     * @param data
     * @return
     */
    public List<Map<String,Object>> searchList(Map<String,Object> data){
        String title = (String) data.get("title");
        Integer pag = (Integer) data.get("pag")*10;
        Integer start = (Integer) data.get("start");
        String openId = (String) data.get("openId");

        title = "%"+title+"%";
        List<Map<String,Object>> articleList = new ArrayList<>();
        //判断文章还是视频
        if(start == 0){
            //平台文章查询
            List<Article> articles = articleMapper.selectArticleTitle(title,pag);
            //品牌文章
            List<BrandArticle> brandArticles = brandArticleMapper.selectBrandArticleList(openId,title,pag);

            //整理数据
            if (brandArticles.size() == 0 && articles.size() == 0){
                return null;
            }else if(brandArticles.size() != 0 && articles.size() == 0){
                for (BrandArticle brandArticle : brandArticles) {
                    Map<String,Object> article = new HashMap<>();
                    article.put("id",brandArticle.getArticle_id());
                    article.put("title",brandArticle.getTitle());
                    article.put("coverImg",brandArticle.getCover_img());
                    BrandNav classify = brandNavMapper.selectBrandId(brandArticle.getBrand_id());
                    if (classify==null){
                        continue;
                    }
                    article.put("classify",classify.getBrand_name());
                    article.put("start",1);
                    articleList.add(article);
                }
            }else if (brandArticles.size() == 0 && articles.size() != 0){
                for (Article article1 : articles) {
                    Map<String,Object> article = new HashMap<>();
                    article.put("id",article1.getId());
                    article.put("title",article1.getTitle());
                    article.put("coverImg",article1.getCover_img());
                    Integer classifya = article1.getArticle_classify();
                    String a = articleClassifyMapper.selectIdArticle(classifya).getClassify_name();
                    article.put("classify",a);
                    article.put("start",0);
                    articleList.add(article);
                }
            }else if (brandArticles.size() != 0 && articles.size() != 0){
                for (Article article1 : articles) {
                    Map<String,Object> article = new HashMap<>();
                    article.put("id",article1.getId());
                    article.put("title",article1.getTitle());
                    article.put("coverImg",article1.getCover_img());
                    article.put("classify",articleClassifyMapper.selectIdArticle(article1.getArticle_classify()).getClassify_name());
                    article.put("start",0);
                    articleList.add(article);
                }
                for (BrandArticle brandArticle : brandArticles) {
                    Map<String,Object> article = new HashMap<>();
                    article.put("id",brandArticle.getArticle_id());
                    article.put("title",brandArticle.getTitle());
                    article.put("coverImg",brandArticle.getCover_img());
                    BrandNav classify = brandNavMapper.selectBrandId(brandArticle.getBrand_id());
                    if (classify == null){
                        continue;
                    }
                    article.put("classify",classify.getBrand_name());
                    article.put("start",1);
                    articleList.add(article);
                }
            }

        }else if (start == 1){
            //查询平台视频
            List<Video> videos = videoMapper.selectVideoTitleList(title,pag);

            //查询品牌视频
            List<BrandVideo> brandVideos = brandVideoMapper.selectBrandVideoList(openId,title,pag);

            if (videos.size() == 0 && brandVideos.size() == 0){
                return null;
            }else if (videos.size() != 0 && brandVideos.size() == 0){
                for (Video vi : videos) {
                    Map<String,Object> video = new HashMap<>();
                    video.put("id",vi.getVideo_id());
                    video.put("title",vi.getVideo_name());
                    video.put("coverImg",vi.getCover_url());
                    video.put("classify",articleClassifyMapper.selectIdArticle(vi.getVideo_classify()).getClassify_name());
                    video.put("start",0);
                    articleList.add(video);
                }
            }else if (videos.size() == 0 && brandVideos.size() != 0){
                for (BrandVideo brandVideo : brandVideos) {
                    Map<String,Object> video = new HashMap<>();
                    video.put("id",brandVideo.getVideo_id());
                    video.put("title",brandVideo.getTitle());
                    video.put("coverImg",brandVideo.getCover_img());
                    BrandNav classify = brandNavMapper.selectBrandId(brandVideo.getBrand_id());
                    if (classify == null){
                        continue;
                    }
                    video.put("classify",classify.getBrand_name());
                    video.put("start",1);
                    articleList.add(video);
                }
            }else if (videos.size() != 0 && brandVideos.size() != 0){
                for (Video vi : videos) {
                    Map<String,Object> video = new HashMap<>();
                    video.put("id",vi.getVideo_id());
                    video.put("title",vi.getVideo_name());
                    video.put("coverImg",vi.getCover_url());
                    video.put("classify",articleClassifyMapper.selectIdArticle(vi.getVideo_classify()).getClassify_name());
                    video.put("start",0);
                    articleList.add(video);
                }
                for (BrandVideo brandVideo : brandVideos) {
                    Map<String,Object> video = new HashMap<>();
                    video.put("id",brandVideo.getVideo_id());
                    video.put("title",brandVideo.getTitle());
                    video.put("coverImg",brandVideo.getCover_img());
                    BrandNav classify = brandNavMapper.selectBrandId(brandVideo.getBrand_id());
                    if (classify == null){
                        continue;
                    }
                    video.put("classify",classify.getBrand_name());
                    video.put("start",1);
                    articleList.add(video);
                }
            }


        }else {
            return null;
        }
        return articleList;
    }



    /**
     * 智能推送文章视频
     */
    public String push(String openId){
        //判断用户是否选择分类
        List<UserClassify> classify = userClassifyMapper.selectUserClassify(openId);

        String pushStr ="\uD83C\uDF04公众号将继续为您推荐文章,视频\uD83D\uDCAB\n";
        if (classify.size() == 0){
            //随机查询6个文章
            List<Article> articles = articleMapper.selectRaArticle();
            for (Article article : articles) {
                pushStr = pushStr+"\n"+"\uD83D\uDC49<a href='http://web.xiaocisw.site/articleInfo?openId="+openId+"&id="+article.getId()+"&start=0&sign=0'>"+article.getTitle()+"</a>"+"\n";
            }
            //随机查询2个视频
            List<Video> videos = videoMapper.selectRaVideo();

            for (Video video : videos) {
                pushStr = pushStr+"\n"+"\uD83C\uDFA5<a href='http://web.xiaocisw.site/viewInfo?openId="+openId+"&id="+video.getVideo_id()+"&start=0&sign=0'>"+video.getVideo_name()+"</a>"+"\n";
            }

        }else {
            //创建分类数组
            List<Integer> classifyId = new ArrayList<>();
            for (UserClassify userClassify : classify) {
                classifyId.add(userClassify.getClassify_id());
            }
            Random random = new Random();
            int n = random.nextInt(classifyId.size());
            Integer id = classifyId.get(n);
            List<Article> articles = articleMapper.selectClaRaArticle(id);
            if (articles.size() != 0){
                for (Article article : articles) {
                    pushStr = pushStr+"\n"+"\uD83D\uDC49<a href='http://web.xiaocisw.site/articleInfo?openId="+openId+"&id="+article.getId()+"&start=0&sign=0'>"+article.getTitle()+"</a>"+"\n";
                }
            }else {
                List<Article> articless = articleMapper.selectRaArticle();
                for (Article article : articless) {
                    pushStr = pushStr + "\n" + "\uD83D\uDC49a href='http://web.xiaocisw.site/articleInfo?openId=" + openId + "&id=" + article.getId() + "&start=0&sign=0'>" + article.getTitle() + "</a>" + "\n";
                }
            }

            //随机查询2个视频
            List<Video> videos = videoMapper.selectRaClVideo(id);
            if (videos.size() != 0){
                for (Video video : videos) {
                    pushStr = pushStr+"\n"+"\uD83C\uDFA5<a href='http://web.xiaocisw.site/viewInfo?openId="+openId+"&id="+video.getVideo_id()+"&start=0&sign=0'>"+video.getVideo_name()+"</a>"+"\n";
                }
            }else {
                List<Video> videoss = videoMapper.selectRaVideo();
                for (Video video : videoss) {
                    pushStr = pushStr + "\n" + "\uD83C\uDFA5<a href='http://web.xiaocisw.site/viewInfo?openId=" + openId + "&id=" + video.getVideo_id() + "&start=0&sign=0'>" + video.getVideo_name() + "</a>" + "\n";
                }
            }
        }

//        System.out.println(pushStr);

        return pushStr;
    }

    /**
     * 特定推送
     */
    public String pushParticularly(String openId) {
        //判断用户是否选择分类
        List<UserClassify> classify = userClassifyMapper.selectUserClassify(openId);

        String pushStr = "";
        if (classify.size() == 0) {
            //随机查询6个文章
            List<Article> articles = articleMapper.selectRaArticle();
            for (Article article : articles) {
                pushStr = pushStr + "\n" + "\uD83D\uDC49<a href='http://web.xiaocisw.site/articleInfo?openId=" + openId + "&id=" + article.getId() + "&start=0&sign=0'>" + article.getTitle() + "</a>" + "\n";
            }
            //随机查询2个视频
            List<Video> videos = videoMapper.selectRaVideo();

            for (Video video : videos) {
                pushStr = pushStr + "\n" + "\uD83C\uDFA5<a href='http://web.xiaocisw.site/viewInfo?openId=" + openId + "&id=" + video.getVideo_id() + "&start=0&sign=0'>" + video.getVideo_name() + "</a>" + "\n";
            }

        } else {
            //创建分类数组
            List<Integer> classifyId = new ArrayList<>();
            for (UserClassify userClassify : classify) {
                classifyId.add(userClassify.getClassify_id());
            }
            Random random = new Random();
            int n = random.nextInt(classifyId.size());
            Integer id = classifyId.get(n);
            List<Article> articles = articleMapper.selectClaRaArticle(id);
            if (articles.size() != 0) {
                for (Article article : articles) {
                    pushStr = pushStr + "\n" + "\uD83D\uDC49<a href='http://web.xiaocisw.site/articleInfo?openId=" + openId + "&id=" + article.getId() + "&start=0&sign=0'>" + article.getTitle() + "</a>" + "\n";
                }
            }else {
                List<Article> articless = articleMapper.selectRaArticle();
                for (Article article : articless) {
                    pushStr = pushStr + "\n" + "\uD83D\uDC49<a href='http://web.xiaocisw.site/articleInfo?openId=" + openId + "&id=" + article.getId() + "&start=0&sign=0'>" + article.getTitle() + "</a>" + "\n";
                }
            }

            //随机查询2个视频
            List<Video> videos = videoMapper.selectRaClVideo(id);
            if (videos.size() != 0) {
                for (Video video : videos) {
                    pushStr = pushStr + "\n" + "\uD83C\uDFA5<a href='http://web.xiaocisw.site/viewInfo?openId=" + openId + "&id=" + video.getVideo_id() + "&start=0&sign=0'>" + video.getVideo_name() + "</a>" + "\n";
                }
            }else {
                List<Video> videoss = videoMapper.selectRaVideo();
                for (Video video : videoss) {
                    pushStr = pushStr + "\n" + "\uD83C\uDFA5<a href='http://web.xiaocisw.site/viewInfo?openId=" + openId + "&id=" + video.getVideo_id() + "&start=0&sign=0'>" + video.getVideo_name() + "</a>" + "\n";
                }
            }
        }
        return pushStr;
    }

    /**
     * 分享有礼导航
     */
    public List<Map<String,Object>> selectShareNav(){
        //查询导航
        List<PosterClassify> posterClassifies = posterClassifyMapper.selectSharePoster();
        List<Map<String,Object>> posterList = new ArrayList<>();
        for (PosterClassify posterClassify : posterClassifies) {
            Map<String,Object> data = new HashMap<>();
            data.put("id",posterClassify.getClassify_id());
            data.put("classify",posterClassify.getPoster_classify_name());
            data.put("start",posterClassify.getStart());
            posterList.add(data);
        }
        return posterList;
    }


    /**
     * 查询分享有礼海报
     */
    public List<Map<String,Object>> selectPoster(Map<String,Object> data){
        Integer pag = Integer.parseInt((String) data.get("pag"))*20;
        Integer start =Integer.parseInt((String) data.get("start"));
        List<Map<String,Object>> posterList = new ArrayList<>();
        if (start == 2){//查询海报
            List<BillingImage> posters = billingImageMapper.selectBillingImage(pag);
            if (posters.size() == 0){
                return null;
            }
            for (BillingImage poster : posters) {
                Map<String,Object> datas = new HashMap<>();
                datas.put("id",poster.getImage_id());
                datas.put("title",poster.getImage_title());
                datas.put("posterUrl",poster.getImage_url());
                posterList.add(datas);
            }
        }else if (start == 0){//查询文章
            //根据id查询文章列表
            List<Article> articles = articleMapper.selectSpecialArticle(pag,0);
            if (articles.size() == 0){
                return null;
            }
            for (Article poster : articles) {
                Map<String,Object> datas = new HashMap<>();
                datas.put("id",poster.getId());
                datas.put("title",poster.getTitle());
                datas.put("coverImg",poster.getCover_img());
                datas.put("quantity",poster.getQuantity());
                posterList.add(datas);
            }
        }else if (start == 1){
            List<Video> videos = videoMapper.selectSpecialVideo(pag);
            if (videos.size() == 0){
                return null;
            }
            for (Video poster : videos) {
                Map<String,Object> datas = new HashMap<>();
                datas.put("id",poster.getVideo_id());
                datas.put("title",poster.getVideo_name());
                datas.put("coverImg",poster.getCover_url());
                datas.put("quantity",poster.getQuantity());
                posterList.add(datas);
            }
        }
        return posterList;
    }

    /**
     * 帮助中心列表
     */
    public List<Map<String,Object>> selectHelp(Map<String,Object> data){
        //获取分类:1为使用须知,2为管理后台,3功能使用,4为赚取收益
        Integer start = Integer.parseInt((String) data.get("start"));
        //获取页码
        Integer pag = Integer.parseInt((String) data.get("pag"))*20;

        //根据id查询文章列表
        List<Article> articles = articleMapper.selectSpecialArticle(pag,start);
        if (articles.size() == 0){
            return null;
        }
        List<Map<String,Object>> dataList = new ArrayList<>();
        for (Article article : articles) {
            Map<String,Object> datas = new HashMap<>();
            datas.put("id",article.getId());
            datas.put("title",article.getTitle());
            datas.put("url",article.getUrl());
            datas.put("icon",article.getContent_img());
            dataList.add(datas);
        }
        return dataList;
    }
}
