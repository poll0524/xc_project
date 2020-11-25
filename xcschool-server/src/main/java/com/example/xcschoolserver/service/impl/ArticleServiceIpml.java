package com.example.xcschoolserver.service.impl;

import com.example.xcschoolserver.common.Patterns;
import com.example.xcschoolserver.common.WeChatUtil;
import com.example.xcschoolserver.mapper.*;
import com.example.xcschoolserver.pojo.*;
import com.example.xcschoolserver.service.IArticleService;
import com.example.xcschoolserver.util.AccessTokenUtil;
import com.example.xcschoolserver.util.CollectionUtil;
import com.example.xcschoolserver.util.CrawlerUtil;
import com.example.xcschoolserver.util.SpiderUtil;
import com.nimbusds.jose.JOSEException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;

@Service
public class ArticleServiceIpml implements IArticleService {
    @Autowired
    private AccessTokenUtil accessTokenUtil;

    @Autowired
    private articleMapper articleMapper;

    @Autowired
    private articleClassifyMapper articleClassifyMapper;

    @Autowired
    private userClassifyMapper userClassifyMapper;

    @Autowired
    private brandNavMapper brandNavMapper;

    @Autowired
    private videoMapper videoMapper;

    @Autowired
    private xcuserMapper xcuserMapper;

    @Autowired
    private shareVideoMapper shareVideoMapper;

    @Autowired
    private shareArticleMapper shareArticleMapper;

    @Autowired
    private brandVideoMapper brandVideoMapper;

    @Autowired
    private shareUserMapper shareUserMapper;

    @Autowired
    private brandArticleMapper brandArticleMapper;

    @Autowired
    private materialMapper materialMapper;

    @Autowired
    private productMapper productMapper;

    @Autowired
    private productImgMapper productImgMapper;

    @Autowired
    private brandClassifyMapper brandClassifyMapper;

    @Autowired
    private courseMapper courseMapper;

    @Autowired
    private billingBrandClassifyMapper billingBrandClassifyMapper;

    @Autowired
    private brandCourseMapper brandCourseMapper;

    @Autowired
    private userBrandMapper userBrandMapper;



    //    /**
//     * 查询文章所有标题及内容
//     */
//    @Override
    public String selectArticlelist() throws JSONException {
        //定义图文素材的实体类
//        List<Article> lists = new ArrayList<>();
        JSONObject materialcount = accessTokenUtil.getArticleCount();
        //语音总数
        String voice_count = materialcount.getString("voice_count");
        int voiceCount = Integer.parseInt(voice_count);
        //视频总数
        String video_count = materialcount.getString("video_count");
        int videoCount = Integer.parseInt(video_count);
        //图片总数
        String image_count = materialcount.getString("image_count");
        int imageCount = Integer.parseInt(image_count);
        //图文总数
        String news_count = materialcount.getString("news_count");
        int newsCount = Integer.parseInt(news_count);

        //创建map存放分类和数量
        Map<String, Integer> counts = new HashMap<>();
        counts.put("voice", voiceCount);
        counts.put("video", videoCount);
        counts.put("image", imageCount);
        counts.put("news", newsCount);

        for (Map.Entry<String, Integer> count : counts.entrySet()) {

            if (count.getKey() == "news") {
                if (count.getValue() != 0 && count.getValue() < 20) {
                    JSONObject articles = accessTokenUtil.postArticleList("news", 0, 20);
                    try {
                        JSONArray jsonArray = articles.getJSONArray("item");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = (JSONObject) jsonArray.get(i);
                            json = json.getJSONObject("content");
                            JSONArray arr = json.getJSONArray("news_item");
                            json = (JSONObject) arr.get(0);
                            Article article = new Article();
                            String title = json.getString("title");
                            String author = json.getString("author");
                            String digest = json.getString("digest");
                            String thumb_media_id = json.getString("thumb_media_id");
                            String url = json.getString("url");
                            String content = json.getString("content");
                            Article selectArt = articleMapper.selectMediaIdArticle(thumb_media_id);
                            if (selectArt == null) {
                                articleMapper.insertArticle(title, thumb_media_id, author, digest, content, url, 1, 1,3000);
                            }
                        }
                    } catch (JSONException e) {
                        String accessToken = null;
                    }
                } else if (count.getValue() > 20) {
                    int o = count.getValue() / 20;
                    int a = 0;
                    for (int c = 0; c <= o; c++) {
                        JSONObject articles = accessTokenUtil.postArticleList("news", a, 20);
                        a += 20;
                        try {
                            JSONArray jsonArray = articles.getJSONArray("item");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = (JSONObject) jsonArray.get(i);
                                json = json.getJSONObject("content");
                                JSONArray arr = json.getJSONArray("news_item");
                                json = (JSONObject) arr.get(0);
                                Article article = new Article();
                                String title = json.getString("title");
                                String author = json.getString("author");
                                String digest = json.getString("digest");
                                String thumb_media_id = json.getString("thumb_media_id");
                                String url = json.getString("url");
                                String content = json.getString("content");
                                Article selectArt = articleMapper.selectMediaIdArticle(thumb_media_id);
                                if (selectArt == null) {
                                    articleMapper.insertArticle(title, thumb_media_id, author, digest, content, url, 1, 1,3000);
                                }
                            }
                        } catch (JSONException e) {
                            String accessToken = null;
                        }
                    }

                }


            } else if (count.getKey() == "image") {
                if (count.getValue() != 0 && count.getValue() < 20) {
                    JSONObject articles = accessTokenUtil.postArticleList(count.getKey(), 0, 20);
                    try {
                        JSONArray jsonArray = articles.getJSONArray("item");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = (JSONObject) jsonArray.get(i);
                            String name = json.getString("name");

                            if (articleMapper.selectMediaIdArticle(json.getString("media_id")) != null) {
                                articleMapper.updataMediaIdImg(json.getString("media_id"), json.getString("url"));
                            }

                        }
                    } catch (JSONException e) {
                        String accessToken = null;
                    }
                } else if (count.getValue() > 20) {
                    int o = count.getValue() / 20;
                    int a = 0;
                    for (int c = 0; c <= o; c++) {
                        JSONObject articles = accessTokenUtil.postArticleList("image", a, 20);
                        a += 20;
                        try {
                            JSONArray jsonArray = articles.getJSONArray("item");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = (JSONObject) jsonArray.get(i);
                                String name = json.getString("name");

                                if (articleMapper.selectMediaIdArticle(json.getString("media_id")) != null) {
                                    articleMapper.updataMediaIdImg(json.getString("media_id"), json.getString("url"));
                                }

                            }
                        } catch (JSONException e) {
                            String accessToken = null;
                        }
                    }
                }
            }
