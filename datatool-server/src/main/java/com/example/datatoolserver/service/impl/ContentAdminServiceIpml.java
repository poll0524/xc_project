package com.example.datatoolserver.service.impl;

import com.example.datatoolserver.common.ReturnVO;
import com.example.datatoolserver.common.WeChatUtil;
import com.example.datatoolserver.mapper.*;
import com.example.datatoolserver.pojo.*;
import com.example.datatoolserver.service.IContentAdminService;
import com.example.datatoolserver.util.MD5Util;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ContentAdminServiceIpml implements IContentAdminService {
    @Autowired
    private articleAdminMapper articleAdminMapper;
    @Autowired
    private videoAdminMapper videoAdminMapper;
    @Autowired
    private adminUserMapper adminUserMapper;
    @Autowired
    private articleMapper articleMapper;
    @Autowired
    private videoMapper videoMapper;
    @Autowired
    private articleClassifyMapper articleClassifyMapper;
    @Autowired
    private xcuserMapper xcuserMapper;
    @Autowired
    private vipOredermapper vipOredermapper;
    @Autowired
    private brandNavMapper brandNavMapper;
    @Autowired
    private brandArticleMapper brandArticleMapper;
    @Autowired
    private brandVideoMapper brandVideoMapper;
    @Autowired
    private shareUserMapper shareUserMapper;
    @Autowired
    private matterDataMapper matterDataMapper;
    @Autowired
    private courseMapper courseMapper;
    @Autowired
    private brandCourseMapper brandCourseMapper;
    @Autowired
    private brandClassifyMapper brandClassifyMapper;



    /**
     * 品牌素材分类
     */
    public List<Map<String,Object>> brandClassify(){
        //查询所有品牌
        List<BrandNav> brandNavs = brandNavMapper.selectBrandNav();
        List<Map<String,Object>> datas = new ArrayList<>();
        //整理数据
        for (BrandNav brandNav : brandNavs) {
            Map<String,Object> data = new HashMap<>();
            List<BrandClassify>brandClassifies = brandClassifyMapper.selectBrandClassify(brandNav.getBrand_id());
            data.put("brandId",brandNav.getBrand_id());
            data.put("openId",brandNav.getOpen_id());
            data.put("brandName",brandNav.getBrand_name());
            if (brandClassifies.size() == 0){
                data.put("brandClassify","");
            }else {
                List<Map<String,Object>> a = new ArrayList<>();
                for (BrandClassify brandClassify : brandClassifies) {
                    Map<String,Object> b = new HashMap<>();
                    b.put("classifyId",brandClassify.getClassify_id());
                    b.put("brandName",brandClassify.getBrand_classify_name());
                    a.add(b);
                }
                data.put("brandClassify",a);
            }
            datas.add(data);
        }
        return datas;
    }


    /**
     * 文章编辑
     */
    public String insertArticle(Map<String,Object> data,String userToken) throws JOSEException {
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser == null){
            return null;
        }
//        List<String> delectImgs = (List<String>) data.get("delectImg");
//        System.out.println(delectImgs);
//        if (delectImgs.size() != 0 ){
//            for (String delectImg : delectImgs) {
//                Map c = new HashMap();
//                c.put("url",delectImg);
//                deleteImage(c);
//            }
//        }
        Integer start = Integer.parseInt((String) data.get("start"));
        String title = (String) data.get("title");
        String digest = (String) data.get("digest");
        String content = (String) data.get("content");
        String coverImg = (String) data.get("coverImg");
        Integer quantity = Integer.parseInt((String) data.get("quantity"));
        String author = WeChatUtil.AUTHOR;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());


        //生成素材唯一值
        Map<String,Object> maps = new HashMap<>();
        //建立载荷,这些数据根据业务,自己定义
        maps.put("title",title);
        maps.put("digest",digest);
        //生成时间
        maps.put("sta", new Date().getTime());
        //过期时间
        maps.put("exp", new Date().getTime() + 6);
        String thumbMediaId = com.example.datatoolserver.util.token.creatToken(maps);

        //将文章写入数据库
