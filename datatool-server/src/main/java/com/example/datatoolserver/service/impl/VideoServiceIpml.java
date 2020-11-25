package com.example.datatoolserver.service.impl;

import com.example.datatoolserver.controller.shareController;
import com.example.datatoolserver.mapper.*;
import com.example.datatoolserver.pojo.*;
import com.example.datatoolserver.service.IVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoServiceIpml implements IVideoService {
    @Autowired
    private shareVideoMapper shareVideoMapper;

    @Autowired
    private brandNavMapper brandNavMapper;

    @Autowired
    private shareArticleMapper shareArticleMapper;

    @Autowired
    private videoMapper videoMapper;

    @Autowired
    private xcuserMapper xcuserMapper;

    @Autowired
    private articleClassifyMapper articleClassifyMapper;

    @Autowired
    private shareController shareController;

    @Autowired
    private brandVideoMapper brandVideoMapper;

    @Autowired
    private materialMapper materialMapper;

    @Autowired
    private productMapper productMapper;

    @Autowired
    private productImgMapper productImgMapper;

    @Autowired
    private videoLikeMapper videoLikeMapper;


    /**
     * 上传修改视频
     * @param video
     * @return
     */
    public String alterVideo(Map<String,Object> video){
        int videoId = (int) video.get("videoId");
        String openId = (String) video.get("openId");
        String videoName = (String) video.get("videoName");
        Integer sign = (Integer) video.get("sign");
        String coverUrl = (String) video.get("coverUrl");
        String downUrl = (String) video.get("downUrl");
        Integer videoLike = (Integer) video.get("videoLike");
        Integer videoClassify = (Integer) video.get("videoClassify");
        List<Integer> product = (List<Integer>) video.get("product");
        Integer quantity = (Integer) video.get("quantity");
        String author = (String) video.get("author");
        String digest = (String) video.get("digest");
        String popImg = (String) video.get("popImg");
        Boolean popCode = (Boolean) video.get("popCode");
        if (downUrl == null){
            return null;
        }
        Integer pop = 0;
        if (popCode == true){
            pop = 1;
        }


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

        if (sign == 0){
            ShareVideo shareVideo = shareVideoMapper.selectShareVid(openId,videoId);
            if (shareVideo != null){
                shareVideoMapper.updataShareVid(openId,videoId,videoName,coverUrl,downUrl,videoClassify,0,advertising1,quantity,digest,videoLike,author,popImg,pop);
                return "分享成功";
            }
            shareVideoMapper.insertShareVideo(openId,videoId,videoName,coverUrl,downUrl,videoClassify,0,advertising1,quantity,digest,videoLike,author,popImg,pop);
        }else if (sign == 1){
            ShareVideo shareVideo = shareVideoMapper.selectShareBid(openId,videoId);
            if (shareVideo != null){
                shareVideoMapper.updataShareBid(openId,0,videoName,coverUrl,downUrl,videoClassify,videoId,advertising1,quantity,digest,videoLike,author,popImg,pop);
                return "分享成功";
            }
            shareVideoMapper.insertShareVideo(openId,0,videoName,coverUrl,downUrl,videoClassify,videoId,advertising1,quantity,digest,videoLike,author,popImg,pop);
        }
        return "分享成功";

    }

    /**
     * 按品牌查询视频
     */
    public List<Map<String,Object>> selectVideo(Map<String,Object> video){
//        String openId = (String) video.get("openId");
//        int start = (int) video.get("start");
//        //获取页数
//        Integer pag = (Integer) video.get("pag")*20;
//
//        List<Map<String,Object>> datas = new ArrayList<>();
//
//        if (start == 0){
//            if(brandNavMapper.selectBrand(openId).size() >= 1){
//                List<ShareArticle> shareArticles = shareArticleMapper.selectShareArtOpenIdList(openId,pag);
//                if (shareArticles.size() == 0){
//                    Map<String,Object> data = new HashMap<>();
//                    data.put("erorr","还没有分享过文章");
//                    datas.add(data);
//
//                }
//                for (ShareArticle shareArticle : shareArticles) {
//                    Map<String,Object> data = new HashMap<>();
//                    data.put("id",shareArticle.getShare_id());
//                    data.put("title",shareArticle.getTitle());
//                    data.put("cover_img",shareArticle.getCover_img());
//                    datas.add(data);
//                }
//
//            } else if (brandNavMapper.selectBrand(openId).size() == 0){
//                Map<String,Object> data = new HashMap<>();
//                data.put("erorr","您还没有创建品牌");
//                datas.add(data);
//
//            }
//        }else if (start == 1){
//            if (brandNavMapper.selectBrand(openId).size() >= 1){
//                List<ShareVideo> shareVideos = shareVideoMapper.selectShareVideoOpenIdList(openId,pag);
//                if (shareVideos.size() == 0){
//                    Map<String,Object> data = new HashMap<>();
//                    data.put("erorr","还没有分享过文章");
//                    datas.add(data);
//
//                }
//                for (ShareVideo shareVideo : shareVideos) {
//                    Map<String,Object> data = new HashMap<>();
//                    data.put("id",shareVideo.getShare_video_id());
//                    data.put("title",shareVideo.getVideo_name());
//                    data.put("cover_img",shareVideo.getCover_url());
//                    datas.add(data);
//                }
//
//            }else if (brandNavMapper.selectBrand(openId).size() == 0){
//                Map<String,Object> data = new HashMap<>();
//                data.put("erorr","您还没有创建品牌");
//                datas.add(data);
//
//            }
//        }

        return null;
    }


    /**
     * 按照video_id查询视频信息,openid查询用户信息
     * @param video
     * @return
     */
    public Map<String,Object> videoInfo(Map<String,Object> video){
        int videoId = (int) video.get("id");
        String openId = (String) video.get("openId");
        Integer start = (Integer) video.get("start");
        Integer sign = (Integer) video.get("sign");
        Map<String,Object> data = new HashMap<>();

        if (start == 0){
            if (sign == 0 || sign == null){
                //阅读量加1
                videoMapper.updataQuantity(videoId);
                Video videoinfo = videoMapper.selectVideoId(videoId);
                BrandNav brandNav = brandNavMapper.selectBrandName(openId,videoinfo.getAuthor());
                String classify = articleClassifyMapper.selectIdArticle(videoinfo.getVideo_classify()).getClassify_name();
                Map<String,Object> art = new HashMap<>();
                art.put("video_id",videoinfo.getVideo_id());
                art.put("video_name",videoinfo.getVideo_name());
                art.put("digest",videoinfo.getDigest());
                art.put("cover_img",videoinfo.getCover_url());
                art.put("down_url",videoinfo.getDown_url());
                art.put("quantity",0);
                art.put("popImg",null);
                art.put("popCode",false);
                art.put("brandId",brandNav.getBrand_id());
                art.put("author",brandNav.getBrand_name());
                art.put("video_like",0);
                data.put("videoinfo",art);
//                data.put("classify",classify);
                data.put("classify","");
                data.put("brandName",brandNav.getBrand_name());
                data.put("brandId",brandNav.getBrand_id());
                List a = new ArrayList();
                data.put("product",a);
                if (openId!=null){
                    Xcuser userinfo = xcuserMapper.selectOpenId(openId);
                    data.put("userinfo",userinfo);
                }

                VideoLike videoLike = videoLikeMapper.selectVideoLikeVideoId(openId,videoId);
                if (videoLike == null){
                    data.put("like",false);
                }else {
                    data.put("like",true);
                }

            }else if (sign == 1){
                BrandVideo brandVideo = brandVideoMapper.selectVideoIds(videoId);
                Map<String,Object> art = new HashMap<>();
                art.put("video_id",brandVideo.getVideo_id());
                art.put("video_name",brandVideo.getTitle());
                art.put("digest",brandVideo.getDigest());
                art.put("cover_url",brandVideo.getCover_img());
                art.put("down_url",brandVideo.getDown_url());
                art.put("quantity",brandVideo.getQuantity());
                art.put("brandId",brandVideo.getBrand_id());
                art.put("author",brandVideo.getAuthor());
                art.put("video_like",brandVideo.getVideo_like());
                art.put("popImg",brandVideo.getPop_img());
                if (brandVideo.getPop_code() == 0){
                    art.put("popCode",false);
                }else {
                    art.put("popCode",true);
                }

                List<Material> materials = materialMapper.selectMaterialVideo(brandVideo.getVideo_id());
                if (materials.size()!=0){
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
                    data.put("product",product);
                }else {
                    List a = new ArrayList();
                    data.put("product",a);
                }

                data.put("videoinfo",art);
                data.put("brandId",brandVideo.getBrand_id());
                data.put("brandName",brandVideo.getAuthor());

//                data.put("classify",brandNavMapper.selectBrandId(brandVideo.getBrand_id()).getBrand_name());

                data.put("classify",brandVideo.getClassify());

                if (openId!=null){
                    Xcuser userinfo = xcuserMapper.selectOpenId(openId);
                    data.put("userinfo",userinfo);
                }

                VideoLike videoLike = videoLikeMapper.selectVideoLikeBrandVideoId(openId,videoId);
                if (videoLike == null){
                    data.put("like",false);
                }else {
                    data.put("like",true);
                }

            }

        }else if (start == 1){
            ShareVideo shareVideo = shareVideoMapper.selectShareVideoId(videoId);
            data.put("videoinfo",shareVideo);
            data.put("Classify",articleClassifyMapper.selectIdArticle(shareVideo.getVideo_classify()).getClassify_name());
            data.put("userinfo",xcuserMapper.selectOpenId(openId));
        }



        return data;
    }


    /**
     * 视频点赞
     */
    public Integer videoLike(Map<String,Object> data){
        String openId = (String) data.get("openId");
        String readOpenId = (String) data.get("readOpenId");
        Integer id = (Integer) data.get("id");
        //区分平台还是素材
        Integer sign = (Integer) data.get("sign");
        //区分点赞还是减赞
        Integer start = (Integer) data.get("start");
        //区分分享人或被分享人
        Integer tell = (Integer) data.get("tell");
        if (tell == null || tell == 0){
            if (sign == 0 && start == 0){
                videoMapper.updataLike(id);
                videoLikeMapper.insertVideoLike(openId,id,0,0,null);
                return videoMapper.selectVideoId(id).getVideo_like();
            }else if (sign ==0 && start == 1){
                videoMapper.updataLikeNo(id);
                videoLikeMapper.deleteVideoLikeVideoId(openId,id);
                return videoMapper.selectVideoId(id).getVideo_like();
            }else if (sign == 1 && start == 0){
                brandVideoMapper.updataLike(id);
                videoLikeMapper.insertVideoLike(openId,0,id,0,null);
                return brandVideoMapper.selectVideoIds(id).getVideo_like();
            }else if (sign == 1 && start == 1){
                brandVideoMapper.updataLikeNo(id);
                videoLikeMapper.deletetVideoLikeBrandVideoId(openId,id);
                return brandVideoMapper.selectVideoIds(id).getVideo_like();
            }
        }else if (tell == 1){
            if (sign == 0 && start == 0){//平台点赞
                videoMapper.updataLike(id);
                videoLikeMapper.insertVideoLike(openId,id,0,0,readOpenId);
                ShareVideo shareVideo = shareVideoMapper.selectShareVid(openId,id);
                if (shareVideo != null){
                    shareVideoMapper.updataLike(openId,id);
                }
                return shareVideoMapper.selectShareVid(openId,id).getVideo_like();
            }else if (sign == 0 && start == 1){//平台减赞
                shareVideoMapper.updataLikeNo(openId,id);
                videoLikeMapper.deleteVideoLikeVideoIdRead(openId,id,readOpenId);
                return shareVideoMapper.selectShareVid(openId,id).getVideo_like();
            }else if (sign == 1 && start == 0){//素材点赞
                brandVideoMapper.updataLike(id);
                videoLikeMapper.insertVideoLike(openId,0,id,0,readOpenId);
                ShareVideo shareVideo = shareVideoMapper.selectShareBid(openId,id);
                if (shareVideo != null){
                    shareVideoMapper.updataLikeB(openId,id);
                }
                return shareVideoMapper.selectShareBid(openId,id).getVideo_like();
            }else if (sign == 1 && start == 1){ //素材减赞
                videoMapper.updataLikeNo(id);
                videoLikeMapper.deleteVideoLikeVideoIdRead(openId,id,readOpenId);
                return shareVideoMapper.selectShareBid(openId,id).getVideo_like();
            }
        }

        return null;
    }
}