//            else if(count.getKey() == "video"){
//                if(count.getValue() != 0 && count.getValue()<20){
//                    JSONObject articles = accessTokenUtil.postArticleList(count.getKey(),0,20);
//                    try {
//                        JSONArray jsonArray = articles.getJSONArray("item");
//
//                        for (int i = 0; i < jsonArray.length(); i++){
//                            JSONObject json =(JSONObject) jsonArray.get(i);
//                            String mediaId = json.getString("media_id");
//                            String videoName = json.getString("name");
//                            String coverUrl = json.getString("cover_url");
//                            if(videoMapper.selectMediaIdVideo(mediaId) == null){
//                                // 将视频列表信息写入数据库
//                                videoMapper.insertVideo(mediaId,videoName,coverUrl,1);
//                                // 根据唯一id向微信服务器拉取视频详情
//                                JSONObject videoInfo = accessTokenUtil.postMaterial(mediaId);
//                                //根据唯一id将视频播放地址存入数据库
//                                videoMapper.updataVideo(mediaId,videoInfo.getString("down_url"));
//                            }
//
//                        }
//                    }catch (JSONException e){
//                        String accessToken = null;
//                    }
//                }else if(count.getValue()>20){
//                    int o = count.getValue()/20;
//                    int a = 0;
//                    for (int c = 0 ; c<=o;c++){
//                        JSONObject articles = accessTokenUtil.postArticleList(count.getKey(),a,20);
//                        try {
//                            JSONArray jsonArray = articles.getJSONArray("item");
//
//                            for (int i = 0; i < jsonArray.length(); i++){
//                                JSONObject json =(JSONObject) jsonArray.get(i);
//                                String mediaId = json.getString("media_id");
//                                String videoName = json.getString("name");
//                                String coverUrl = json.getString("cover_url");
//                                if(videoMapper.selectMediaIdVideo(mediaId) == null){
//                                    // 将视频列表信息写入数据库
//                                    videoMapper.insertVideo(mediaId,videoName,coverUrl,1);
//                                    // 根据唯一id向微信服务器拉取视频详情
//                                    JSONObject videoInfo = accessTokenUtil.postMaterial(mediaId);
//                                    //根据唯一id将视频播放地址存入数据库
//                                    videoMapper.updataVideo(mediaId,videoInfo.getString("down_url"));
//                                }
//
//                            }
//                        }catch (JSONException e){
//                            String accessToken = null;
//                        }
//                    }
//                }
//            }
        }

        return "数据拉取成功";

    }

    /**
     * 根据media_id,openId查询文章和用户信息
     */
    public Map<String, Object> articleInfo(Map<String, Object> articleInfo) {

        Integer id = (Integer) articleInfo.get("id");
        String openId = (String) articleInfo.get("openId");
        Integer start = (Integer) articleInfo.get("start");
        Integer sign = (Integer) articleInfo.get("sign");
        Map<String, Object> obj = new HashMap<>();
        Xcuser user = xcuserMapper.selectOpenId(openId);
        if (user.getCode_url() == null) {
            user.setCode_url("");
        }
        if (user.getUser_phone() == null) {
            user.setUser_phone("");
        }

        if (sign == null){
            sign = 0;
        }

        if (start == 0){
            if (sign == 0 || sign == null){
                Article article = articleMapper.selectArticleId(id);
                BrandNav brandNav = brandNavMapper.selectBrandName(openId, WeChatUtil.AUTHOR);
                if (brandNav == null){
                    brandNavMapper.insertBrand(openId,WeChatUtil.AUTHOR,1);
                    brandNav = brandNavMapper.selectBrandName(openId, WeChatUtil.AUTHOR);
                }
                //阅读量+1
                articleMapper.updataQuantity(id);
                obj.put("userInfo", user);
                obj.put("article", article);
                obj.put("classify", articleClassifyMapper.selectIdArticle(article.getArticle_classify()).getClassify_name());
                obj.put("brandName",brandNav.getBrand_name());
                obj.put("second","");
                obj.put("brandId",brandNav.getBrand_id());
                List a = new ArrayList();
                obj.put("product",a);
            }else if (sign == 1){
                BrandArticle brandArticle = brandArticleMapper.selectArticleId(id);
                BrandNav brandNav = brandNavMapper.selectBrandId(brandArticle.getBrand_id());
                List<Material> materials = materialMapper.selectMaterialArticle(id);
                if (materials.size() != 0){
                    //创建空list存放产品
                    List<Map<String,Object>> product = new ArrayList<>();
                    for (Material material : materials) {
                        Map<String,Object> prod = new HashMap<>();
                        Product pro = productMapper.selectProductInfo(material.getProduct_id());
                        prod.put("productTitle",pro.getProduct_title());
                        prod.put("productId",pro.getProduct_id());
                        prod.put("url",pro.getUrl());
                        prod.put("vipPrice",pro.getVip_price());
                        prod.put("price",pro.getPrice());
                        prod.put("coverImg",productImgMapper.selectProductImg(pro.getOpen_id(),pro.getProduct_id()).getCover_url());
                        product.add(prod);
                    }
                    obj.put("product",product);
                }else {
                    List a = new ArrayList();
                    obj.put("product",a);
                }

                obj.put("userInfo",user);
                obj.put("article",brandArticle);
                obj.put("classify",brandNav.getBrand_name());
                obj.put("brandName",brandNav.getBrand_name());
                obj.put("brandId",brandArticle.getBrand_id());
                obj.put("second",brandArticle.getClassify());
            }
        } else if (start == 1) {
            ShareArticle article = shareArticleMapper.selectShareArtLists(openId, id);
            if (article == null) {
                return null;
            }
            obj.put("article", article);
            obj.put("classify", articleClassifyMapper.selectIdArticle(article.getArticle_classify()).getClassify_name());
            obj.put("userInfo", user);
        }

        return obj;
    }

    /**
     * 修改或分享文章上传
     */
    public String alterArticle(Map<String, Object> article) throws JOSEException {
        String openId = (String) article.get("openId");
        String thumbMediaId = (String) article.get("thumbMediaId");
        String coverImg = (String) article.get("coverImg");
        String title = (String) article.get("title");
        String content = (String) article.get("content");
        String author = (String) article.get("author");
        String digest = (String) article.get("digest");
        String url = (String) article.get("url");
        Integer showCoverPic = (Integer) article.get("showCoverPic");
        Integer articleClassify = (Integer) article.get("articleClassify");
        Integer id = (Integer) article.get("id");
        Integer sign = (Integer) article.get("sign");
        List<Integer> product = (List<Integer>) article.get("product");

        String advertising1 = "";

        if (product.size() == 0){
            advertising1 = "0";
        }else if(product.size() == 1){
            advertising1 = product.get(0).toString();
        }else if (product.size() == 2){
            advertising1 = product.get(0).toString()+","+product.get(1).toString();
        }else if (product.size() == 3){
            advertising1 = product.get(0).toString()+","+product.get(1).toString()+","+product.get(2).toString();
        }else if (product.size() == 4){
            advertising1 = product.get(0).toString()+","+product.get(1).toString()+","+product.get(2).toString()+","+product.get(3).toString();
        }




        if (thumbMediaId == null){
            //生成素材唯一值
            Map<String,Object> maps = new HashMap<>();
            //建立载荷,这些数据根据业务,自己定义
            maps.put("openId",openId);
            maps.put("groupName",title);
            //生成时间
            maps.put("sta", new Date().getTime());
            //过期时间
            maps.put("exp", new Date().getTime() + 6);
            thumbMediaId = com.example.xcschoolserver.util.token.creatToken(maps);
        }

        if (sign == 0){
            ShareArticle shareArticle = shareArticleMapper.selectShareArtList(openId,id);
            if (shareArticle != null){
                shareArticleMapper.updataShareArticle(openId,title,thumbMediaId,author,digest,content,url,coverImg,showCoverPic,articleClassify,advertising1,0,id);
                return "分享成功";
            }
            shareArticleMapper.insertShareArticle(openId, title, thumbMediaId, author, digest, content, url, coverImg, showCoverPic, articleClassify,advertising1,null,id);
        }else if (sign == 1){
            ShareArticle shareArticle = shareArticleMapper.selectShareBraList(openId,id);
            if (shareArticle != null){
                shareArticleMapper.updataShareArticleB(openId,title,thumbMediaId,author,digest,content,url,coverImg,showCoverPic,articleClassify,advertising1,id,0);
                return "分享成功";
            }
            shareArticleMapper.insertShareArticle(openId, title, thumbMediaId, author, digest, content, url, coverImg, showCoverPic, articleClassify,advertising1,id,null);
        }
        return "分享成功";

    }

    /**
     * 按照品牌查询文章
     *
     * @param video
     * @return
     */
    public List<Map<String, Object>> selectArticleName(Map<String, Object> video) {
        //获取openid
        String openId = (String) video.get("openId");

        //获取页数
        Integer pag = (Integer) video.get("pag") * 20;
        if (brandNavMapper.selectBrand(openId).size() >= 1) {
            List<ShareArticle> shareArticles = shareArticleMapper.selectShareArtOpenIdList(openId, pag);
            if (shareArticles.size() == 0) {
                return null;
            }
            List<Map<String, Object>> datas = new ArrayList<>();
            for (ShareArticle shareArticle : shareArticles) {
                Map<String, Object> data = new HashMap<>();
                data.put("share_id", shareArticle.getShare_id());
                data.put("title", shareArticle.getTitle());
                data.put("cover_img", shareArticle.getCover_img());
                datas.add(data);
            }
            return datas;
        }

        return null;
    }


    /**
     * 查询团队文章
     *
     * @param group
     * @return
     */
    public Map<String,Object> GroupList(Map<String, Object> group) {
        String openId = (String) group.get("openId");
        Integer start = (Integer) group.get("start");
        Integer pag = (Integer) group.get("pag")*20;
//        Integer pag = (Integer) group.get("pag") * 5;


        List dataInfo = new ArrayList();

        if (start == 0) {

            //查询分享前20篇文章
            List<ShareUser> shareUsers = shareUserMapper.selectShareArticle(openId,pag);
            Integer a = 0;
            for (ShareUser shareUser : shareUsers) {
                Map<String,Object> article = new HashMap<>();
                ShareArticle shareArticle = new ShareArticle();
                if (shareUser.getTell() == 0){
                    shareArticle = shareArticleMapper.selectShareVids(openId,shareUser.getArticle_id());
                    if(shareArticle == null){
                        continue;
                    }
                    article.put("sign",0);
                    article.put("quantity",articleMapper.selectArticleId(shareUser.getArticle_id()).getQuantity());
                }else if (shareUser.getTell() == 1){
                    shareArticle = shareArticleMapper.selectShareBids(openId,shareUser.getArticle_id());
                    if(shareArticle == null){
                        continue;
                    }
                    article.put("sign",1);
                    article.put("quantity",brandArticleMapper.selectArticleId(shareUser.getArticle_id()).getQuantity());
                }

                article.put("id",shareUser.getArticle_id());
                article.put("title",shareArticle.getTitle());
                article.put("coverImg",shareArticle.getCover_img());
                article.put("time",shareUser.getRead_time_begin());
                article.put("deg",shareUser.getDeg());
                article.put("desc_id",a);
                dataInfo.add(article);
                a = a +1;
            }



        } else if (start == 1) {

            List<ShareUser> shareUsers = shareUserMapper.selectShareVideo(openId,pag);
            Integer a = 0;
            for (ShareUser shareUser : shareUsers) {
                if (shareUser.getVideo_id() != null){
                    Map<String,Object> article = new HashMap<>();
                    ShareVideo shareVideo = new ShareVideo();

                    if (shareUser.getTell() == 0){
                        shareVideo = shareVideoMapper.selectShareVids(openId,shareUser.getVideo_id());
                        if(shareVideo == null){
                            continue;
                        }
                        article.put("sign",0);
                        article.put("quantity",videoMapper.selectVideoId(shareUser.getVideo_id()).getQuantity());
                    }else if (shareUser.getTell() == 1){
                        shareVideo = shareVideoMapper.selectShareBids(openId,shareUser.getVideo_id());
                        if(shareVideo == null){
                            continue;
                        }
                        article.put("sign",1);
                        article.put("quantity",brandVideoMapper.selectVideoIds(shareUser.getVideo_id()).getQuantity());
                    }
                    article.put("id",shareUser.getVideo_id());
                    article.put("title",shareVideo.getVideo_name());
                    article.put("coverImg",shareVideo.getCover_url());
                    article.put("time",shareUser.getRead_time_begin());
                    article.put("desc_id",a);
                    article.put("deg",shareUser.getDeg());
                    dataInfo.add(article);
                }
            }
        }else if (start == 2){
            //查询该用户分享的数据
            List<ShareUser> shareUsers = shareUserMapper.selectShareCourse(openId,pag);
            Integer a = 0;

            for (ShareUser shareUser : shareUsers) {
                if (shareUser.getCourse_id() != null){
                    Map<String,Object> article = new HashMap<>();


                    if (shareUser.getTell() == 0){
                        Course shareVideo = courseMapper.selectCourseId(shareUser.getCourse_id());
                        if(shareVideo == null){
                            continue;
                        }
                        article.put("sign",0);
                        article.put("id",shareUser.getCourse_id());
                        article.put("title",shareVideo.getTitle());
                        article.put("coverImg",shareVideo.getCover_img());
                        article.put("price",shareVideo.getPrice());
                        article.put("quantity",shareVideo.getQuantity());
                        article.put("time",shareUser.getRead_time_begin());
                        article.put("deg",shareUser.getDeg());
                        article.put("tell",shareUser.getTell());
                        article.put("desc_id",a);
                    }else if (shareUser.getTell() == 1){
                        BrandCourse shareVideo = brandCourseMapper.selectCourseId(shareUser.getCourse_id());
                        if(shareVideo == null){
                            continue;
                        }
                        article.put("sign",1);
                        article.put("id",shareUser.getCourse_id());
                        article.put("title",shareVideo.getTitle());
                        article.put("coverImg",shareVideo.getCover_img());
                        article.put("price",shareVideo.getPrice());
                        article.put("quantity",shareVideo.getQuantity());
                        article.put("time",shareUser.getRead_time_begin());
                        article.put("deg",shareUser.getDeg());
                        article.put("tell",shareUser.getTell());
                        article.put("desc_id",a);
                    }

                    dataInfo.add(article);
                }
            }
        }
        Collections.sort(dataInfo, new Comparator<Map<String,Object>>() {
            public int compare(Map<String,Object> o1,Map<String,Object> o2){
                Integer name1 = (Integer) o1.get("desc_id");
                Integer name2 = (Integer) o2.get("desc_id");

                return name1.compareTo(name2);
            }
        });

        Map<String,Object> map = new HashMap<>();
        map.put("dataInfo",dataInfo);
        map.put("userInfo",xcuserMapper.selectOpenId(openId));
        return map;
    }


    /**
     * 根据openId查询素材分类标签
     * @return
     */
    public Map<String, Object> openidClassify(Map<String,Object> datas) {
        //获取openid
        String openId = (String) datas.get("openId");

        //存放所有的分类数据
        List<Map<String,Object>> classify = new ArrayList();

        Map<String, Object> data = new HashMap<>();
        //创建list用于存放返回的数据
        List brandList = new ArrayList();

        //查询自己创建的品牌
        List<BrandNav> brandNav = brandNavMapper.selectBrand(openId);
        //查询所有品牌,不包含自己的品牌
        List<BrandNav> brands = brandNavMapper.selectBrands(openId);
        //根据openid查询用户信息
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);

        //创建数组用于判断靠前的家庭教育品牌是否为自己创建的品牌
        List<String>  muzixin = new ArrayList<>();

        /**
         * 所有品牌
         */
        List<Map<String,Object>> brandLists = new ArrayList<>();

        if (xcuser.getStart() < 4){//当用户身份为非品牌会员时

            //创建list用于存放品牌二级分类
            List<Map<String,Object>> classifys = new ArrayList<>();
            //创建map用于存放默认品牌数据
            Map<String,Object> brandinfo = new HashMap<>();
            brandinfo.put("id",brandNavMapper.selectBrandName(openId,WeChatUtil.AUTHOR).getBrand_id());
            brandinfo.put("brand_name",WeChatUtil.AUTHOR);
            brandinfo.put("brandClassify",classifys);
            //类型
            brandinfo.put("type",0);
            //将数据放入list中
            brandList.add(brandinfo);
            data.put("brand", brandList);
            classify.add(brandinfo);
        } else {//当用户身份为品牌会员时
            for (BrandNav nav : brandNav) {
                if (nav.getBrand_name() == null){
                    continue;
                }

                //创建list用于存放品牌二级分类
                List<Map<String,Object>> classifys = new ArrayList<>();
                //创建map用于存放默认品牌数据
                Map<String,Object> brandinfo = new HashMap<>();
                brandinfo.put("id",nav.getBrand_id());
                brandinfo.put("brand_name",nav.getBrand_name());


                //查找二级分类
                List<BrandClassify> brandClassifies = brandClassifyMapper.selectBrandClassify(nav.getBrand_id());
                for (BrandClassify brandClassify : brandClassifies) {
                    Map<String,Object> brandClassifyInfo = new HashMap<>();
                    brandClassifyInfo.put("classifyId",brandClassify.getClassify_id());
                    brandClassifyInfo.put("classifyName",brandClassify.getBrand_classify_name());
                    classifys.add(brandClassifyInfo);
                }
                //品牌二级分类
                brandinfo.put("brandClassify",classifys);
                //类型
                brandinfo.put("type",0);
                classify.add(brandinfo);
                brandList.add(brandinfo);
                if (nav.getBrand_name().equals("家庭教育")){
                    muzixin.add("家庭教育");
                    continue;
                }
                brandLists.add(brandinfo);

            }
        }

        //查找家庭教育品牌并靠前
        if (muzixin.size() == 0){
            for (BrandNav brand : brands) {
                if (brand.getBrand_name().equals("家庭教育")){
                    //创建map用于存放默认品牌数据
                    Map<String,Object> brandinfos = new HashMap<>();

                    //创建list用于存放品牌二级分类
                    List<Map<String,Object>> classifys = new ArrayList<>();

                    brandinfos.put("id",brand.getBrand_id());
                    brandinfos.put("brand_name",brand.getBrand_name());
                    //查找二级分类
                    List<BrandClassify> brandClassifies = brandClassifyMapper.selectBrandClassify(brand.getBrand_id());
                    for (BrandClassify brandClassify : brandClassifies) {
                        Map<String,Object> brandClassifyInfo = new HashMap<>();
                        brandClassifyInfo.put("classifyId",brandClassify.getClassify_id());
                        brandClassifyInfo.put("classifyName",brandClassify.getBrand_classify_name());
                        classifys.add(brandClassifyInfo);
                    }
                    //品牌二级分类
                    brandinfos.put("brandClassify",classifys);
                    //类型
                    brandinfos.put("type",0);
                    classify.add(brandinfos);
                    break;
                }

            }
        }
        //根据openId查询选择的标签
        List<UserClassify> userClassifies = userClassifyMapper.selectUserClassify(openId);
        //将用户选择的标签加入list
        List classifyListUser = new ArrayList();
        if (userClassifies.size() == 0) {

            data.put("userNav", classifyListUser);
        } else {
            List<Map<String,Object>> userLists = new ArrayList<>();
            for (UserClassify userClassify : userClassifies) {
                ArticleClassify classifyName = articleClassifyMapper.selectIdArticle(userClassify.getClassify_id());
                Map<String,Object> classifyData = new HashMap<>();
                classifyData.put("id",userClassify.getClassify_id());
                classifyData.put("brand_name",classifyName.getClassify_name());
                classifyData.put("brandClassify",userLists);
                classifyData.put("type",1);
                classifyListUser.add(classifyData);
                classify.add(classifyData);
            }
            data.put("userNav", classifyListUser);
        }


        List classifyList = new ArrayList();

        List classifyLists = new ArrayList();
        //查询所有的分类标签
        List<ArticleClassify> articleClassifies = articleClassifyMapper.selectClassifyList();
        //将固定要展示的标签加入list
        for (ArticleClassify articleClassify : articleClassifies) {
            if (articleClassify.getClassify_show() == 1 && articleClassify.getClassify_name().equals("推荐")) {
                continue;
            } else if (articleClassify.getClassify_show() == 1) {
                Map<String,Object> classifyData = new HashMap<>();
                classifyData.put("id",articleClassify.getClassify_id());
                classifyData.put("brand_name",articleClassify.getClassify_name());
                classifyData.put("brandClassify",classifyLists);
                classifyData.put("type",1);
                classifyList.add(classifyData);
                classify.add(classifyData);
            }
            data.put("classify", classifyList);
        }

        //平台的品牌
        for (BrandNav brand : brands) {

            //创建list用于存放品牌二级分类
            List<Map<String,Object>> classifys = new ArrayList<>();
            //创建map用于存放默认品牌数据
            Map<String,Object> brandinfo = new HashMap<>();
            brandinfo.put("id",brand.getBrand_id());
            brandinfo.put("brand_name",brand.getBrand_name());

            //查找二级分类
            List<BrandClassify> brandClassifies = brandClassifyMapper.selectBrandClassify(brand.getBrand_id());
            for (BrandClassify brandClassify : brandClassifies) {
                Map<String,Object> brandClassifyInfo = new HashMap<>();
                brandClassifyInfo.put("classifyId",brandClassify.getClassify_id());
                brandClassifyInfo.put("classifyName",brandClassify.getBrand_classify_name());
                classifys.add(brandClassifyInfo);
            }
            //品牌二级分类
            brandinfo.put("brandClassify",classifys);
            //类型
            brandinfo.put("type",0);

            brandLists.add(brandinfo);
            if (brand.getBrand_name().equals("家庭教育")){
                continue;
            }
            classify.add(brandinfo);
        }

        List<Map<String,Object>> a = new ArrayList<>();
        List<Map<String,Object>> b = new ArrayList<>();


        for (Map<String, Object> map : classify) {
            Integer type = (Integer) map.get("type");
            Integer id = (Integer) map.get("id");
            String brand_name = (String) map.get("brand_name");
            if (type == 0){
                UserBrand userBrand = userBrandMapper.selectUserBrand(openId,id);
                if (userBrand == null){
                    b.add(map);
                }else if (userBrand != null || brand_name.equals(WeChatUtil.AUTHOR)){
                    a.add(map);
                }
            }else {
                b.add(map);
            }
        }

        for (Map<String, Object> map : b) {
            a.add(map);
        }


        //其他人创建的品牌
        data.put("brandList",brandLists);
        //我的品牌
        data.put("brand", brandList);
        //导航品牌分类排序
