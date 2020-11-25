package com.example.xcschoolserver.mapper;



import com.example.xcschoolserver.pojo.BrandVideo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface brandVideoMapper {
    //写入素材视频
    int insertVideo(String openId, String title, String digest, String coverImg, Integer quantity, Integer brandId, Integer videoLike, String token, String author, String classify, String videoTime, String downUrl, String popImg, Integer popCode);

    //更新视频详情
    int updataBrandVideoInfo(Integer videoId, String title, String digest, Integer quantity, Integer brandId, Integer videoLike, String author, String classify, String popImg, Integer popCode);
    int updataBrandVideoInfos(Integer videoId, String openId, String title, String digest, Integer quantity, Integer brandId, Integer videoLike, String author, String classify, String popImg, Integer popCode, String downUrl);

    //点赞数+1
    int updataLike(Integer videoId);

    int updataLikeNo(Integer videoId);


    //根据token查询一条视频
    BrandVideo selectVideoId(String token);
    //根据品牌id和openid查询
    List<BrandVideo> selectVideo(String openId, Integer brandId, Integer pag);
    //根据标题进行模糊查询
    List<BrandVideo> selectBrandVideoList(String openId, String title, Integer pag);
    //根据id进行查询
    BrandVideo selectVideoIds(Integer videoId);

    List<BrandVideo> selectBrandName(String brandName, String classify, Integer pag);
    List<BrandVideo> selectBrandNam(String brandName, String classify);
    List<BrandVideo> selectBrandNames(String brandName, Integer pag);
    List<BrandVideo> selectBrandNams(String brandName);

    List<BrandVideo> selectBrandNameOpenId(String brandName, String classify, Integer pag, String openId);
    List<BrandVideo> selectBrandNamesOpenId(String brandName, Integer pag, String openId);

    //跟新视频品牌归属
    int updataBrandVideo(Integer videoId, Integer brandId, String author, String classify);

    //根据id删除
    int deleteBrandVideo(Integer videoId);

    //根据品牌id删除
    int deleteBrandId(Integer brandId);

    int updataVideoClassify(Integer brandId, String classify);

    //查询视频总数
    List<BrandVideo> selectBrandVideoSum();
    //
    List<BrandVideo> selectBrandVideoSums(Integer pag);

    List<BrandVideo> selectVideoTitleListAdmin(String title, Integer pag);
    List<BrandVideo> selectVideoTitleListAdmins(String title);

}
