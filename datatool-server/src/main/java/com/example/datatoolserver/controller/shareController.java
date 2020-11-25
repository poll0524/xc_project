package com.example.datatoolserver.controller;

import com.example.datatoolserver.common.ReturnVO;
import com.example.datatoolserver.common.WeChatUtil;
import com.example.datatoolserver.mapper.*;
import com.example.datatoolserver.pojo.*;
import com.example.datatoolserver.service.IShareService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/share")
public class shareController {
    @Autowired
    private articleClassifyMapper articleClassifyMapper;
    @Autowired
    private shareArticleMapper shareArticleMapper;
    @Autowired
    private xcuserMapper xcuserMapper;
    @Autowired
    private IShareService shareService;
    @Autowired
    private shareVideoMapper shareVideoMapper;
    @Autowired
    private materialMapper materialMapper;
    @Autowired
    private productImgMapper productImgMapper;
    @Autowired
    private productMapper productMapper;
    @Autowired
    private videoLikeMapper videoLikeMapper;
    @Autowired
    private brandNavMapper brandNavMapper;
    @Autowired
    private shareUserMapper shareUserMapper;

    /**
     * 查询分享文章
     */
    @ResponseBody
    @PostMapping("/selectShareArticle")
    public ReturnVO<Map<String,Object>> selectShareArticle(@RequestBody Map<String,Object> share){
        //分享人openid
        String openId = (String) share.get("openId");
        //阅读人openId
        String readOpenId = (String) share.get("readOpenid");
        //素材id
        Integer id = (Integer) share.get("id");
        //平台还是品牌
        Integer sign = (Integer) share.get("sign");
        if (openId == null && id == null){
            System.out.println("openId和thumbMediaId都为null");
            return new ReturnVO().error(40000,"openId和thumbMediaId都为null");
        }else if(id == null){
            System.out.println("thumbMediaId为null");
            return new ReturnVO().error(40001,"thumbMediaId为null");
        }else if (openId == null){
            System.out.println("openId为null");
            return new ReturnVO().error(40002,"openId为null");
        }
        ShareArticle article = new ShareArticle();
        List<Map<String,Object>> products = new ArrayList<>();
        String ClassifyName = "";
        if (sign == 0){
            article = shareArticleMapper.selectShareVid(openId,id);
            ClassifyName = articleClassifyMapper.selectIdArticle(article.getArticle_classify()).getClassify_name();
        }else if (sign == 1){
            article = shareArticleMapper.selectShareBid(openId,id);
//            查询产品
            List<Material> materials = materialMapper.selectMaterialArticle(id);
            for (Material material : materials) {
                Map<String,Object> prod = new HashMap<>();
                Product pro = productMapper.selectProductInfo(material.getProduct_id());
                if (pro != null){
                    prod.put("productTitle",pro.getProduct_title());
                    prod.put("productId",pro.getProduct_id());
                    prod.put("url",pro.getUrl());
                    prod.put("vipPrice",pro.getVip_price());
                    prod.put("price",pro.getPrice());
                    prod.put("coverImg",productImgMapper.selectProductImg(pro.getOpen_id(),pro.getProduct_id()).getCover_url());
                    products.add(prod);
                }
            }
            ClassifyName = article.getAuthor();
        }

        if (article == null){
            return new ReturnVO().error(40003,"您还没有该分享文章");
        }
        Map<String,Object> data = new HashMap<>();
        data.put("article",article);

        if (openId.equals(readOpenId)){
            data.put("brandId",brandNavMapper.selectBrandName(openId,ClassifyName).getBrand_id());
            data.put("ClassifyName",ClassifyName);
        }else {
            data.put("brandId",brandNavMapper.selectBrandName(readOpenId,WeChatUtil.AUTHOR).getBrand_id());
            data.put("ClassifyName",WeChatUtil.AUTHOR);
        }
        data.put("start",0);
        data.put("userinfo",xcuserMapper.selectOpenId(openId));
        data.put("readgrade",xcuserMapper.selectOpenId(readOpenId).getStart());
        data.put("product",products);

        return new ReturnVO(data);
    }