//        data.put("navList", classify);
        data.put("navList", a);

        return data;
    }

    //首页导航
    public List<Map<String, Object>> openidClassifys(Map<String,Object> data) {
        //获取openid
        String openId = (String) data.get("openId");

        //存放所有的分类数据
        List<Map<String,Object>> dataList = new ArrayList();

        List<Integer> brandIds = new ArrayList<>();

        //导航顺序:热门+选择的品牌+自己创建的品牌+选择的分类+系统分类+没有选择的品牌


        //查询用户选择的品牌
        List<UserBrand> userBrands = userBrandMapper.selectUserBrands(openId);

        if (userBrands.size() == 0){

        }else {

            for (UserBrand userBrand : userBrands) {
                brandIds.add(userBrand.getBrand_id());
                //查询品牌的名称
                BrandNav brandNav = brandNavMapper.selectBrandId(userBrand.getBrand_id());
                //通过品牌查询二级分类
                List<BrandClassify> brandClassifies = brandClassifyMapper.selectBrandClassify(userBrand.getBrand_id());
                Map<String,Object> datas = new HashMap<>();
                if (brandClassifies.size() == 0){
                    datas.put("id",brandNav.getBrand_id());
                    datas.put("name",brandNav.getBrand_name());
                    List<String> a = new ArrayList<>();
                    datas.put("classify",a);
                    datas.put("start",1);
                }else {
                    datas.put("id",brandNav.getBrand_id());
                    datas.put("name",brandNav.getBrand_name());
                    List<Map<String,Object>> classifyList = new ArrayList<>();
                    for (BrandClassify brandClassify : brandClassifies) {
                        Map<String,Object> classifydatas = new HashMap<>();
                        classifydatas.put("classifyId",brandClassify.getClassify_id());
                        classifydatas.put("classifyName",brandClassify.getBrand_classify_name());
                        classifyList.add(classifydatas);
                    }
                    datas.put("classify",classifyList);
                    datas.put("start",1);
                }
                //将计算出来的用户选择的品牌加入到返回的数据中
                dataList.add(datas);
            }
        }

        //查询自己创建的品牌
        List<BrandNav> userbrandNavs = brandNavMapper.selectBrandUser(openId);
        if (userbrandNavs.size() == 0){

        }else {
            for (BrandNav userbrandNav : userbrandNavs) {
                if (brandIds.contains(userbrandNav.getBrand_id())){
                    continue;
                }else {
                    //通过品牌查询二级分类
                    List<BrandClassify> brandClassifies = brandClassifyMapper.selectBrandClassify(userbrandNav.getBrand_id());
                    Map<String,Object> datas = new HashMap<>();
                    if (brandClassifies.size() == 0){
                        datas.put("id",userbrandNav.getBrand_id());
                        datas.put("name",userbrandNav.getBrand_name());
                        List<String> a = new ArrayList<>();
                        datas.put("classify",a);
                        datas.put("start",1);
                    }else {
                        datas.put("id",userbrandNav.getBrand_id());
                        datas.put("name",userbrandNav.getBrand_name());
                        List<Map<String,Object>> classifyList = new ArrayList<>();
                        for (BrandClassify brandClassify : brandClassifies) {
                            Map<String,Object> classifydatas = new HashMap<>();
                            classifydatas.put("classifyId",brandClassify.getClassify_id());
                            classifydatas.put("classifyName",brandClassify.getBrand_classify_name());
                            classifyList.add(classifydatas);
                        }
                        datas.put("classify",classifyList);
                        datas.put("start",1);
                    }
                    //将计算出来的用户选择的品牌加入到返回的数据中
                    dataList.add(datas);
                }
            }
        }


        //查询用户选择的分类
        List<UserClassify> userClassifies = userClassifyMapper.selectUserClassify(openId);
        if (userClassifies.size() == 0){

        }else {
            for (UserClassify userClassify : userClassifies) {
                //通过用户选择的分类到分类表中查询分类详情
                ArticleClassify articleClassify = articleClassifyMapper.selectIdArticle(userClassify.getClassify_id());
                Map<String,Object> datas = new HashMap<>();
                datas.put("id",articleClassify.getClassify_id());
                datas.put("name",articleClassify.getClassify_name());
                List<String> a = new ArrayList<>();
                datas.put("classify",a);
                datas.put("start",0);
                //将计算出来的用户选择的系统分类加入到返回的数据中
                dataList.add(datas);
            }
        }



        //查询需要固定展示的系统分类
        List<ArticleClassify> articleClassifyList = articleClassifyMapper.selectClassifys();
        for (ArticleClassify articleClassi : articleClassifyList) {
            Map<String,Object> datas = new HashMap<>();
            datas.put("id",articleClassi.getClassify_id());
            datas.put("name",articleClassi.getClassify_name());
            List<String> a = new ArrayList<>();
            datas.put("classify",a);
            datas.put("start",0);
            //将计算出来的固定展示的系统分类加入到返回的数据中
            dataList.add(datas);
        }



        //查询所有用户未选择的品牌
        List<BrandNav> brandNavs = brandNavMapper.selectBrands(openId);
        //当用户选择的品牌不为0时
        for (BrandNav brandNav : brandNavs) {
            if (brandIds.contains(brandNav.getBrand_id())){
                continue;
            }else {
                //通过品牌查询二级分类
                List<BrandClassify> brandClassifies = brandClassifyMapper.selectBrandClassify(brandNav.getBrand_id());
                Map<String,Object> datas = new HashMap<>();
                if (brandClassifies.size() == 0){
                    datas.put("id",brandNav.getBrand_id());
                    datas.put("name",brandNav.getBrand_name());
                    List<String> a = new ArrayList<>();
                    datas.put("classify",a);
                    datas.put("start",1);
                }else {
                    datas.put("id",brandNav.getBrand_id());
                    datas.put("name",brandNav.getBrand_name());
                    List<Map<String,Object>> classifyList = new ArrayList<>();
                    for (BrandClassify brandClassify : brandClassifies) {
                        Map<String,Object> classifydatas = new HashMap<>();
                        classifydatas.put("classifyId",brandClassify.getClassify_id());
                        classifydatas.put("classifyName",brandClassify.getBrand_classify_name());
                        classifyList.add(classifydatas);
                    }
                    datas.put("classify",classifyList);
                    datas.put("start",1);
                }
                //将计算出来的用户选择的品牌加入到返回的数据中
                dataList.add(datas);
            }
        }


        return dataList;
    }











    /**
     * 查询品牌详情
     */
    public Map<String,Object> selectBrandInfo(Map<String,Object> data){
        Integer id = (Integer) data.get("id");
        //查询品牌信息
        BrandNav brandNav = brandNavMapper.selectBrandId(id);
        //查询快捷选择分类
        List<BillingBrandClassify> billingBrandClassifies = billingBrandClassifyMapper.selectClassify();

        List<Integer> billingClassify = new ArrayList<>();

        Map<String,Object> dataInfo = new HashMap<>();
        if (brandNav.getBrand_name() == null){
            dataInfo.put("id",brandNav.getBrand_id());
            dataInfo.put("brandName","");
            List<String> a = new ArrayList<>();
            dataInfo.put("brandClassify",a);
        }else {
            dataInfo.put("id",brandNav.getBrand_id());
            dataInfo.put("brandName",brandNav.getBrand_name());
            //查询品牌二级分类信息
            List<BrandClassify> brandClassifies = brandClassifyMapper.selectBrandClassify(id);
            //判断二级分类是否为空
            if (brandClassifies.size() == 0){
                List<String> a = new ArrayList<>();
                dataInfo.put("brandClassify",a);
            }else {//二级分类不为空时
                //整理二级分类
                List<Map<String,Object>> classifys = new ArrayList<>();
                for (BrandClassify brandClassify : brandClassifies) {
                    Map<String,Object> classify = new HashMap<>();
                    classify.put("id",brandClassify.getClassify_id());
                    classify.put("name",brandClassify.getBrand_classify_name());
                    if (brandClassify.getBilling_classify() > 0){
                        billingClassify.add(brandClassify.getBilling_classify());
                    }
                    classify.put("billingClassify",brandClassify.getBilling_classify());


                    classifys.add(classify);
                }
                dataInfo.put("brandClassify",classifys);
            }
        }

        //整理快捷分类数据
        List<Map<String,Object>> classifyList = new ArrayList<>();
        for (BillingBrandClassify billingBrandClassify : billingBrandClassifies) {
            boolean boll = billingClassify.contains(billingBrandClassify.getClassify_id());
            Map<String,Object> classify = new HashMap<>();
            classify.put("id",billingBrandClassify.getClassify_id());
            classify.put("name",billingBrandClassify.getBrand_classify_name());
            if (boll == true){
                classify.put("start",true);
            }else {
                classify.put("start",false);
            }
            classifyList.add(classify);
        }
        //二级分类
        dataInfo.put("billingClassify",classifyList);
        dataInfo.put("quantity",brandNav.getQuantity());
        return dataInfo;
    }






    /**
     * 根据openId修改导航
     */
    public Map<String, Object> updataUserClassify(Map<String, Object> obj) {
        //获取用户最新选择的标签分类或者品牌
        List<Integer> classifys = (List<Integer>) obj.get("classifys");
        List<Integer> brands = (List<Integer>) obj.get("brands");
        //获取openId
        String openId = (String) obj.get("openId");

        //根据openId将分类删除
        userClassifyMapper.deleteUserClassify(openId);

        //根据openid将品牌分类删除
        userBrandMapper.deleteUserBrand(openId);
        //将datas写入数据库
        for (Integer data : classifys) {
            userClassifyMapper.insertUserClassify(openId, data);
        }
        for (Integer data : brands) {
            userBrandMapper.insertUserBrand(openId,data);
        }



        return openidClassify(obj);
    }


    /**
     * 上传图文接口
     */
    public Map addArticle(Article articles) throws JSONException {
        String article = "{\n" +
                "  \"articles\": [{\n" +
                "     \"title\": \"" + articles.getTitle() + "\",\n" +
                "     \"thumb_media_id\": \"" + articles.getThumb_media_id() + "\",\n" +
                "     \"author\": \"" + articles.getAuthor() + "\",\n" +
                "     \"show_cover_pic\": " + 0 + ",\n" +
                "     \"content\": \"" + articles.getContent() + "\"\n" +
                "     }\n" +
                "    ]\n" +
                "}";
        String media = accessTokenUtil.postAddArticle(article);
        JSONObject media_id = new JSONObject(new String(media));
        Map map = new HashMap();
        map.put("media_id", media_id.getString("media_id"));

        return map;
    }






    /**
     * 根据opid返回所有标签分类,带选中的
     */
    public List<Map> classifyList(String openId) {
        //查询所有系统标签
        List<ArticleClassify> articleClassifies = articleClassifyMapper.selectClassifyList();
        //查询所有选中的标签
        List<UserClassify> userClassifies = userClassifyMapper.selectUserClassify(openId);
        //创建空list存放系统标签
        List classifys = new ArrayList();

        for (ArticleClassify classify : articleClassifies) {
            classifys.add(classify.getClassify_name());
        }

        List<Map> datas = new ArrayList<>();
        //选中的标签
        for (ArticleClassify articleClassify : articleClassifies) {
            for (UserClassify userClassify : userClassifies) {
                if (articleClassify.getClassify_id() == userClassify.getClassify_id()) {
                    classifys.remove(articleClassify.getClassify_name());
                    Map<String, Object> map = new HashMap<>();
                    map.put("classify_id", articleClassify.getClassify_id());
                    map.put("classify_name", articleClassify.getClassify_name());
                    map.put("show", articleClassify.getClassify_show());
                    map.put("checked", true);
                    datas.add(map);
                }
            }
        }

        for (ArticleClassify articleClass : articleClassifies) {
            for (Object ass : classifys) {
                if (articleClass.getClassify_name() == ass) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("classify_id", articleClass.getClassify_id());
                    map.put("classify_name", articleClass.getClassify_name());
                    map.put("show", articleClass.getClassify_show());
                    map.put("checked", false);
                    datas.add(map);
                }
            }
        }
        return datas;
    }


    /**
     * 根据分类标签/品牌查询文章
     *
     * @return
     */
    public List<Map<String, Object>> classifyArticle(Map<String, Object> classify) {
        //获取文章还是视频分类
        int a = (int) classify.get("classify");
        //获取标签名称
        String name = (String) classify.get("name");
        //获取分页数量
        int pag = (int) classify.get("pag") * 10;
        //用户主键id
        String openId = (String) classify.get("openId");



        List dataList = new ArrayList<>();

        if (a == 0) {
            List<Article> articles = new ArrayList<>();
            List<BrandArticle> brandArticles = new ArrayList<>();

            if (name.equals("热门")) {
//                articles = articleMapper.selectTitle(pag);
//                articles = brandArticleMapper.selectBrandArticleAdmin(pag);
                brandArticles = brandArticleMapper.selectBrandArticleAdmins(pag,"家庭教育");
                if (brandArticles.size() == 0) {
                    return null;
                }
                for (BrandArticle art : brandArticles) {
                    Map<String, Object> datas = new HashMap<>();
                    datas.put("id", art.getArticle_id());
                    datas.put("title", art.getTitle());
                    datas.put("cover_img", art.getCover_img());
                    datas.put("quantity",art.getQuantity());
                    datas.put("price","免费");
                    dataList.add(datas);
                }
            } else {
                ArticleClassify claName = articleClassifyMapper.selectNameArticle(name);
                articles = articleMapper.selectClassify(claName.getClassify_id(), pag);
                if (articles.size() == 0) {
                    return null;
                }
                for (Article art : articles) {
                    Map<String, Object> datas = new HashMap<>();
                    datas.put("id", art.getId());
                    datas.put("title", art.getTitle());
                    datas.put("cover_img", art.getCover_img());
                    datas.put("quantity",art.getQuantity());
                    datas.put("price","免费");
                    dataList.add(datas);
                }

            }
            return dataList;
        } else if (a == 1) {
            List<Video> articles = new ArrayList<>();
            List<BrandVideo> brandVideos = new ArrayList<>();
            if (name.equals("热门")) {
//                articles = videoMapper.selectVideoTiele(pag);
                brandVideos = brandVideoMapper.selectBrandNames("家庭教育",pag);
                if (brandVideos.size() == 0) {
                    return null;
                }
                for (BrandVideo art : brandVideos) {
                    Map<String, Object> datas = new HashMap<>();
                    datas.put("id", art.getVideo_id());
                    datas.put("title", art.getTitle());
                    datas.put("cover_img", art.getCover_img());
                    datas.put("quantity",art.getQuantity());
                    datas.put("price","免费");
                    datas.put("sign",1);
                    dataList.add(datas);
                }
            } else {
                ArticleClassify claName = articleClassifyMapper.selectNameArticle(name);
                articles = videoMapper.selectClaVideo(claName.getClassify_id(), pag);
                if (articles.size() == 0) {
                    return null;
                }
                for (Video art : articles) {
                    Map<String, Object> datas = new HashMap<>();
                    datas.put("id", art.getVideo_id());
                    datas.put("title", art.getVideo_name());
                    datas.put("cover_img", art.getCover_url());
                    datas.put("quantity",art.getQuantity());
                    datas.put("price","免费");
                    datas.put("sign",0);
                    dataList.add(datas);
                }
            }
            return dataList;
        } else if (a == 2) {
            List<Course> articles = new ArrayList<>();
            List<BrandCourse> brandCourses = new ArrayList<>();
            Xcuser xcuser = xcuserMapper.selectOpenId(openId);
            if (name.equals("热门")){
                //查询品牌课程
                brandCourses = brandCourseMapper.selectBrandName("家庭教育",pag);

            }else{
                ArticleClassify claName = articleClassifyMapper.selectNameArticle(name);
                articles = courseMapper.selectClassify(claName.getClassify_id(), pag);
            }

            if (name.equals("热门")){
                for (BrandCourse brandCours : brandCourses) {
                    Map<String, Object> datas = new HashMap<>();
                    datas.put("id", brandCours.getCourse_id());
                    datas.put("title", brandCours.getTitle());
                    datas.put("cover_img", brandCours.getCover_img());
                    datas.put("quantity",brandCours.getQuantity());
                    datas.put("price",brandCours.getPrice());
                    datas.put("start",1);
                    datas.put("tell",brandCours.getTell());
                    dataList.add(datas);
                }
            }else {
                for (Course brandCours : articles) {
                    Map<String, Object> datas = new HashMap<>();
                    datas.put("id", brandCours.getCourse_id());
                    datas.put("title", brandCours.getTitle());
                    datas.put("cover_img", brandCours.getCover_img());
                    datas.put("quantity",brandCours.getQuantity());
                    datas.put("price",brandCours.getPrice());
                    datas.put("start",0);
                    datas.put("tell",brandCours.getTell());
                    dataList.add(datas);
                }
            }
            return dataList;
        }
        return null;
    }


    /**
     * 创建我的品牌
     */
    public String insertBrand(Map<String, Object> brand){
        // 获取品牌id
        Integer brandId = (Integer) brand.get("brandId");
        //用户唯一标识
        String openId = (String) brand.get("openId");
        //品牌名称
        String brandName = (String) brand.get("brandName");
        //type
        String type = (String) brand.get("type");
        //品牌分类
        List<Map<String,Object>> classifys = (List<Map<String, Object>>) brand.get("classifys");

        //获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());

        //检测品牌名字是否为空
        if (brandName.equals("")){
            return "0";//为空
        }



        //判断二级分类是否有重复
        List<String> b = new ArrayList<>();
        for (Map<String, Object> classify : classifys) {
           String name = (String) classify.get("name");
           b.add(name);
        }
        HashSet<String> set = new HashSet<String>(b);
        boolean result = set.size() == b.size()?true:false;

        if (result == false){
            return "2"; //二级分类里面有重复数据
        }

        //判断执行添加还是更新
        if (type.equals("insert")){//新增操作
            //检测品牌名称是否重名
            if(brandNavMapper.selectBrandNames(brandName) != null){
                return "1";//已经重名
            }
            //根据openId查询品牌
            List<BrandNav> brands = brandNavMapper.selectBrand(openId);
            //写入一个品牌,并将show为1的品牌改为show为0
            brandNavMapper.updataBrandShow(openId,1,0);
            //根据品牌id更新数据
            brandNavMapper.updataBrandInfo(brandId,brandName,1,time);

            //判断品牌二级分类是否为空
            for (Map<String,Object> classify : classifys) {
                String name = (String) classify.get("name");
                Integer classifyid = (Integer) classify.get("billingClassify");
                brandClassifyMapper.insertBrandClassify(openId,name,brandId,classifyid);
            }

            return "创建成功";
        }else if (type.equals("update")){//更新操作
            //检测名称是否跟数据库的不一致在判断修改次数
            BrandNav name = brandNavMapper.selectBrandId(brandId);
            if (!name.getBrand_name().equals(brandName)){
                String brandQuantity = getBrandQuantity(brandId);
                if (brandQuantity.equals("不能改")){
                    return "4";
                }
                if (name.getQuantity()>0) {//当修改次数小于4次时
                    //修改名字
                    brandNavMapper.updataBrandName(brandId, brandName);
                }else {
                    return "3";
                }
            }

            //查询该品牌数据库中所有的二级分类
            List<BrandClassify> brandClassifies = brandClassifyMapper.selectBrandClassify(brandId);
            //该品牌中所有二级分类的名称
            List<String> classifyName = new ArrayList<>();
            if (brandClassifies.size() != 0){
                for (BrandClassify brandClassify : brandClassifies) {
                    classifyName.add(brandClassify.getBrand_classify_name());
                }
            }

            //传入分类整理
            List<String> userClassifyName = new ArrayList<>();
            for (Map<String, Object> classify : classifys) {
                userClassifyName.add((String) classify.get("name"));
            }

            //传入二级分类与数据库二级分类的不同元素
            List<String> different = (List<String>) CollectionUtil.getDifferent(classifyName,userClassifyName);

            for (String s : different) {
                //到数据库中查询是否有该元素,有则删除,没有则存入
                BrandClassify brandClassify = brandClassifyMapper.selectBrandName(brandId,s);
                if (brandClassify == null){
                    Integer a = 0;
                    for (Map<String, Object> classify : classifys) {
                        if (s.equals(classify.get("name"))){
                            a = (Integer) classify.get("billingClassify");
                        }
                    }
                    brandClassifyMapper.insertBrandClassify(openId,s,brandId,a);
                }else if (brandClassify != null){
                    brandClassifyMapper.deleteBrandName(brandId,s);
                    //将文章中删除掉的分类名称改为null
                    brandArticleMapper.updataArticleClassify(brandId,s);
                    //将视频中删除掉的分类名称改为null
                    brandVideoMapper.updataVideoClassify(brandId,s);
                    //将课程中删除掉的分类名称改为null;
                    brandCourseMapper.updataCourseClassify(brandId,s);
                }

            }
            return "更新成功";
        }
        return null;
    }

    /**
     * 品牌名字查重
     */
    public String selectBrandName(Map<String,Object> data){
        String brandName = (String) data.get("brand_name");
        BrandNav brandNav = brandNavMapper.selectBrandNames(brandName);
        if (brandNav != null){
            return "2";
        }
        return "没有重复";
    }

    /**
     * 查询品牌
     */
    public List<BrandNav> selectBrand(Map<String,Object> data){
        //获取openId
        String openId = (String) data.get("openId");
        //查询用户信息
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);
        List<BrandNav> a = new ArrayList<>();
        if (xcuser.getStart() < 4){
            a.add(brandNavMapper.selectBrandName(openId,WeChatUtil.AUTHOR));
        }else {
            List<BrandNav> brandNavs =  brandNavMapper.selectBrand(openId);
            if (brandNavs.size() != 0){
                for (BrandNav brandNav : brandNavs) {
                    if (brandNav.getBrand_name()!=null){
                        a.add(brandNav);
                    }
                }
            }
        }
        return a;
    }

    /**
     * 查询品牌列表
     */
    public List<BrandNav> selectBrandList(Map<String,Object> data){
        //获取openId
        String openId = (String) data.get("openId");
        //查询用户信息
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);
        List<BrandNav> a = new ArrayList<>();
        if (xcuser.getStart() < 4){
            a.add(brandNavMapper.selectBrandName(openId,WeChatUtil.AUTHOR));
        }else {
            List<BrandNav> brandNavs =  brandNavMapper.selectBrand(openId);
            //用户允许创建的品牌数量
            Integer userBrandQuantity = xcuser.getBrand_quantity()+1;
            //当前品牌表的数据
            Integer brandQuantity = brandNavs.size();
            //计算用户允许创建品牌数量和品牌表数量的差
            Integer quantity = userBrandQuantity - brandQuantity;
            if (quantity == 0 || quantity < 0){
                for (BrandNav brandNav : brandNavs) {
                    a.add(brandNav);
                }
            }else if (quantity > 0){
                for (int i = 0;i<quantity;i++){
                    brandNavMapper.insertBrands(openId,null,0,3);
                }
                List<BrandNav> brandNavss =  brandNavMapper.selectBrand(openId);
                for (BrandNav brandNavc : brandNavss) {
                    a.add(brandNavc);
                }
            }
        }
        return a;
    }




    /**
     * 删除品牌
     */
    public List<BrandNav> deleteBrand(Map<String,Object> data){
        // 获取品牌主键id
        Integer brandId = (Integer) data.get("brandId");

        String brandQuantity = getBrandQuantity(brandId);
        if (brandQuantity.equals("不能改")){
            return null;
        }

        brandNavMapper.updataBrandId(brandId);
        //删除品牌的二级分类
        brandClassifyMapper.deleteBrandClassify(brandId);
        //删除品牌所属的文章
        brandArticleMapper.deleteBrandId(brandId);
        //删除品牌所属的视频
        brandVideoMapper.deleteBrandId(brandId);
        //删除品牌所属课程等待随后开发
        brandCourseMapper.deleteBrandId(brandId);

        return selectBrandList(data);
    }


    public String getBrandQuantity(Integer brandId){
        BrandNav brandNav = brandNavMapper.selectBrandId(brandId);
        if (brandNav.getQuantity() > 0){
            return "大于零";
        }else if (brandNav.getQuantity() == 0 || brandNav.getQuantity() < 0){
            return "不能改";
        }
        return null;
    }

    /**
     * 文章采集页面采集
     */
    public Map getArticle(String url) {
        Map resp = SpiderUtil.getActicle(url);
        return resp;
    }

    /**
     * 公众号聊天框采集文章
     */
    public Map getArticles(String url, String openId) throws JSONException, JOSEException {
        Map resp = SpiderUtil.getActicles(url,openId);
        String title = (String) resp.get("title");
        String coverImg = (String) resp.get("coverImg");
        // 通过openId和孝粉云学堂查询品牌id
        BrandNav brandNav = brandNavMapper.selectBrandName(openId,WeChatUtil.AUTHOR);

        String urls = "http://web.xiaocisw.site/modifyArticle?selectBrand="+WeChatUtil.AUTHOR+"&brandId="+brandNav.getBrand_id()+"&mark=0&originalPath="+url;
        Map<String,Object> data = new HashMap<>();
        data.put("title",title);
        data.put("coverImg",coverImg);
        data.put("url",urls);
        return data;
    }

    public Map getVideo(String url) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            Matcher matcher = Patterns.WEB_URL.matcher(url);

            String urls = "";

            if (matcher.find()){
                urls = matcher.group();
            }

            return CrawlerUtil.demo2(urls);
        } catch (Exception e) {
            e.printStackTrace();
            return map;
        }
    }



    public Map getVideos(String url,String openId) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            Matcher matcher = Patterns.WEB_URL.matcher(url);

            String urls = "";

            if (matcher.find()){
                urls = matcher.group();
            }

            //得到视频数据
            Map<String,Object> data = CrawlerUtil.videos(urls);
            //将视频数据写入数据
            String title = (String) data.get("title");
            String coverImg = (String) data.get("coverImg");
            //获取品牌id
            BrandNav brandNav = brandNavMapper.selectBrandName(openId,WeChatUtil.AUTHOR);

            String urlss = "http://web.xiaocisw.site/modifyVideo?selectBrand="+WeChatUtil.AUTHOR+"&brandId="+brandNav.getBrand_id()+"&mark=0&originalPath="+urls;
            Map<String,Object> datas = new HashMap<>();
            datas.put("title",title);
            datas.put("coverImg",coverImg);
            datas.put("url",urlss);
            return datas;
        } catch (Exception e) {
            e.printStackTrace();
            return map;
        }
    }


    /**
     * 查询专题文章
     * @param data
     * @return
     */
    public List<Map<String,Object>> specialList(Map<String,Object> data){
        Integer pag = (Integer) data.get("pag");
        List<Map<String,Object>> dataList = new ArrayList<>();
        //查询专题文章
        List<Article> articles = articleMapper.selectSpecialArticle(pag*4,0);
        for (Article article : articles) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",article.getId());
            map.put("title",article.getTitle());
            map.put("coverImg",article.getCover_img());
            map.put("quantity",article.getQuantity());
            map.put("start",0);
            dataList.add(map);
        }
        //查询专题文章
        List<Video> videos = videoMapper.selectSpecialVideo(pag*2);
        for (Video video : videos) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",video.getVideo_id());
            map.put("title",video.getVideo_name());
            map.put("coverImg",video.getCover_url());
            map.put("quantity",video.getQuantity());
            map.put("start",1);
            dataList.add(map);
        }

        return dataList;
    }

    /**
     * 查询推荐品牌
     */
    public List<Map<String,Object>> selectBrandTop(Map<String,Object> data){
        String openId = (String) data.get("openId");
        List<BrandNav> brandNavs = brandNavMapper.selectBrands(openId);


        //整理数据
        List<Map<String,Object>> dataList = new ArrayList<>();
        for (BrandNav brandNav : brandNavs) {
            Map<String,Object> content = new HashMap<>();
            content.put("brandId",brandNav.getBrand_id());
            content.put("openId",brandNav.getOpen_id());
            content.put("brandName",brandNav.getBrand_name());
            UserBrand userBrand = userBrandMapper.selectUserBrand(openId,brandNav.getBrand_id());
            if (userBrand == null){
                content.put("show",false);
            }else {
                content.put("show",true);
            }
            dataList.add(content);
        }
        return dataList;
    }


    /**
     * 查询品牌id
     */
    public BrandNav getBrandId(String openId,String name){
        BrandNav brandNav = brandNavMapper.selectBrandName(openId,name);
        if (brandNav == null){
            brandNavMapper.insertBrands(openId,name,1,3);
            brandNav = brandNavMapper.selectBrandName(openId, WeChatUtil.AUTHOR);
        }
        return brandNav;
    }

}