//        int a = articleAdminMapper.insertArticle(title,thumbMediaId,author,digest,content,coverImg,classify,0,quantity);
        int a = 0 ;
        if (start == 0){
            String classify = (String) data.get("classify");
            Integer classifyId = articleClassifyMapper.selectNameArticle(classify).getClassify_id();
            a = articleMapper.insertArticleAdmin(title,thumbMediaId,author,digest,content,null,coverImg,"0" ,classifyId,quantity,time);
        }else if (start == 1){
            String classify = (String) data.get("classify");
            if (classify.equals("全部")){
                classify = null;
            }
            String brandName = (String) data.get("brandName");
            Integer brandId = (Integer) data.get("brandId");
            BrandNav brandNav = brandNavMapper.selectBrandId(brandId);
            a = brandArticleMapper.insertBrandArticle(brandNav.getOpen_id(),title,digest,coverImg,null,quantity,brandId,content,thumbMediaId,brandName,classify,time);
        }

        if (a != 0){
            return "添加成功";
        }
        return "添加失败";
    }

    /**
     * 文章更新
     */
    public String updataArticle(Map<String,Object> data,String userToken){
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser == null){
            return null;
        }
        Integer start = Integer.parseInt((String) data.get("start"));
        Integer id = Integer.parseInt((String) data.get("id"));
        String title = (String) data.get("title");
        String digest = (String) data.get("digest");
        String content = (String) data.get("content");
        String coverImg = (String) data.get("coverImg");
        Integer quantity = Integer.parseInt((String) data.get("quantity"));

        String author = WeChatUtil.AUTHOR;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());
        int a = 0;
        if(start == 0){
            String classify = (String) data.get("classify");
            Integer classifyId = articleClassifyMapper.selectNameArticle(classify).getClassify_id();
            a = articleMapper.updataArticleAdmin(id,title,author,digest,content,coverImg,"0",classifyId,quantity,time);
        }else {
            String classify = (String) data.get("classify");
            if (classify.equals("全部")){
                classify = null;
            }
            String brandName = (String) data.get("brandName");
            Integer brandId = (Integer) data.get("brandId");
            BrandNav brandNav = brandNavMapper.selectBrandId(brandId);
            a = brandArticleMapper.updataBrandArticleInfos(id,brandNav.getOpen_id(),title,digest,null,quantity,brandId,content,brandName,classify);
        }
        if (a == 0){
            return "更新失败";
        }
        return "更新成功";
    }

    /**
     * 更新视频
     */
    public String updataVideo(Map<String,Object> data,String userToken){
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser == null){
            return null;
        }
        Integer start = Integer.parseInt((String) data.get("start"));
        Integer id = Integer.parseInt((String) data.get("id"));
        String title = (String) data.get("title");
        String digest = (String) data.get("digest");
        String coverImg = (String) data.get("coverImg");
        String downUrl = (String) data.get("downUrl");
        Integer quantity = Integer.parseInt((String) data.get("quantity"));
        Integer videoLike = Integer.parseInt((String) data.get("videoLike"));
        String author = WeChatUtil.AUTHOR;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());
        int a = 0;
        if(start == 0){
            String classify = (String) data.get("classify");
            Integer classifyId = articleClassifyMapper.selectNameArticle(classify).getClassify_id();
            a = videoMapper.updataVideos(id,title,coverImg,downUrl,classifyId,quantity,digest,videoLike,author,"0",time);
        }else {
            String classify = (String) data.get("classify");
            if (classify.equals("全部")){
                classify = null;
            }
            String brandName = (String) data.get("brandName");
            Integer brandId = (Integer) data.get("brandId");
            BrandNav brandNav = brandNavMapper.selectBrandId(brandId);
            a = brandVideoMapper.updataBrandVideoInfos(id,brandNav.getOpen_id(),title,digest,quantity,brandId,videoLike,brandName,classify,null,0,downUrl);
        }
        if (a == 0){
            return "更新失败";
        }
        return "更新成功";
    }


    /**
     * 视频编辑
     */
    public String insertVideo(Map<String,Object> data,String userToken) throws JOSEException {
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser == null){
            return null;
        }
//        List<String> delectImgs = (List<String>) data.get("delectImg");
//        System.out.println(delectImgs);
//        if (delectImgs.size() != 0 ){
//            for (String delectImg : delectImgs) {
//                Map c = new HashMap();
//                c.put("url",delectImg);
//                deleteImage(c);
//            }
//        }
        Integer start = Integer.parseInt((String) data.get("start"));
        String title = (String) data.get("title");
        String digest = (String) data.get("digest");
        String coverImg = (String) data.get("coverImg");
        String downUrl = (String) data.get("downUrl");
        Integer quantity = Integer.parseInt((String) data.get("quantity"));
        Integer videoLike = Integer.parseInt((String) data.get("videoLike"));
        String author = WeChatUtil.AUTHOR;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());

        //生成素材唯一值
        Map<String,Object> maps = new HashMap<>();
        //建立载荷,这些数据根据业务,自己定义
        maps.put("title",title);
        maps.put("digest",digest);
        //生成时间
        maps.put("sta", new Date().getTime());
        //过期时间
        maps.put("exp", new Date().getTime() + 6);
        String thumbMediaId = com.example.datatoolserver.util.token.creatToken(maps);


        //将视频存入数据