    /**
     * 查询分享的视频
     */
    @ResponseBody
    @PostMapping("/selectShareVideo")
    public ReturnVO<Map<String,Object>> selectShareVideo(@RequestBody Map<String,Object> video){
        String openId = (String) video.get("openId");
        String readOpenid = (String) video.get("readOpenid") ;
        Integer videoId = (Integer) video.get("videoId");
        Integer sign = (Integer) video.get("sign");
        if (openId == null && videoId == null){
            System.out.println("openId和thumbMediaId都为null");
            return new ReturnVO().error(40000,"openId和thumbMediaId都为null");
        }else if(videoId == null){
            System.out.println("thumbMediaId为null");
            return new ReturnVO().error(40001,"thumbMediaId为null");
        }else if (openId == null){
            System.out.println("openId为null");
            return new ReturnVO().error(40002,"openId为null");
        }

        Map<String,Object> data = new HashMap<>();
        ShareVideo shareVideo = new ShareVideo();
        Boolean code = true;
        List<Map<String,Object>> products = new ArrayList<>();
        String ClassifyName = "";
        if (sign == 0){
            shareVideo = shareVideoMapper.selectShareVid(openId,videoId);
            ClassifyName = shareVideo.getAuthor();
            data.put("brandId",brandNavMapper.selectBrandName(openId,shareVideo.getAuthor()).getBrand_id());
            //判断是否点赞
            VideoLike like = videoLikeMapper.selectVideoLikeVideoId(openId,videoId);
            if (like == null){
                data.put("like",false);
            }else {
                data.put("like",true);
            }
            if(shareVideo.getPop_code() == 0){
                code = false;
            }
        }else if (sign == 1){
            shareVideo = shareVideoMapper.selectShareBid(openId,videoId);
            List<Material> materials = materialMapper.selectMaterialVideo(videoId);
            for (Material material : materials) {
                Map<String,Object> prod = new HashMap<>();
                Product pro = productMapper.selectProductInfo(material.getProduct_id());
                if (pro != null){
                    prod.put("productTitle",pro.getProduct_title());
                    prod.put("productId",pro.getProduct_id());
                    prod.put("url",pro.getUrl());
                    prod.put("vipPrice",pro.getVip_price());
                    prod.put("price",pro.getPrice());
                    prod.put("coverImg",productImgMapper.selectProductImg(pro.getOpen_id(),pro.getProduct_id()).getCover_url());
                    products.add(prod);
                }
            }
            ClassifyName = shareVideo.getAuthor();

            //判断是否点赞
            VideoLike like = videoLikeMapper.selectVideoLikeBrandVideoId(openId,videoId);
            if (like == null){
                data.put("like",false);
            }else {
                data.put("like",true);
            }
            if(shareVideo.getPop_code() == 0){
                code = false;
            }
        }


        if (shareVideo ==null){
            return new ReturnVO().error(40003,"您还没有该分享文章");
        }

        data.put("video",shareVideo);

        if (openId.equals(readOpenid)){
            data.put("brandId",brandNavMapper.selectBrandName(openId,ClassifyName).getBrand_id());
            data.put("ClassifyName",ClassifyName);
        }else {
            data.put("brandId",brandNavMapper.selectBrandName(readOpenid, WeChatUtil.AUTHOR).getBrand_id());
            data.put("ClassifyName",WeChatUtil.AUTHOR);
        }
        data.put("start",1);
        data.put("readgrade",xcuserMapper.selectOpenId(readOpenid).getStart());
        data.put("userinfo",xcuserMapper.selectOpenId(openId));
        data.put("product",products);
        data.put("code",code);
        return new ReturnVO(data);
    }




    /**
     * 阅读人信息接口
     */
    @ResponseBody
    @PostMapping("/insertShareUser")
    public ReturnVO<Object> insertShareUser(@RequestBody Map<String,Object> share) throws JSONException {
        return new ReturnVO(shareService.insertShareUser(share));
    }

    /**
     * 阅读人种类和次数
     */
    @ResponseBody
    @PostMapping("/shareUserList")
    public ReturnVO<Map<String,Object>> shareUserList(@RequestBody Map<String,Object> datac){

        return new ReturnVO(shareService.shareUserList(datac));
    }

    /**
     * 阅读人阅读详情
     */
    @ResponseBody
    @PostMapping("/readUser")
    public ReturnVO<Map<String,Object>> readUser(@RequestBody Map<String,Object> users) throws ParseException {
        return new ReturnVO(shareService.readUser(users));
    }

    /**
     * 团队列表
     */
    @ResponseBody
    @PostMapping("/selectGroupList")
    public ReturnVO<Map<String,Object>> selectGroupList(@RequestBody Map<String,Object> data){
        return new ReturnVO(shareService.selectGroupList(data));
    }

    /**
     * 课程详情
     */
    @ResponseBody
    @PostMapping("/selectShareCourse")
    public ReturnVO<Map<String,Object>> selectShareCourse(@RequestBody Map<String,Object> data) throws JSONException {
        return new ReturnVO(shareService.selectShareCourse(data));
    }


    /**
     * 数据追踪诱惑数据
     */
    @ResponseBody
    @PostMapping("/selectShareUser")
    public ReturnVO<Integer> selectShareUser(@RequestBody Map<String,Object> data){
        String openId = (String) data.get("openId");
        List<ShareUser> shareUsers = shareUserMapper.selectShareUserList(openId);
        if (shareUsers.size() == 0){
            return new ReturnVO(0);
        }
        Integer deg = 0;
        for (ShareUser shareUser : shareUsers) {
            deg = deg + shareUser.getDeg();
        }
        return new ReturnVO(deg);
    }

    /**
     * 阅读时长
     */
    @ResponseBody
    @PostMapping("/readTime")
    public ReturnVO<String> readTime(@RequestBody Map<String,Object> data){
        return new ReturnVO(shareService.readTime(data));
    }
}
