package com.example.xcschoolserver.service.impl;

import com.example.xcschoolserver.common.ReturnVO;
import com.example.xcschoolserver.common.WeChatUtil;
import com.example.xcschoolserver.mapper.*;
import com.example.xcschoolserver.pojo.*;
import com.example.xcschoolserver.service.IHomeService;
import com.example.xcschoolserver.util.AliOssUtil;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class HomeServiceIpml implements IHomeService {

    @Autowired
    private xcuserMapper xcuserMapper;
    @Autowired
    private brandNavMapper brandNavMapper;
    @Autowired
    private productMapper productMapper;
    @Autowired
    private brandArticleMapper brandArticleMapper;
    @Autowired
    private materialMapper materialMapper;
    @Autowired
    private brandVideoMapper brandVideoMapper;
    @Autowired
    private productImgMapper productImgMapper;
    @Autowired
    private posterMapper posterMapper;
    @Autowired
    private posterClassifyMapper posterClassifyMapper;
    @Autowired
    private leaveMapper leaveMapper;
    @Autowired
    private articleMapper articleMapper;
    @Autowired
    private videoMapper videoMapper;
    @Autowired
    private brandCourseMapper brandCourseMapper;
    @Autowired
    private courseCollectMapper courseCollectMapper;
    @Autowired
    private popImageMapper popImageMapper;
    @Autowired
    private teacherMapper teacherMapper;
    @Autowired
    private courseMapper courseMapper;



    /**
     * 查询主页信息
     * @param data
     * @return
     */
    public Map<String,Object> selectHome(Map<String,Object> data){
        String openId = (String) data.get("openId");
        Map<String,Object> homeinfo = new HashMap<>();
        Xcuser userinfo = xcuserMapper.selectOpenId(openId);
        List<BrandNav> brandNavs = new ArrayList<>();
        if (userinfo.getStart()<4) {//会员等级为非品牌会员时
            brandNavs.add(brandNavMapper.selectBrandName(openId,WeChatUtil.AUTHOR));
        }else {//会员等级会品牌会员时
            brandNavs = brandNavMapper.selectBrandss(openId);
        }

        List<String> a = new ArrayList<>();
        for (BrandNav brandNav : brandNavs) {
            if (brandNav.getStart() == 1){
                a.add("1");
            }
        }


        if (a.size() == 0){
            brandNavMapper.updataBrandOne(openId);
        }else if (a.size() >= 2){
            brandNavMapper.updataBrandOnes(openId);
            brandNavMapper.updataBrandOne(openId);
        }

        if (userinfo == null){
            return null;
        }
        if (userinfo.getUser_phone() == null){
            userinfo.setUser_phone("");
        }
        if (userinfo.getCode_url() == null){
            userinfo.setCode_url("");
        }
        //将会员信息加入map,用于返回数据
        homeinfo.put("userInfo",userinfo);

        if (brandNavs.size() == 0){//品牌列表为空时,添加一条默认品牌,并查询加入到map返回数据
            brandNavMapper.insertBrand(openId,WeChatUtil.AUTHOR,1);
            homeinfo.put("brand",brandNavMapper.selectBrand(openId));
        }else {//品牌列表有数据时
            //有品牌主页和默认主页
            for (BrandNav brandNav : brandNavs) {
                if (brandNav.getBrand_name() == null){
                    continue;
                }
                if (brandNav.getBrand_name().equals(WeChatUtil.AUTHOR)){
                    homeinfo.put("brand",brandNavs);
                    return homeinfo;
                }
            }
            //有品牌没有默认主页
            for (BrandNav brandNav : brandNavs) {
                if (brandNav.getStart() == 1){
                    if (brandNav.getBrand_name() == null){
                        continue;
                    }
                    brandNavMapper.insertBrand(openId,WeChatUtil.AUTHOR,0);
                    homeinfo.put("brand",brandNavMapper.selectBrand(openId));
                    return homeinfo;
                }
            }

        }


        return homeinfo;
    }

    /**
     * 阅读人查询主页
     */
    public Map<String,Object> selectReadHome(Map<String,Object> data){
        String openId = (String) data.get("openId");
//        String readOpenId = (String) data.get("readOpenId");
        String brandName = (String) data.get("brandName");
        Xcuser userInfo = xcuserMapper.selectOpenId(openId);
        BrandNav brandInfo = brandNavMapper.selectBrandName(openId,brandName);
        if (brandInfo == null){
            return null;
        }
        Map<String,Object> dataInfo = new HashMap<>();
        dataInfo.put("userName",userInfo.getUser_name());
        dataInfo.put("coverImg",userInfo.getUser_headimgurl());
        if (userInfo.getUser_phone() == null){
            userInfo.setUser_phone("");
        }
        dataInfo.put("userPhone",userInfo.getUser_phone());
        if (userInfo.getCode_url() == null){
            userInfo.setCode_url("");
        }
        dataInfo.put("codeUrl",userInfo.getCode_url());
        dataInfo.put("brandName",brandInfo.getBrand_name());
        dataInfo.put("textInfo",brandInfo.getText_info());
        return dataInfo;

    }


    /**
     * 切换品牌主页
     */
    public Map<String,Object> cutHome(Map<String,Object> data){
        String openId = (String) data.get("openId");
        String brandName = (String) data.get("brandName");
        Map<String,Object> homeinfo = new HashMap<>();
        Xcuser userinfo = xcuserMapper.selectOpenId(openId);
        if (userinfo.getUser_phone() == null){
            userinfo.setUser_phone("");
        }
        if (userinfo.getCode_url() == null){
            userinfo.setCode_url("");
        }
        homeinfo.put("userInfo",userinfo);



        brandNavMapper.updataBrandShow(openId,1,0);
        brandNavMapper.updataBrandstart(openId,brandName,1);

        homeinfo.put("brand",brandNavMapper.selectBrand(openId));

        return homeinfo;
    }

    /**
     * 更新品牌主页简介
     */
    public String updataInfo(Map<String,Object> data){
        Integer brandId = (Integer) data.get("brandId");
        String textInfo = (String) data.get("textInfo");
        brandNavMapper.updataTextInfo(brandId,textInfo);
        return "更改成功";
    }

    /**
     * 添加产品
     */
    public String addProduct(Map<String,Object> data){
        Integer productId = (Integer) data.get("productId");
//        Integer productId = null;
        String openId = (String) data.get("openId");
        Integer brandId = (Integer) data.get("brandId");
        String productTitle = (String) data.get("productTitle");
        String productDescribe = (String) data.get("productDescribe");
//        String describeImg = (String) data.get("describeImg");
        String url = (String) data.get("url");
        List<String> coverImg = (List<String>) data.get("coverImg");
        String price = (String) data.get("price");
        String vipPrice = (String) data.get("vipPrice");
        Integer start = (Integer) data.get("start");
        List<String> list = new ArrayList<String>(coverImg);

        if (productId == null){

            int a = productMapper.insertProduct(openId,brandId,productTitle,productDescribe,url,price,start,null,vipPrice);

            Product id = productMapper.selectProductId(openId,brandId,productTitle,productDescribe,url,price,start);

            if (a == 0){
                return null;
            }

            if (coverImg.size() != 0){
                int sort = 0;
                for (String s : coverImg) {
                    sort += 1;
                    productImgMapper.insertProductImg(openId,s,sort,id.getProduct_id());
                }
            }

            return "添加成功";
        }else{
            //根据产品id到数据库查询产品图片并删除
            List<ProductImg> productImgs = productImgMapper.selectProductImgList(openId,productId);


            //创建数据库产品图片数组
            List<String> coverImgs = new ArrayList<>();
            for (ProductImg productImg : productImgs) {
                coverImgs.add(productImg.getCover_url());
            }

            //创建list存放两个list中相同的元素
            List<String> cover = new ArrayList<>();
            //对比传入图片数组和数据库产品数组的相同
            for (String img : coverImgs) {
                for (String s : coverImg) {
                    if (img.equals(s)){
                        cover.add(img);
                    }
                }
            }

            //计算数据库中的不同
            coverImgs.removeAll(cover);
            //计算传入图片的不同
            coverImg.removeAll(cover);


            //将产品图片在oss中删除
            List<String> a = new ArrayList<>();
            if (coverImgs.size() != 0){
                for (String img : coverImgs) {
                    //将不同的在数据中删除
                    productImgMapper.deleteProductImgs(openId,img,productId);
                    String[] b =  img.split("/");
                    a.add(b[3]+"/"+b[4]);
                }
                //将之前的二维码删除
                AliOssUtil.deleteObjects(WeChatUtil.BUCKETNAME,a);
            }

            //将数据库图片删除
            productImgMapper.deleteProductImg(openId,productId);


            //将新的图片路径写入数据库
            if (list.size() != 0){
                int sort = 0;
                for (String s : list) {
                    sort += 1;
                    productImgMapper.insertProductImg(openId,s,sort,productId);
                }
            }
            //将跟新后的产品写入数据库
            int ac = productMapper.updataProduct(productId,productTitle,productDescribe,url,price,start,null,vipPrice);
            if (ac == 0){
                return null;
            }




            return "更新成功";
        }

    }

    /**
     * 查询打卡海报
     */
    public Map<String,Object> selectPoster(Map<String,Object> data){
        Integer pag = (Integer) data.get("pag")*20;
        Integer classifyId = (Integer) data.get("classifyId");
        Map<String,Object> posterInfo = new HashMap<>();
        if (classifyId == null){
            //查询海报
            List<Poster> posters = posterMapper.selectPoster(pag);
            //查询分类
            List<PosterClassify> posterClassifies = posterClassifyMapper.selectPosterClassify();
            if (posters.size() == 0){
                return null;
            }

            List<Map<String,Object>> posterList = new ArrayList<>();
            for (Poster poster : posters) {
                Map<String,Object> pos = new HashMap<>();
                pos.put("title",poster.getPoster_title());
                pos.put("posterUrl",poster.getPoster_url());
                posterList.add(pos);
            }
            List<Map<String,Object>> posterClassifyList = new ArrayList<>();
            for (PosterClassify posterClassify : posterClassifies) {
                if (posterClassify.getStart() == 0){
                    continue;
                }
                Map<String,Object> cla = new HashMap<>();
                cla.put("classifyId",posterClassify.getClassify_id());
                cla.put("posterClassifyName",posterClassify.getPoster_classify_name());

                posterClassifyList.add(cla);
            }

            posterInfo.put("posterInfo",posterList);
            posterInfo.put("posterNav",posterClassifyList);

        }else {
            List<Poster> posters =posterMapper.selectPosterClassify(classifyId,pag);
            if (posters.size() == 0){
                return null;
            }
            List<Map<String,Object>> posterList = new ArrayList<>();
            for (Poster poster : posters) {
                Map<String,Object> pos = new HashMap<>();
                pos.put("title",poster.getPoster_title());
                pos.put("posterUrl",poster.getPoster_url());
                posterList.add(pos);
            }

            posterInfo.put("posterInfo",posterList);
        }
        return posterInfo;
    }




    /**
     * 查询产品列表或详情
     */
    public Map<String,Object> selectProduct(Map<String,Object> data){
        String openId = (String) data.get("openId");
        Integer brandId = (Integer) data.get("brandId");
        Integer productId = (Integer) data.get("productId");

        Map<String,Object> productInfo = new HashMap<>();
        if (productId == null){
            Integer pag = (Integer) data.get("pag") * 10;
            List<Product> products = productMapper.selectProductList(openId,brandId,pag,0);
            List<Product> productTops = productMapper.selectProductList(openId,brandId,0,1);
            if (products.size() == 0 && productTops.size() == 0 ){
                return null;
            }

            //普通商品
            List pro = new ArrayList<>();
            //热卖商品
            List proTop = new ArrayList();
            if (products.size() != 0){
                for (Product product1 : products) {
                    Map<String,Object> product = new HashMap<>();
                    product.put("productId",product1.getProduct_id());
                    product.put("productTitle",product1.getProduct_title());
                    ProductImg productImg = productImgMapper.selectProductImg(openId,product1.getProduct_id());
                    if (productImg != null){
                        product.put("coverImg",productImg.getCover_url());
                    }else {
                        product.put("coverImg","");
                    }
                    product.put("price",product1.getPrice());
                    product.put("vipPrice",product1.getVip_price());
                    product.put("start",product1.getStart());
                    pro.add(product);
                }
            }
            if (productTops.size() != 0){
                for (Product productTop : productTops) {
                    Map<String,Object> product = new HashMap<>();
                    product.put("productId",productTop.getProduct_id());
                    product.put("productTitle",productTop.getProduct_title());
                    product.put("coverImg",productImgMapper.selectProductImg(openId,productTop.getProduct_id()).getCover_url());
                    product.put("price",productTop.getPrice());
                    product.put("vipPrice",productTop.getVip_price());
                    product.put("start",productTop.getStart());
                    proTop.add(product);
                }
            }

            productInfo.put("productList",pro);
            productInfo.put("productListTop",proTop);
        }else {
            Product product = productMapper.selectProductInfo(productId);
            List<ProductImg> productImg = productImgMapper.selectProductImgList(product.getOpen_id(),product.getProduct_id());
            List<String> img = new ArrayList<>();
            for (ProductImg productImg1 : productImg) {
                img.add(productImg1.getCover_url());
            }
            productInfo.put("productInfo",product);
            productInfo.put("coverImg",img);
        }
        return productInfo;
    }

    /**
     * 添加素材库文章
     */
    public String addArticle (Map<String,Object> data) throws JOSEException {
        //获取区分和文章
        Integer sign = (Integer) data.get("sign");
        //获取平台还是品牌
        Integer start = (Integer) data.get("start");
        //用户唯一标识
        String openId = (String) data.get("openId");
        //品牌id
        Integer brandId = (Integer) data.get("brandId");
//        String brandName = (String) data.get("brandName");
        //素材标题
        String title = (String) data.get("title");
        //简介
        String digest = (String) data.get("digest");
        //封面图片
        String coverImg = (String) data.get("coverImg");
        //阅读数量
        Integer quantity = (Integer) data.get("quantity");
        //二级分类名称
        String classify = (String) data.get("classify");

        //产品
        List<Integer> product = (List<Integer>) data.get("product");

        BrandNav brandNav = brandNavMapper.selectBrandId(brandId);
        if (brandNav == null){
            return null;
        }

        //获取当前的时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String time = df.format(new Date());


        //生成素材唯一值
        Map<String,Object> map = new HashMap<>();
        //建立载荷,这些数据根据业务,自己定义
        map.put("openId",openId);
        map.put("groupName",title);
        //生成时间
        map.put("sta", new Date().getTime());
        //过期时间
        map.put("exp", new Date().getTime() + 6);
        String token = com.example.xcschoolserver.util.token.creatToken(map);

        if (sign == 0){
            if (start == 0){
                String content = (String) data.get("content");
                String url = (String) data.get("url");
                //写入数据库
                brandArticleMapper.insertBrandArticle(openId,title,digest,coverImg,url,quantity,brandId,content,token,brandNavMapper.selectBrandId(brandId).getBrand_name(),classify,time);
                if (product.size() == 0){
                    return "保存成功";
                }
                //获取id
                Integer articleId = brandArticleMapper.selectBrandArticleId(token).getArticle_id();
                for (Integer productId : product) {
                    materialMapper.insertMaterial(articleId,0,productId);
                }
            }else if (start == 1){
                Integer like = (Integer) data.get("like");
                String downUrl = (String) data.get("downUrl");
                String popImg = (String) data.get("popImg");
                Boolean popCode = (Boolean) data.get("popCode");
                if (downUrl==null){
                    return null;
                }
                Integer pop = 0;
                if (popCode == true){
                    pop = 1;
                }

                //写入数据库
                brandVideoMapper.insertVideo(openId,title,digest,coverImg,quantity,brandId,like,token,brandNavMapper.selectBrandId(brandId).getBrand_name(),classify,time,downUrl,popImg,pop);
                if (product.size() == 0){
                    return "保存成功";
                }
                //获取Id
                Integer videoId = brandVideoMapper.selectVideoId(token).getVideo_id();
                for (Integer productId : product) {
                    materialMapper.insertMaterial(0,videoId,productId);
                }
            }
            return "保存成功";
        }else if (sign == 1){
            updataArticleInfo(data);
        }
        return "更新成功";
    }

    /**
     * 查询素材详情
     */
    public Map<String, Object> selectArticleInfo(Map<String,Object> data){
        Integer id = (Integer) data.get("id");
        Integer start = (Integer) data.get("start");
        String openId = (String) data.get("openId");
        Integer sign = (Integer) data.get("sign");
        Map<String,Object> datas = new HashMap<>();
        datas.put("userInfo",xcuserMapper.selectOpenId(openId));
        if (sign == 0){
            if (start == 0){//平台文章
                Article article = articleMapper.selectArticleId(id);
                BrandNav brandNav = brandNavMapper.selectBrandName(openId, WeChatUtil.AUTHOR);
                Map<String,Object> art = new HashMap<>();
                art.put("title",article.getTitle());
                art.put("digest",article.getDigest());
                art.put("cover_img",article.getCover_img());
                art.put("url",article.getUrl());
                art.put("quantity",0);
                art.put("brandId",brandNav.getBrand_id());
                art.put("content",article.getContent());
                art.put("author",brandNav.getBrand_name());
                datas.put("dataInfo",art);
                List a = new ArrayList();
                datas.put("poster",a);
            }else if(start == 1){//平台视频
                Video video = videoMapper.selectVideoId(id);
                BrandNav brandNav = brandNavMapper.selectBrandName(openId,WeChatUtil.AUTHOR);
                Map<String,Object> art = new HashMap<>();
                art.put("title",video.getVideo_name());
                art.put("digest",video.getDigest());
                art.put("cover_img",video.getCover_url());
                art.put("down_url",video.getDown_url());
                art.put("quantity",0);
                art.put("popImg",null);
                art.put("popCode",false);
                art.put("brandId",brandNav.getBrand_id());
                art.put("author",brandNav.getBrand_name());
                art.put("video_like",0);
                datas.put("dataInfo",art);
                List a = new ArrayList();
                datas.put("poster",a);
            }

        }else if (sign == 1){
            if (start == 0){//品牌文章
                datas.put("dataInfo",brandArticleMapper.selectArticleId(id));
                List<Material> materials = materialMapper.selectMaterialArticle(id);
                if (materials.size()!=0){
                    BrandArticle brandVideo = brandArticleMapper.selectArticleId(id);
                    if (brandVideo.getOpen_id().equals(openId)){
                        List<Map<String,Object>> productList = new ArrayList<>();
                        for (Material material : materials) {
                            Product product = productMapper.selectProductInfo(material.getProduct_id());
                            ProductImg productImg = productImgMapper.selectProductImg(product.getOpen_id(),product.getProduct_id());
                            Map<String,Object> pro = new HashMap<>();
                            pro.put("productId",product.getProduct_id());
                            pro.put("coverImg",productImg.getCover_url());
                            pro.put("productTitle",product.getProduct_title());
                            pro.put("start",product.getStart());
                            pro.put("price",product.getPrice());
                            pro.put("vipPrice",product.getVip_price());
                            productList.add(pro);
                        }
                        datas.put("poster",productList);
                    }else {
                        List a = new ArrayList();
                        datas.put("poster",a);
                    }
                }else {
                    List a = new ArrayList();
                    datas.put("poster",a);
                }
            }else if (start == 1){
                BrandVideo video = brandVideoMapper.selectVideoIds(id);
                BrandNav brandNav = brandNavMapper.selectBrandName(openId,WeChatUtil.AUTHOR);
                Map<String,Object> art = new HashMap<>();
                art.put("title",video.getTitle());
                art.put("digest",video.getDigest());
                art.put("cover_img",video.getCover_img());
                art.put("down_url",video.getDown_url());
                art.put("quantity",0);
                art.put("brandId",brandNav.getBrand_id());
                art.put("author",brandNav.getBrand_name());
                art.put("popImg",video.getPop_img());
                if (video.getPop_code() == 0){
                    art.put("popCode",false);
                }else {
                    art.put("popCode",true);
                }
                art.put("video_like",0);
                datas.put("dataInfo",art);
                List<Material> materials = materialMapper.selectMaterialVideo(id);
                if (materials.size()!=0){
                    BrandVideo brandVideo = brandVideoMapper.selectVideoIds(id);
                    if (brandVideo.getOpen_id().equals(openId)){
                        List<Map<String,Object>> productList = new ArrayList<>();
                        for (Material material : materials) {
                            Product product = productMapper.selectProductInfo(material.getProduct_id());
                            ProductImg productImg = productImgMapper.selectProductImg(product.getOpen_id(),product.getProduct_id());
                            Map<String,Object> pro = new HashMap<>();
                            pro.put("productId",product.getProduct_id());
                            pro.put("coverImg",productImg.getCover_url());
                            pro.put("productTitle",product.getProduct_title());
                            pro.put("start",product.getStart());
                            pro.put("price",product.getPrice());
                            pro.put("vipPrice",product.getVip_price());
                            productList.add(pro);
                        }
                        datas.put("poster",productList);
                    }else {
                        List a = new ArrayList();
                        datas.put("poster",a);
                    }
                }else {
                    List a = new ArrayList();
                    datas.put("poster",a);
                }

            }
        }

        return datas;
    }

    /**
     * 更新素材库文章
     */
    public String updataArticleInfo(Map<String,Object> data){
        //获取区分分账和视频
        Integer start = (Integer) data.get("start");
        //素材主键id
        Integer id = (Integer) data.get("id");
//        String openId = (String) data.get("openId");
        //素材品牌id
        Integer brandId = (Integer) data.get("brandId");
        // 素材标题
        String title = (String) data.get("title");
        //素材描述
        String digest = (String) data.get("digest");
//        String coverImg = (String) data.get("coverImg");
        //素材阅读数量
        Integer quantity = (Integer) data.get("quantity");

        String classify = (String) data.get("classify");
        //素材品牌
        List<Integer> product = (List<Integer>) data.get("product");

        if (start == 0){
            //素材内容
            String content = (String) data.get("content");
            //素材原文地址
            String url = (String) data.get("url");

            brandArticleMapper.updataBrandArticleInfo(id,title,digest,url,quantity,brandId,content,brandNavMapper.selectBrandId(brandId).getBrand_name(),classify);

            materialMapper.deleteMaterialArticle(id);
            for (Integer productId : product) {
                materialMapper.insertMaterial(id ,0,productId);
            }
        }else if (start == 1){
            Integer like = (Integer) data.get("like");
            String popImg = (String) data.get("popImg");
            Boolean popCode = (Boolean) data.get("popCode");
            Integer pop = 0;
            if (popCode == true){
                pop = 1;
            }

            brandVideoMapper.updataBrandVideoInfo(id,title,digest,quantity,brandId,like,brandNavMapper.selectBrandId(brandId).getBrand_name(),classify,popImg,pop);
            materialMapper.deleteMaterialVideo(id);
            for (Integer productId : product) {
                materialMapper.insertMaterial(0,id,productId);
            }
        }
        return "更新成功";
    }

    /**
     * 查询品牌文章列表
     */
    public Map<String,Object> selectArticleList(Map<String,Object> data){
        //文章视频标识,0为文章,1为视频,2为课程
        Integer start = (Integer) data.get("start");
        //页码
        Integer pag = (Integer) data.get("pag")*10;
        //品牌名称
        String brandName = (String) data.get("brandName");

        //用户唯一标识
        String openId = (String) data.get("openId");

        //品牌二级分类名称
        String classify = (String) data.get("classify");

        //创建空数组用于返回数据
        Map<String,Object> dataList = new HashMap<>();

        //判断文章还是视频
        if (start == 0){//查询文章
            List<BrandArticle> brandArticles = new ArrayList<>();
            //按照品牌分类查询
            if (classify.equals("全部") || brandName.equals(WeChatUtil.AUTHOR)){
                brandArticles = brandArticleMapper.selectArticleBrands(brandName,pag);

            }else {
                brandArticles = brandArticleMapper.selectArticleBrand(brandName,classify,pag);

            }

            if (brandArticles.size() == 0){
                return null;
            }
            List<Map<String,Object>> contentList = new ArrayList<>();
            //整理数据
            for (BrandArticle brandArticle : brandArticles) {
                Map<String,Object> content = new HashMap<>();
                content.put("article_id",brandArticle.getArticle_id());
                content.put("title",brandArticle.getTitle());
                content.put("cover_img",brandArticle.getCover_img());
                content.put("quantity",brandArticle.getQuantity());
                content.put("brand_id",brandArticle.getBrand_id());
                content.put("article_time",brandArticle.getPublish_time());
                content.put("classify",brandArticle.getClassify());
                content.put("price",0.00);
                contentList.add(content);
            }

            dataList.put("articleList",contentList);
            dataList.put("start",0);
        }else if (start == 1){
            List<BrandVideo> brandVideos = new ArrayList<>();
            if (classify.equals("全部") || brandName.equals(WeChatUtil.AUTHOR)){
                brandVideos = brandVideoMapper.selectBrandNames(brandName,pag);

            }else {
                brandVideos = brandVideoMapper.selectBrandName(brandName,classify,pag);

            }

            if (brandVideos.size() == 0){
                return null;
            }
            List<Map<String,Object>> contentList = new ArrayList<>();
            //整理数据
            for (BrandVideo brandArticle : brandVideos) {
                Map<String,Object> content = new HashMap<>();
                content.put("article_id",brandArticle.getVideo_id());
                content.put("title",brandArticle.getTitle());
                content.put("cover_img",brandArticle.getCover_img());
                content.put("quantity",brandArticle.getQuantity());
                content.put("brand_id",brandArticle.getBrand_id());
                content.put("article_time",brandArticle.getVideo_time());
                content.put("classify",brandArticle.getClassify());
                content.put("sign",1);
                content.put("price",0.00);
                contentList.add(content);
            }
            dataList.put("videoList",contentList);
            dataList.put("start",1);
        }else if (start == 2){
            //查询课程
            List<BrandCourse> brandCourses = new ArrayList<>();
            if (classify.equals("全部") || brandName.equals(WeChatUtil.AUTHOR)){
                brandCourses = brandCourseMapper.selectBrandName(brandName,pag);
            }else {
                brandCourses = brandCourseMapper.selectClassify(brandName,classify,pag);
            }
            //如果查询没有课程时
            if (brandCourses.size() == 0){
                return null;
            }
            Xcuser xcuser = xcuserMapper.selectOpenId(openId);
            //创建list返回数据
            List<Map<String,Object>> contentList = new ArrayList<>();

            for (BrandCourse brandCours : brandCourses) {
                Map<String,Object> content = new HashMap<>();
                content.put("id",brandCours.getCourse_id());
                content.put("title",brandCours.getTitle());
                content.put("cover_img",brandCours.getCover_img());
                content.put("quantity",brandCours.getQuantity());
                content.put("brand_id",brandCours.getBrand_id());
                content.put("article_time",brandCours.getPublish_time());
                content.put("classify",brandCours.getClassify());
                content.put("tell",brandCours.getTell());
                content.put("price",brandCours.getPrice());
                contentList.add(content);
            }
            dataList.put("courseList",contentList);
            dataList.put("start",2);
        }
        return dataList;
    }

    /**
     * 查询我的素材
     * @param data
     * @return
     */
//    public Map<String,Object> selectArticle(Map<String,Object> data){
//        Integer start = (Integer) data.get("start");
//        Integer pag = (Integer) data.get("pag")*10;
//        String openId = (String) data.get("openId");
//        String brandName = (String) data.get("brandName");
//        String classify = (String) data.get("classify");
//
//        Map<String,Object> dataList = new HashMap<>();
//        //判断文章还是视频
//        if (start == 0){//查询文章
//            List<BrandArticle> brandArticles = new ArrayList<>();
//            //按照品牌分类查询
//            if (brandName.equals(WeChatUtil.AUTHOR)){
//                brandArticles = brandArticleMapper.selectArticleBrandsOpenId(openId,brandName,pag);
//            }else if (classify.equals("全部")){
//                brandArticles = brandArticleMapper.selectArticleBrandsOpenId(openId,brandName,pag);
//
//            } else if (brandName != WeChatUtil.AUTHOR && classify != null){
//                brandArticles = brandArticleMapper.selectArticleBrandOpenId(brandName,classify,pag,openId);
//
//            }
//
//            if (brandArticles.size() == 0){
//                return null;
//            }
//            List<Map<String,Object>> contentList = new ArrayList<>();
//            //整理数据
//            for (BrandArticle brandArticle : brandArticles) {
//                Map<String,Object> content = new HashMap<>();
//                content.put("article_id",brandArticle.getArticle_id());
//                content.put("title",brandArticle.getTitle());
//                content.put("cover_img",brandArticle.getCover_img());
//                content.put("quantity",brandArticle.getQuantity());
//                content.put("brand_id",brandArticle.getBrand_id());
//                content.put("article_time",brandArticle.getPublish_time());
//                content.put("classify",brandArticle.getClassify());
//                content.put("price",0.00);
//                contentList.add(content);
//            }
//
//            dataList.put("articleList",contentList);
//            dataList.put("start",0);
//        }else if (start == 1){
//            List<BrandVideo> brandVideos = new ArrayList<>();
//            if (classify.equals("全部") || brandName.equals(WeChatUtil.AUTHOR)){
//                brandVideos = brandVideoMapper.selectBrandNamesOpenId(brandName,pag,openId);
//            }else if (brandName != WeChatUtil.AUTHOR && classify != null){
//                brandVideos = brandVideoMapper.selectBrandNameOpenId(brandName,classify,pag,openId);
//
//            }
//
//            if (brandVideos.size() == 0){
//                return null;
//            }
//            List<Map<String,Object>> contentList = new ArrayList<>();
//            //整理数据
//            for (BrandVideo brandArticle : brandVideos) {
//                Map<String,Object> content = new HashMap<>();
//                content.put("article_id",brandArticle.getVideo_id());
//                content.put("title",brandArticle.getTitle());
//                content.put("cover_img",brandArticle.getCover_img());
//                content.put("quantity",brandArticle.getQuantity());
//                content.put("brand_id",brandArticle.getBrand_id());
//                content.put("article_time",brandArticle.getVideo_time());
//                content.put("classify",brandArticle.getClassify());
//                content.put("price",0.00);
//                contentList.add(content);
//            }
//            dataList.put("videoList",contentList);
//            dataList.put("start",1);
//        }else if (start == 2){
//            //查询课程
//            List<BrandCourse> brandCourses = new ArrayList<>();
//            if (classify.equals("全部") || brandName.equals(WeChatUtil.AUTHOR)){
//                brandCourses = brandCourseMapper.selectBrandNames(brandName,pag,openId);
//            }else {
//                brandCourses = brandCourseMapper.selectClassifys(brandName,classify,pag,openId);
//            }
//            //如果查询没有课程时
//            if (brandCourses.size() == 0){
//                return null;
//            }
//            Xcuser xcuser = xcuserMapper.selectOpenId(openId);
//            //创建list返回数据
//            List<Map<String,Object>> contentList = new ArrayList<>();
//
//            for (BrandCourse brandCours : brandCourses) {
//                Map<String,Object> content = new HashMap<>();
//                content.put("id",brandCours.getCourse_id());
//                content.put("title",brandCours.getTitle());
//                content.put("cover_img",brandCours.getCover_img());
//                content.put("quantity",brandCours.getQuantity());
//                content.put("brand_id",brandCours.getBrand_id());
//                content.put("article_time",brandCours.getPublish_time());
//                content.put("classify",brandCours.getClassify());
//                content.put("price",brandCours.getPrice());
//                contentList.add(content);
//            }
//            dataList.put("courseList",contentList);
//            dataList.put("start",2);
//        }
//        return dataList;
//    }

    public Map<String,Object> selectArticle(Map<String,Object> data){
        Integer start = (Integer) data.get("start");
        Integer pag = (Integer) data.get("pag")*10;
        String openId = (String) data.get("openId");
        String brandName = (String) data.get("brandName");
        String classify = (String) data.get("classify");

        Map<String,Object> dataList = new HashMap<>();
        //判断文章还是视频
        if (start == 0){//查询文章
            List<BrandArticle> brandArticles = new ArrayList<>();
            //按照品牌分类查询
            if (brandName.equals(WeChatUtil.AUTHOR)){
                brandArticles = brandArticleMapper.selectArticleBrandsOpenId(openId,brandName,pag);
            }else if (classify.equals("全部")){
                brandArticles = brandArticleMapper.selectArticleBrandsOpenId(openId,brandName,pag);

            } else if (brandName != WeChatUtil.AUTHOR && classify != null){
                brandArticles = brandArticleMapper.selectArticleBrandOpenId(brandName,classify,pag,openId);

            }

            if (brandArticles.size() == 0){
                return null;
            }
            List<Map<String,Object>> contentList = new ArrayList<>();
            //整理数据
            for (BrandArticle brandArticle : brandArticles) {
                Map<String,Object> content = new HashMap<>();
                content.put("article_id",brandArticle.getArticle_id());
                content.put("title",brandArticle.getTitle());
                content.put("cover_img",brandArticle.getCover_img());
                content.put("quantity",brandArticle.getQuantity());
                content.put("brand_id",brandArticle.getBrand_id());
                content.put("article_time",brandArticle.getPublish_time());
                content.put("classify",brandArticle.getClassify());
                content.put("price",0.00);
                contentList.add(content);
            }

            dataList.put("articleList",contentList);
            dataList.put("start",0);
        }else if (start == 1){
            List<BrandVideo> brandVideos = new ArrayList<>();
            if (classify.equals("全部") || brandName.equals(WeChatUtil.AUTHOR)){
                brandVideos = brandVideoMapper.selectBrandNamesOpenId(brandName,pag,openId);
            }else if (brandName != WeChatUtil.AUTHOR && classify != null){
                brandVideos = brandVideoMapper.selectBrandNameOpenId(brandName,classify,pag,openId);

            }

            if (brandVideos.size() == 0){
                return null;
            }
            List<Map<String,Object>> contentList = new ArrayList<>();
            //整理数据
            for (BrandVideo brandArticle : brandVideos) {
                Map<String,Object> content = new HashMap<>();
                content.put("article_id",brandArticle.getVideo_id());
                content.put("title",brandArticle.getTitle());
                content.put("cover_img",brandArticle.getCover_img());
                content.put("quantity",brandArticle.getQuantity());
                content.put("brand_id",brandArticle.getBrand_id());
                content.put("article_time",brandArticle.getVideo_time());
                content.put("classify",brandArticle.getClassify());
                content.put("price",0.00);
                contentList.add(content);
            }
            dataList.put("videoList",contentList);
            dataList.put("start",1);
        }else if (start == 2){
            //查询课程
            List<BrandCourse> brandCourses = new ArrayList<>();
            if (classify.equals("全部") || brandName.equals(WeChatUtil.AUTHOR)){
                brandCourses = brandCourseMapper.selectBrandNames(brandName,pag,openId);
            }else {
                brandCourses = brandCourseMapper.selectClassifys(brandName,classify,pag,openId);
            }
            //如果查询没有课程时
            if (brandCourses.size() == 0){
                return null;
            }
            Xcuser xcuser = xcuserMapper.selectOpenId(openId);
            //创建list返回数据
            List<Map<String,Object>> contentList = new ArrayList<>();

            for (BrandCourse brandCours : brandCourses) {
                Map<String,Object> content = new HashMap<>();
                content.put("id",brandCours.getCourse_id());
                content.put("title",brandCours.getTitle());
                content.put("cover_img",brandCours.getCover_img());
                content.put("quantity",brandCours.getQuantity());
                content.put("brand_id",brandCours.getBrand_id());
                content.put("article_time",brandCours.getPublish_time());
                content.put("classify",brandCours.getClassify());
                content.put("price",brandCours.getPrice());
                contentList.add(content);
            }
            dataList.put("courseList",contentList);
            dataList.put("start",2);
        }
        return dataList;
    }

    /**
     * 更新文章视频品牌归属
     */
    public String updataArticle(Map<String,Object> data){
        Integer start = (Integer) data.get("start");
        Integer id = (Integer) data.get("id");
        String brandName = (String) data.get("brandName");
        String classify = (String) data.get("classify");
        String openId = (String) data.get("openId");

        Integer brandId = brandNavMapper.selectBrandName(openId,brandName).getBrand_id();
        if (start == 0){
            brandArticleMapper.updataBrandName(id,brandId,brandName,classify);
        }else if (start == 1){
            brandVideoMapper.updataBrandVideo(id,brandId,brandName,classify);
        }else if (start == 2){
            brandCourseMapper.updataBrandName(id,brandId,brandName,classify);
        }
        return "更新成功";
    }

    /**
     * 删除素材库文章和视频
     */
    public String deleteBrand(Map<String,Object> data){
        Integer id = (Integer) data.get("id");
        Integer start = (Integer) data.get("start");


        if (start == 0){
            brandArticleMapper.deleteBrandArticle(id);
        }else if (start == 1){
            BrandVideo brandVideo = brandVideoMapper.selectVideoIds(id);
            if (brandVideo.getDown_url().indexOf("poster") != -1){
                String url = brandVideo.getDown_url();
                String[] a = url.split("/");
                File file = new File("/poster/userVideo/"+a[5]);
                file.delete();
            }
            brandVideoMapper.deleteBrandVideo(id);
        }
        return "删除成功";
    }


    /**
     * 客户留言
     */
    public String insertLeave(Map<String,Object> data){
        Integer brandId = (Integer) data.get("brandId");
        String openId = (String) data.get("openId");
        String phone = (String) data.get("phone");
        String username = (String) data.get("username");
        String textInfo = (String) data.get("textInfo");
        if (brandId == null && openId == null && phone == null && username == null){
            return null;
        }
        leaveMapper.insertLeave(username,phone,brandId,openId,textInfo);
        return "留言成功";
    }


    /**
     * 写入/更新弹框图片
     */
    public List<Map<String,Object>> insertPop(Map<String,Object> data){
        //获取用户openId
        String openId = (String) data.get("openId");
        //获取图片地址
        String popimg = (String) data.get("popimg");;
        //将图片写入数据库
        popImageMapper.insertPopimg(openId,popimg);

        return selectPop(data);

    }

    /**
     * 查询弹框图片列表
     */
    public List<Map<String,Object>> selectPop(Map<String,Object> data){
        //获取openid
        String openId = (String) data.get("openId");
        //到数据库查询
        List<PopImage> popImages = popImageMapper.selectPop(openId);
        List<Map<String,Object>> dataList = new ArrayList<>();
        for (PopImage popImage : popImages) {
            Map<String,Object> pop = new HashMap<>();
            pop.put("id",popImage.getPop_id());
            pop.put("imageUrl",popImage.getImage_url());
            dataList.add(pop);
        }
        return dataList;
    }

    /**
     * 删除弹框图片
     */
    public List<Map<String,Object>> deletePop(Map<String,Object> data){
        //获取openid
        String openId = (String) data.get("openId");
        //获取删除图片的id
        List<Integer> popIds = (List<Integer>) data.get("popIds");
        if (popIds.size() == 0){
            return null;
        }

        //根据id删除弹框图片
        List<String> a = new ArrayList<>();
        for (Integer popId : popIds) {
            //根据id查询图片路径
            PopImage popImage = popImageMapper.selectPopId(popId);
            String[] b =  popImage.getImage_url().split("/");
            a.add(b[3]+"/"+b[4]);
            popImageMapper.deletePop(popId);
        }
        //将图片在oss中删除
        AliOssUtil.deleteObjects(WeChatUtil.BUCKETNAME,a);


        return selectPop(data);
    }

    /**
     * 操作删除oss图片
     */
    public String deleteOss(Map<String,Object> data){
        List<String> images = (List<String>) data.get("images");
        List<String> a = new ArrayList<>();
//        String str = "https://xiaocisw-video.oss-cn-chengdu.aliyuncs.com/";
        String str = "http://xiaocisw-video.oss-accelerate.aliyuncs.com/";
        for (String image : images) {
            if (image == null){
                continue;
            }
            String imgUrl = image.replaceAll(str,"");
            a.add(imgUrl);
        }
        //将图片在oss中删除
//        System.out.println(a);
        AliOssUtil.deleteObjects(WeChatUtil.BUCKETNAME,a);
        return "删除成功";
    }


    /**
     * H5登录
     */
    public String insertH5(Map<String,Object> data){
        //获取openid
        String openId = (String) data.get("openId");
        //获取名字
        String username = (String) data.get("username");
        //获取公司名称
        String company = (String) data.get("company");
        //获取微信号
        String weChat = (String) data.get("weChat");
        //获取邮箱
        String email = (String) data.get("email");
        //获取电话
        String phone = (String) data.get("phone");

        //根据openid将用户信息写入数据库
        int a = xcuserMapper.updataUserH5(openId,username,company,weChat,email,phone,1);
        if (a > 0){
            return "写入成功";
        }
        return null;
    }

    /**
     * 热门老师
     * @return
     */
    public List topTeacher(){
        List<BrandNav> brandNavs = brandNavMapper.selectTopTeacher();
        List<Map<String,Object>> dataList = new ArrayList<>();
        for (BrandNav brandNav : brandNavs) {
            Teacher teacher = teacherMapper.selectTeacher(brandNav.getOpen_id());
            Map<String,Object> data = new HashMap<>();
            data.put("id",brandNav.getBrand_id());
            data.put("name",brandNav.getBrand_name());
            data.put("teacherImg",teacher.getTeacher_img());
            dataList.add(data);
        }
        return dataList;
    }

    public List topCourse(){
        //查询平台可以热门课程
        List<Course> courses = courseMapper.selectTopCourse();
        //查询品牌视频
        List<BrandCourse> brandCourses = brandCourseMapper.selectTopBrandCourse();
        if (courses.size() == 0 && brandCourses.size() == 0){
            return null;
        }else if (brandCourses.size() == 0){
            List<Map<String,Object>> dataList = new ArrayList<>();
            for (Course course : courses) {
                Map<String,Object> data = new HashMap<>();
                data.put("id",course.getCourse_id());
                data.put("title",course.getTitle());
                data.put("coverImg",course.getCover_img());
                data.put("start",0);
                dataList.add(data);
            }
            return dataList;
        }else if (courses.size() == 0){
            List<Map<String,Object>> dataList = new ArrayList<>();
            for (BrandCourse course : brandCourses) {
                Map<String,Object> data = new HashMap<>();
                data.put("id",course.getCourse_id());
                data.put("title",course.getTitle());
                data.put("coverImg",course.getCover_img());
                data.put("start",1);
                dataList.add(data);
            }
            return dataList;
        }


        List<Map<String,Object>> dataList = new ArrayList<>();
        for (Course course : courses) {
            Map<String,Object> data = new HashMap<>();
            data.put("id",course.getCourse_id());
            data.put("title",course.getTitle());
            data.put("coverImg",course.getCover_img());
            data.put("start",0);
            dataList.add(data);
        }
        for (BrandCourse brandCourse : brandCourses) {
            Map<String,Object> data = new HashMap<>();
            data.put("id",brandCourse.getCourse_id());
            data.put("title",brandCourse.getTitle());
            data.put("coverImg",brandCourse.getCover_img());
            data.put("start",1);
            dataList.add(data);
        }

        return dataList;
    }

    public List topArticle(){
        //查询平台可以热门课程
        List<Article> articles = articleMapper.selectSpecialArticle(0,6);
        //查询品牌视频
        List<BrandArticle> brandCourses = brandArticleMapper.selectSpecialArticle(6);
        if (articles.size() == 0 && brandCourses.size() == 0){
            return null;
        }else if (brandCourses.size() == 0){
            List<Map<String,Object>> dataList = new ArrayList<>();
            for (Article course : articles) {
                Map<String,Object> data = new HashMap<>();
                data.put("id",course.getId());
                data.put("title",course.getTitle());
                data.put("start",0);
                dataList.add(data);
            }
            return dataList;
        }else if (articles.size() == 0){
            List<Map<String,Object>> dataList = new ArrayList<>();
            for (BrandArticle course : brandCourses) {
                Map<String,Object> data = new HashMap<>();
                data.put("id",course.getArticle_id());
                data.put("title",course.getTitle());
                data.put("start",1);
                dataList.add(data);
            }
            return dataList;
        }


        List<Map<String,Object>> dataList = new ArrayList<>();
        for (Article course : articles) {
            Map<String,Object> data = new HashMap<>();
            data.put("id",course.getId());
            data.put("title",course.getTitle());
            data.put("start",0);
            dataList.add(data);
        }
        for (BrandArticle brandCourse : brandCourses) {
            Map<String,Object> data = new HashMap<>();
            data.put("id",brandCourse.getArticle_id());
            data.put("title",brandCourse.getTitle());
            data.put("start",1);
            dataList.add(data);
        }

        return dataList;
    }
}