//        int a = videoAdminMapper.insertVideo(title,coverImg,downUrl,classify,quantity,digest,videoLike,author,0);

        int a = 0 ;
        if (start == 0){
            String classify = (String) data.get("classify");
            Integer classifyId = articleClassifyMapper.selectNameArticle(classify).getClassify_id();
            a = videoMapper.insertVideos(title,coverImg,downUrl,classifyId,quantity,digest,videoLike,author,null,"0",thumbMediaId,time);
        }else if (start == 1){
            String classify = (String) data.get("classify");
            if (classify.equals("全部")){
                classify = null;
            }
            String brandName = (String) data.get("brandName");
            Integer brandId = (Integer) data.get("brandId");
            BrandNav brandNav = brandNavMapper.selectBrandId(brandId);
            a = brandVideoMapper.insertVideo(brandNav.getOpen_id(),title,digest,coverImg,quantity,brandId,videoLike,thumbMediaId,brandName,classify,time,downUrl,null,0);
        }

        if (a == 0){
            return "添加失败";
        }
        return "添加成功";
    }



    /**
     * 登录
     */
    public Map<String, Object> selectLogin(Map<String,Object> data) throws JOSEException {
        String userName = (String) data.get("userName");
        String password = (String) data.get("pwd");
        AdminUser adminUser = new AdminUser();
        if (password.equals("123456")){//如果密码等于123456
            //到数据库进行匹配
            adminUser = adminUserMapper.selectAdminUser(userName,password);

        }else {
            //将密码进行md5加密进行验证
            String pwd = MD5Util.encode("userName="+userName+"password="+password).toUpperCase();
            adminUser = adminUserMapper.selectAdminUser(userName,pwd);
        }

        if (adminUser != null){//adminUser不等于null说明账号密码匹配登录成功
            //根据账户和密码及时间计算token,返回数据
            //生成素材唯一值
            Map<String,Object> maps = new HashMap<>();
            //建立载荷,这些数据根据业务,自己定义
            maps.put("userName",userName);
            maps.put("password",password);
            //生成时间
            maps.put("sta", new Date().getTime());
            //过期时间
            maps.put("exp", new Date().getTime() + 6);
            String token = com.example.datatoolserver.util.token.creatToken(maps);

            //将token更新到数据库
            adminUserMapper.updataToken(userName,token);
            Map<String,Object> datainfo = new HashMap<>();
            datainfo.put("name",adminUser.getAdmin_name());
            datainfo.put("start",adminUser.getStart());
            datainfo.put("token",token);
            if (adminUser.getStart() == 3){
                datainfo.put("openId",adminUser.getOpen_id());
            }else {
                datainfo.put("openId","");
            }
            return datainfo;
        }
        return null;
    }

    /**
     * 修改密码
     */
    public String updataPwd(Map<String,Object> data,String userToken){
        String userName = (String) data.get("userName");
        String oldPwd = (String) data.get("oldPwd");
        String password = (String) data.get("pwd");
        String token = selectTokens(userToken);
        if(token == null){
            return "token错误";
        }
        AdminUser adminUser = new AdminUser();
        //判断密码是否为初始密码
        if (oldPwd.equals("123456")){
            //到数据库进行匹配
            adminUser = adminUserMapper.selectAdminUser(userName,oldPwd);
        }else {
            //将密码进行md5加密进行验证
            oldPwd = MD5Util.encode("userName="+userName+"password="+oldPwd).toUpperCase();
            adminUser = adminUserMapper.selectAdminUser(userName,oldPwd);
        }
        //根据用户名和旧密码查询是否正确
        if (adminUser != null){//adminUser不为空说明用户名密码正确
            //将新密码更新到数据库
            password = MD5Util.encode("userName="+userName+"password="+password).toUpperCase();
            adminUserMapper.updataPwd(userName,password);
            return "密码修改成功";
        }
        return null;
    }


    /**
     * 发布素材
     */
    public String updataContent(Map<String,Object> data,String userToken){
        Integer start = Integer.parseInt((String) data.get("start"));
        Integer id = (Integer) data.get("id");
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser == null){
            return null;
        }
        if (start == 0){
            articleMapper.updataShowCoverPic(id,"1");
        }else if(start == 1){
            videoMapper.updataShowCoverPic(id,"1");
        }
        return "修改成功";
    }
    /**
     * 查询未发布素材
     */
    public Map<String,Object> selectContent(Map<String,Object> data){
        Integer classifyId = (Integer) data.get("classifyId");
        Integer start = Integer.parseInt((String) data.get("start"));
        Integer pag = Integer.parseInt((String) data.get("pag"))*10;

        Map<String,Object> datalist = new HashMap<>();
        List<Map<String,Object>> datalists= new ArrayList<>();
        if (start == 0){
            List<Article> articles = new ArrayList<>();
            List<Article> articleList = new ArrayList<>();
            if (classifyId == 0){
                articles = articleMapper.selectTitleAdmin(pag,0);
                articleList = articleMapper.selectArticle("0");
            }else {
                articles = articleMapper.selectTitleAdmins(pag,classifyId,0);
                articleList = articleMapper.selectArticles(classifyId,"0");
            }
            for (Article article : articles) {
                Map<String,Object> datas = new HashMap<>();
                datas.put("id",article.getId());
                datas.put("title",article.getTitle());
                datas.put("coverImg",article.getCover_img());
                datas.put("publishTime",article.getPublish_time());
                datas.put("classify",articleClassifyMapper.selectIdArticle(article.getArticle_classify()).getClassify_name());
                datalists.add(datas);
            }
            datalist.put("dataInfo",datalists);
            datalist.put("articleQuantity",articleList.size());
        }else if (start == 1){
            List<Video> videos = new ArrayList<>();
            List<Video> videoList = new ArrayList<>();
            if (classifyId == 0){
                videos = videoMapper.selectVideoTieleAdmin(pag,0);
                videoList = videoMapper.selectVideos("0");
            }else {
                videos = videoMapper.selectVideoTieleAdmins(pag,classifyId,0);
                videoList = videoMapper.selectVideoss(classifyId,"0");
            }
            for (Video video : videos) {
                Map<String,Object> datas = new HashMap<>();
                datas.put("id",video.getVideo_id());
                datas.put("title",video.getVideo_name());
                datas.put("coverImg",video.getCover_url());
                datas.put("publishTime",video.getPublish_time());
                datas.put("classify",articleClassifyMapper.selectIdArticle(video.getVideo_classify()).getClassify_name());
                datalists.add(datas);
            }
            datalist.put("dataInfo",datalists);
            datalist.put("articleQuantity",videoList.size());
        }
        return datalist;
    }

    /**
     * 查询已发布素材
     */
    public Map<String,Object> selectConfirm(Map<String,Object> data){
        //0为文章,1为视频
        Integer start = Integer.parseInt((String) data.get("start"));
        //0为平台,1为品牌
        Integer tell = Integer.parseInt((String) data.get("tell"));
        Integer pag = Integer.parseInt((String) data.get("pag"))*10;
        Map<String,Object> datalist = new HashMap<>();
        List<Map<String,Object>> datalists= new ArrayList<>();
        if (tell == 0){
            if (start == 0){//平台已发布文章
                Integer classifyId = (Integer) data.get("classifyId");
                List<Article> articles = new ArrayList<>();
                List<Article> articleList = new ArrayList<>();
                if (classifyId == 0){
                    articles = articleMapper.selectTitleAdmin(pag,1);
                    articleList = articleMapper.selectArticle("1");
                }else {
                    articles = articleMapper.selectTitleAdmins(pag,classifyId,1);
                    articleList = articleMapper.selectArticles(classifyId,"1");
                }
                for (Article article : articles) {
                    Map<String,Object> datas = new HashMap<>();
                    datas.put("id",article.getId());
                    datas.put("title",article.getTitle());
                    datas.put("coverImg",article.getCover_img());
                    datas.put("publishTime",article.getPublish_time());
                    datas.put("classify",articleClassifyMapper.selectIdArticle(article.getArticle_classify()).getClassify_name());
                    datalists.add(datas);
                }
                datalist.put("dataInfo",datalists);
                datalist.put("articleQuantity",articleList.size());
            }else if (start == 1){//平台已发布视频
                List<Video> videos = videoMapper.selectVideoTieleAdmin(pag,1);
                List<Video> videoList = videoMapper.selectVideos("1");
                for (Video video : videos) {
                    Map<String,Object> datas = new HashMap<>();
                    datas.put("id",video.getVideo_id());
                    datas.put("title",video.getVideo_name());
                    datas.put("coverImg",video.getCover_url());
                    datas.put("publishTime",video.getPublish_time());
                    datas.put("classify",articleClassifyMapper.selectIdArticle(video.getVideo_classify()).getClassify_name());
                    datalists.add(datas);
                }
                datalist.put("dataInfo",datalists);
                datalist.put("articleQuantity",videoList.size());
            }
        }else if (tell == 1){
            String brandName = (String) data.get("brandName");
            String classify = (String) data.get("classify");
            if (start == 0){//品牌文章
                List<BrandArticle> brandArticles = new ArrayList<>();
                List<BrandArticle> brandArticleList = new ArrayList<>();
                //判断是否有为品牌查询
                if (brandName.equals("") || classify.equals("")){
                    //查询品牌文章
                    brandArticles = brandArticleMapper.selectBrandArticleAdmin(pag);
                    //查询品牌文章数量
                    brandArticleList = brandArticleMapper.selectBrandArticl();
                }else {
                    if (classify.equals("全部")){
                        brandArticles = brandArticleMapper.selectArticleBrands(brandName,pag);
                        brandArticleList = brandArticleMapper.selectArticleBrans(brandName);
                    }else {
                        brandArticles = brandArticleMapper.selectArticleBrand(brandName,classify,pag);
                        brandArticleList = brandArticleMapper.selectArticleBran(brandName,classify);
                    }
                }

                //整理返回数据
                for (BrandArticle brandArticle : brandArticles) {
                    if (brandArticle.getAuthor().equals("来找客")){
                        continue;
                    }
                    Map<String,Object> datas = new HashMap<>();
                    datas.put("id",brandArticle.getArticle_id());
                    datas.put("title",brandArticle.getTitle());
                    datas.put("coverImg",brandArticle.getCover_img());
                    datas.put("brandId",brandArticle.getBrand_id());
                    datas.put("brandName",brandArticle.getAuthor());
                    if (brandArticle.getClassify() != null){
                        datas.put("brandClassify",brandArticle.getClassify());
                    } else {
                        datas.put("brandClassify","");
                    }
                    datas.put("publishTime",brandArticle.getPublish_time());
                    datalists.add(datas);
                }

                datalist.put("dataInfo",datalists);
                datalist.put("articleQuantity",brandArticleList.size());
            }else if (start == 1){
                List<BrandVideo> brandVideos = new ArrayList<>();
                List<BrandVideo> brandVideoList = new ArrayList<>();
                if (brandName.equals("") || classify.equals("")){
                    brandVideos = brandVideoMapper.selectBrandVideoSums(pag);
                    brandVideoList = brandVideoMapper.selectBrandVideoSum();
                }else {
                    if (classify.equals("全部")){
                        brandVideos = brandVideoMapper.selectBrandNames(brandName,pag);
                        brandVideoList = brandVideoMapper.selectBrandNams(brandName);
                    }else {
                        brandVideos = brandVideoMapper.selectBrandName(brandName,classify,pag);
                        brandVideoList = brandVideoMapper.selectBrandNam(brandName,classify);
                    }
                }
                
                //整理返回数据
                for (BrandVideo brandVideo : brandVideos) {
                    if (brandVideo.getAuthor().equals("来找客")){
                        continue;
                    }
                    Map<String,Object> datas = new HashMap<>();
                    datas.put("id",brandVideo.getVideo_id());
                    datas.put("title",brandVideo.getTitle());
                    datas.put("coverImg",brandVideo.getCover_img());
                    datas.put("brandId",brandVideo.getBrand_id());
                    datas.put("brandName",brandVideo.getAuthor());
                    if (brandVideo.getClassify() != null){
                        datas.put("brandClassify",brandVideo.getClassify());
                    } else {
                        datas.put("brandClassify","");
                    }
                    datas.put("publishTime",brandVideo.getVideo_time());
                    datalists.add(datas);
                }
                datalist.put("dataInfo",datalists);
                datalist.put("articleQuantity",brandVideoList.size());
            }
        }

        return datalist;
    }

    /**
     * 根据id删除素材
     */
    public String deleteContent(Map<String,Object> data,String userToken){
        //判断token是否过期
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser == null){
            return null;
        }

        Integer id = (Integer) data.get("id");
        Integer start = Integer.parseInt((String) data.get("start"));
        Integer tell = Integer.parseInt((String) data.get("tell"));

        //判断删除文章还是视频
        if (tell == 0){
            if (start == 0){
                Article article = articleMapper.selectArticleId(id);
                if (article.getContent_img() == null){

                }else if (article.getContent_img().equals("[]")){
                    article.setContent_img(null);
                }
                if (article.getContent_img() != null){
                    List<String> contentImg = getList(article.getContent_img());
                    for (String s : contentImg) {
                        //线下路径
//                    String[] urls = s.split("/");
//                    String path = "E:\\开发软件\\tomcat\\apache-tomcat-8.5.53\\webapps\\"+urls[3]+"\\"+urls[4]+"\\"+urls[5];
                        //线上路径
                        String[] urls = s.split("/");
                        String path = "/item/apache-tomcat-7.0.99/webapps/product/"+urls[4]+"/"+urls[5];
                        //删除文件
                        File file = new File(path);
                        if (file.isFile()){
                            file.delete();
                        }
                    }
                }
                articleMapper.deleteArticle(id);
            }else if (start == 1){
                videoMapper.deleteVideo(id);
            }
        }else {
            if (start == 0){
                brandArticleMapper.deleteBrandArticle(id);
            }else {
                brandVideoMapper.deleteBrandVideo(id);
            }
        }
        return "删除成功";
    }


    public Map<String,Object> searchContent(Map<String,Object> data,String userToken){
        String title = "%" + (String) data.get("title") + "%";
        Integer start = Integer.parseInt((String) data.get("start"));
        Integer tell = Integer.parseInt((String) data.get("tell"));
        Integer sign = Integer.parseInt((String) data.get("sign"));
        Integer pag = Integer.parseInt((String) data.get("pag"))*10;
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser == null){
            return null;
        }


        Map<String,Object> datalist = new HashMap<>();
        List<Map<String,Object>> datalists= new ArrayList<>();
        if (sign == 0){
            //判断素材分类
            if (start == 0){
                //判断发布状态
                if (tell == 0){
                    List<Article> articles = articleMapper.selectArticleTitleAdmin(title,"0",pag);
                    List<Article> articles1 = articleMapper.selectArticleTitleAdmins(title,"0");
                    for (Article article : articles) {
                        Map<String,Object> datas = new HashMap<>();
                        datas.put("id",article.getId());
                        datas.put("title",article.getTitle());
                        datas.put("coverImg",article.getCover_img());
                        datas.put("publishTime",article.getPublish_time());
                        datas.put("classify",articleClassifyMapper.selectIdArticle(article.getArticle_classify()).getClassify_name());
                        datalists.add(datas);
                    }
                    datalist.put("dataInfo",datalists);
                    datalist.put("articleQuantity",articles1.size());
                }else {
                    List<Article> articles = articleMapper.selectArticleTitleAdmin(title,"1",pag);
                    List<Article> articles1 = articleMapper.selectArticleTitleAdmins(title,"1");
                    for (Article article : articles) {
                        Map<String,Object> datas = new HashMap<>();
                        datas.put("id",article.getId());
                        datas.put("title",article.getTitle());
                        datas.put("coverImg",article.getCover_img());
                        datas.put("publishTime",article.getPublish_time());
                        datas.put("classify",articleClassifyMapper.selectIdArticle(article.getArticle_classify()).getClassify_name());
                        datalists.add(datas);
                    }
                    datalist.put("dataInfo",datalists);
                    datalist.put("articleQuantity",articles1.size());
                }
            }else if (start == 1){
                if (tell == 0){
                    List<Video> videos = videoMapper.selectVideoTitleListAdmin(title,"0",pag);
                    List<Video> videos1 = videoMapper.selectVideoTitleListAdmins(title,"0");
                    for (Video video : videos) {
                        Map<String,Object> datas = new HashMap<>();
                        datas.put("id",video.getVideo_id());
                        datas.put("title",video.getVideo_name());
                        datas.put("coverImg",video.getCover_url());
                        datas.put("publishTime",video.getPublish_time());
                        datas.put("classify",articleClassifyMapper.selectIdArticle(video.getVideo_classify()).getClassify_name());
                        datalists.add(datas);
                    }
                    datalist.put("dataInfo",datalists);
                    datalist.put("articleQuantity",videos1.size());
                }else {
                    List<Video> videos = videoMapper.selectVideoTitleListAdmin(title,"1",pag);
                    List<Video> videos1 = videoMapper.selectVideoTitleListAdmins(title,"1");
                    for (Video video : videos) {
                        Map<String,Object> datas = new HashMap<>();
                        datas.put("id",video.getVideo_id());
                        datas.put("title",video.getVideo_name());
                        datas.put("coverImg",video.getCover_url());
                        datas.put("publishTime",video.getPublish_time());
                        datas.put("classify",articleClassifyMapper.selectIdArticle(video.getVideo_classify()).getClassify_name());
                        datalists.add(datas);
                    }
                    datalist.put("dataInfo",datalists);
                    datalist.put("articleQuantity",videos1.size());
                }
            }
        }else if (sign == 1){
            if (start == 0){
                List<BrandArticle> brandArticles = brandArticleMapper.selectArticleTitleAdmin(title,pag);
                List<BrandArticle> brandArticles1 = brandArticleMapper.selectArticleTitleAdmins(title);

                for (BrandArticle article : brandArticles) {
                    Map<String,Object> datas = new HashMap<>();
                    datas.put("id",article.getArticle_id());
                    datas.put("title",article.getTitle());
                    datas.put("coverImg",article.getCover_img());
                    datas.put("publishTime",article.getPublish_time());
                    datas.put("brandId",article.getBrand_id());
                    datas.put("brandName",article.getAuthor());
                    if (article.getClassify() == null){
                        datas.put("classify","");
                    }else {
                        datas.put("classify",article.getClassify());
                    }
                    datalists.add(datas);
                }
                datalist.put("dataInfo",datalists);
                datalist.put("articleQuantity",brandArticles1.size());

            }else {
                List<BrandVideo> videos = brandVideoMapper.selectVideoTitleListAdmin(title,pag);
                List<BrandVideo> videos1 = brandVideoMapper.selectVideoTitleListAdmins(title);
                for (BrandVideo video : videos) {
                    Map<String,Object> datas = new HashMap<>();
                    datas.put("id",video.getVideo_id());
                    datas.put("title",video.getTitle());
                    datas.put("coverImg",video.getCover_img());
                    datas.put("publishTime",video.getVideo_time());
                    datas.put("brandId",video.getBrand_id());
                    datas.put("brandName",video.getAuthor());
                    if (video.getClassify() == null){
                        datas.put("classify","");
                    }else {
                        datas.put("classify",video.getClassify());
                    }
                    datalists.add(datas);
                }
                datalist.put("dataInfo",datalists);
                datalist.put("articleQuantity",videos1.size());
            }
        }

        return datalist;
    }

    /**
     * 验证登录状态
     */
    public String selectToken(Map<String,Object> data){
        String token = (String) data.get("token");
        AdminUser adminUser = adminUserMapper.selectToken(token);
        if (adminUser != null){
            return "登录成功";
        }
        return null;
    }

    /**
     * 验证登录状态
     */
    public String selectTokens(String userToken){
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser != null){
            return "登录成功";
        }
        return null;
    }

    /**
     * 删除文章图片及上传视频
     */
    public String deleteImage(Map<String,Object> data){
        String url = (String) data.get("url");
        //线下
//        String[] urls = url.split("/");
//        String path = "E:\\开发软件\\tomcat\\apache-tomcat-8.5.53\\webapps\\"+urls[3]+"\\"+urls[4]+"\\"+urls[5];
        //线上
        String[] urls = url.split("/");
        String path = "/item/apache-tomcat-7.0.99/webapps/product/"+urls[4]+"/"+urls[5];
        System.out.println(path);
        //删除文件
        File file = new File(path);
        if (file.isFile()){
            file.delete();
            return "删除成功";
        }
        return null;
    }

    /**
     * string转list 方法
     */
    public List<String> getList(String str){
        String[] q = str.split("\\[");
        String[] w = q[1].split("\\]");
        List<String> list = Arrays.asList(w[0].split(","));
        return list;
    }


    /**
     * 用户信息数据查询
     */
    public Map<String,Object> adminData(String userToken){
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser == null){
            return null;
        }

        //用户信息
        //查询用户数量
        List<Xcuser> xcusers = xcuserMapper.selectxcuser();
        Integer user = xcusers.size();
        
        //计算昨日新增人数
        Integer newUser = 0;
        for (Xcuser xcuser : xcusers) {
            if (xcuser.getSign() == 1){
                newUser = newUser + 1;
            }
        }

        //会员数量
        //会员总数
        Integer vipUser = 0;
        //一年会员总数
        Integer oneUser = 0;
        //三年会员总数
        Integer threeUser = 0;
        //品牌合伙人总数
        Integer brandUser = 0;
        for (Xcuser xcuser : xcusers) {
            if (xcuser.getStart() == 1){
                vipUser = vipUser + 1;
            }else if (xcuser.getStart() == 2){
                vipUser = vipUser + 1;
                oneUser = oneUser + 1;
            }else if (xcuser.getStart() == 3){
                vipUser = vipUser + 1;
                threeUser = threeUser + 1;
            }else if (xcuser.getStart() == 4){
                vipUser = vipUser + 1;
                brandUser = brandUser + 1;
            }
        }

        //成交金额
        //查询成交金额
        List<VipOreder> vipOreders = vipOredermapper.selectVipOrder();
        //累计成交金额
        Double money = 0.00;
        //会员充值总额
        Double vipMoney = 0.00;
        //课程购买总额
        Double courseMoney = 0.00;
        //品牌购买总额
        Double brandMoney = 0.00;

        for (VipOreder vipOreder : vipOreders) {
            money = money + vipOreder.getOrder_money();
            if (vipOreder.getPay_type().equals("开通会员")){
                vipMoney = vipMoney + vipOreder.getOrder_money();
            }else if (vipOreder.getPay_type().equals("购买课程")){
                courseMoney = courseMoney + vipOreder.getOrder_money();
            }else if (vipOreder.getPay_type().equals("购买品牌")){
                brandMoney = brandMoney + vipOreder.getOrder_money();
            }
        }

        //品牌数量
        List<BrandNav> brandNavs = brandNavMapper.selectBrandNav();
        //品牌总量
        Integer brand = brandNavs.size();
        //新增昨日
        Integer yesterdayBrand = 0;
        //昨天日期
        Date d=new Date(System.currentTimeMillis()-1000*60*60*24);
        SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
        String ZUOTIAN=sp.format(d);//获取昨天日期

        for (BrandNav brandNav : brandNavs) {
            String[] a = brandNav.getBrand_time().split("\\s+");
            if (ZUOTIAN.equals(a[0])){
                yesterdayBrand = yesterdayBrand + 1;
            }
        }

        //文章
        //获取平台文章
        List<Article> articles = articleMapper.selectArticleTime();
        //获取品牌文章
        List<BrandArticle> brandArticles = brandArticleMapper.selectBrandArtTime();

        //文章总数
        Integer article = articles.size() + brandArticles.size();

        //视频
        //获取平台视频
        List<Video> videos = videoMapper.selectVideoSum();
        //获取品牌视频
        List<BrandVideo> brandVideos = brandVideoMapper.selectBrandVideoSum();

        //视频总数
        Integer video = videos.size() + brandVideos.size();

        //课程
        //获取平台课程
        List<Course> courses = courseMapper.selectCourseSum();
        //获取品牌课程
        List<BrandCourse> brandCourses = brandCourseMapper.selectBrandCourseSum();

        //课程总数
        Integer course = courses.size() + brandCourses.size();

        List<MatterData> matterDatas = matterDataMapper.selectMatterData();

        //文章新增
        Integer addArticle = 0;
        //视频新增
        Integer addVideo = 0;
        //课程新增
        Integer addCourse = 0;

        for (MatterData matterData : matterDatas) {
            if (matterData.getMatter_name().equals("article")){
                addArticle = article - matterData.getMatter_sum();
            }else if (matterData.getMatter_name().equals("video")){
                addVideo = video - matterData.getMatter_sum();
            }else if (matterData.getMatter_name().equals("course")){
                addCourse = course - matterData.getMatter_sum();
            }
        }


        //获取转发总数
        List<ShareUser> shareUsers = shareUserMapper.selectShareUser();

        // 文章转发总数
        Integer shareArticle = 0;
        //视频转发总数
        Integer shareVideo = 0;
        //课程转发总数
        Integer shareCourse = 0;

        for (ShareUser shareUser : shareUsers) {
            if (shareUser.getArticle_id() != null){
                shareArticle = shareArticle + 1;
            }
            if (shareUser.getVideo_id() != null){
                shareVideo = shareVideo + 1;
            }
            if (shareUser.getCourse_id() != null){
                shareCourse = shareCourse + 1;
            }
        }





        //整理数据返回
        Map<String,Object> map = new HashMap<>();
        map.put("userQuantity",user); //用户数量
        map.put("addUser",newUser); //昨日新增
        map.put("moneySum",money); //累计成交总额
        map.put("moneyVip",vipMoney); //会员充值总额
        map.put("moneyCourse",courseMoney); //课程购买总额
        map.put("moneyBrand",brandMoney); //品牌购买总额
        map.put("userVip",vipUser); //会员总数
        map.put("userOne",oneUser); //一年会员
        map.put("userThree",threeUser); //三年会员
        map.put("userBrand",brandUser); //品牌会员
        map.put("brandQuantity",brand); //品牌总量
        map.put("addBrand",yesterdayBrand); //昨日新增
        map.put("article",article); //文章总量
        map.put("video",video); //视频总量
        map.put("course",course); //课程总量
        map.put("shareArticle",shareArticle); //文章转发
        map.put("shareVideo",shareVideo); //视频转发
        map.put("shareCourse",shareCourse); //课程转发
        map.put("addArticle",addArticle); //文章新增
        map.put("addVideo",addVideo); //视频新增
        map.put("addCourse",addCourse); //课程新增

        return map;
    }

    /**
     * 品牌方首页数据查询
     */
    public Map<String,Object> adminBrandData(Map<String,Object> data , String userToken){
        //验证token
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser == null){
            return null;
        }
        //获取openId
        String openId = (String) data.get("openId");

        //获取品牌方品牌数量
        //计算已创品牌
        List<BrandNav> brandNav = brandNavMapper.selectBrandUser(openId);
        Integer useBrandNav = brandNav.size();
        //计算可创品牌
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);
        Integer unusedBrandNav = xcuser.getBrand_quantity() - useBrandNav;

        //获取品牌方累计收益金额
        List<VipOreder> vipOreder = vipOredermapper.selectReadOpenId(openId);
        Double earnings = 0.00;
        for (VipOreder oreder : vipOreder) {
            earnings = earnings + oreder.getBrokerage();
        }
        //品牌方可提现金额
        Double cash = xcuser.getUser_balance();

        //文章
        //文章总数
        List<BrandArticle> brandArticle = brandArticleMapper.selectBrandArticleSum(openId);
        Integer dayArticle = brandArticle.size();
        //文章转发总数
        Integer shareArticle = 0;
        //文章阅读总数
        Integer readAreicle = 0;
        for (BrandArticle article : brandArticle) {
            List<ShareUser> shareUsers = shareUserMapper.selectBrandArticle(article.getArticle_id());
            if (shareUsers.size() == 0){
                continue;
            }
            shareArticle = shareArticle + shareUsers.size();
            for (ShareUser shareUser : shareUsers) {
                readAreicle = readAreicle + shareUser.getDeg();
            }
        }

        //视频
        //视频总数
        List<BrandVideo> brandVideos = brandVideoMapper.selectBrandVideoAdminSum(openId);
        Integer dayVideo = brandVideos.size();
        //文章转发总数
        Integer shareVideo = 0;
        //文章阅读总数
        Integer readVideo = 0;
        for (BrandVideo video : brandVideos) {
            List<ShareUser> shareUsers = shareUserMapper.selectBrandVideo(video.getVideo_id());
            if (shareUsers.size() == 0){
                continue;
            }
            shareVideo = shareVideo + shareUsers.size();
            for (ShareUser shareUser : shareUsers) {
                readVideo = readVideo + shareUser.getDeg();
            }
        }

        List<BrandCourse> brandCourses = brandCourseMapper.selectBrandSum(openId);
        Integer dayCourse = brandCourses.size();
        //文章转发总数
        Integer shareCourse = 0;
        //文章阅读总数
        Integer readCourse = 0;
        for (BrandCourse course : brandCourses) {
            List<ShareUser> shareUsers = shareUserMapper.selectBrandCourse(course.getCourse_id());
            if (shareUsers.size() == 0){
                continue;
            }
            shareCourse = shareCourse + shareUsers.size();
            for (ShareUser shareUser : shareUsers) {
                readCourse = readCourse + shareUser.getDeg();
            }
        }
        //创建map整理数据
        Map<String,Object> datas = new HashMap<>();
        datas.put("useBrandNav",useBrandNav);//品牌方已创品牌
        datas.put("unusedBrandNav",unusedBrandNav);//品牌可创品牌
        datas.put("earnings",earnings);//品牌方累计收益
        datas.put("cash",cash);//品牌方可提现金额
        datas.put("dayArticle",dayArticle);//品牌方文章总数
        datas.put("shareArticle",shareArticle);//品牌方文章被分享数量
        datas.put("readAreicle",readAreicle);//品牌方文章被阅读数量
        datas.put("dayVideo",dayVideo);//品牌方视频总数
        datas.put("shareVideo",shareVideo);//品牌方视频被分享数量
        datas.put("readVideo",readVideo);//品牌方视频被阅读数量
        datas.put("dayCourse",dayCourse);//品牌方课程总数
        datas.put("shareCourse",shareCourse);//品牌方课程被分享数量
        datas.put("readCourse",readCourse);//品牌方课程被阅读数量

        return datas;
    }


    /**
     * 品牌方文章
     */
    public Map<String,Object> adminBrandArticle(Map<String,Object> data , String userToken){
        //验证token
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser == null){
            return null;
        }
        Integer start = Integer.parseInt((String) data.get("start"));
        if (start == 1){
            return adminBrandVideo(data,userToken);
        }
        //获取页码
        Integer pag = Integer.parseInt((String) data.get("pag"))*10;
        //品牌名称
        String brandName = (String) data.get("brandName");
        String classify = (String) data.get("classify");
        Map<String,Object> datainfo = new HashMap<>();
        //查询文章列表
        List<BrandArticle> brandArticles = new ArrayList<>();
        List<BrandArticle> brandArticless = new ArrayList<>();
        if (classify.equals("全部")){
            brandArticles = brandArticleMapper.selectArticleBrands(brandName,pag);
            brandArticless = brandArticleMapper.selectArticleBrans(brandName);
        }else {
            brandArticles = brandArticleMapper.selectArticleBrand(brandName,classify,pag);
            brandArticless = brandArticleMapper.selectArticleBran(brandName,classify);
        }

        List<Map<String,Object>> dataList = new ArrayList<>();
        for (BrandArticle brandArticle : brandArticles) {
            Map<String,Object> datas = new HashMap<>();
            datas.put("id",brandArticle.getArticle_id());
            datas.put("title",brandArticle.getTitle());
            datas.put("cover",brandArticle.getCover_img());
            datas.put("time",brandArticle.getPublish_time());
            String classifys = brandArticle.getClassify();
            if (classifys == null){
                datas.put("classify","无类别");
            }else {
                datas.put("classify",classifys);
            }
            dataList.add(datas);
        }
        datainfo.put("dataList",dataList);
        datainfo.put("quantity",brandArticless.size());
        return datainfo;
    }

    /**
     * 品牌方视频
     */
    public Map<String,Object> adminBrandVideo(Map<String,Object> data , String userToken){
        //验证token
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser == null){
            return null;
        }

        //获取页码
        Integer pag = Integer.parseInt((String) data.get("pag"))*10;
        String brandName = (String) data.get("brandName");
        String classify = (String) data.get("classify");
        //查询文章列表
        List<BrandVideo> brandArticles = new ArrayList<>();
        List<BrandVideo> brandArticless = new ArrayList<>();

        if (classify.equals("全部")){
            brandArticles = brandVideoMapper.selectBrandNames(brandName,pag);
            brandArticless = brandVideoMapper.selectBrandNams(brandName);
        }else {
            brandArticles = brandVideoMapper.selectBrandName(brandName,classify,pag);
            brandArticless = brandVideoMapper.selectBrandNam(brandName,classify);
        }

        Map<String,Object> datainfo = new HashMap<>();
        List<Map<String,Object>> dataList = new ArrayList<>() ;
        for (BrandVideo brandArticle : brandArticles) {
            Map<String,Object> datas = new HashMap<>();
            datas.put("id",brandArticle.getVideo_id());
            datas.put("title",brandArticle.getTitle());
            datas.put("cover",brandArticle.getCover_img());
            datas.put("time",brandArticle.getVideo_time());
            String classifys = brandArticle.getClassify();
            if (classify == null){
                datas.put("classify","无类别");
            }else {
                datas.put("classify",classifys);
            }
            dataList.add(datas);
        }

        datainfo.put("dataList",dataList);
        datainfo.put("quantity",brandArticless.size());
        return datainfo;
    }

    /**
     * 查询品牌数量
     */
    public List<BrandNav> adminBrandNav(Map<String,Object> data , String userToken){
        //验证token
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser == null){
            return null;
        }
        //获取openId
        String openId = (String) data.get("openId");

        //查询品牌
        List<BrandNav> brandNav = brandNavMapper.selectBrandUser(openId);


        return brandNav;
    }


    public List<BrandClassify> adminBrandClassify(Map<String,Object> data , String userToken){
        //验证token
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser == null){
            return null;
        }
        Integer brandId = (Integer) data.get("brandId");

        List<BrandClassify> brandClassifies = brandClassifyMapper.selectBrandClassify(brandId);
        if (brandClassifies.size() == 0){
            return null;
        }


        return brandClassifies;
    }

}
