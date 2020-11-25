package com.example.datatoolserver.mapper;


import com.example.datatoolserver.pojo.BrandArticle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface brandArticleMapper {
    //写入一条素材库文章
    int insertBrandArticle(String openId, String title, String digest, String coverImg, String url, Integer quantity, Integer brandId, String content, String token, String author, String classify, String articleTime);

    //更新文章
    int updataBrandArticleInfo(Integer articleId, String title, String digest, String url, Integer quantity, Integer brandId, String content, String author, String classify);
    int updataBrandArticleInfos(Integer articleId, String openId, String title, String digest, String url, Integer quantity, Integer brandId, String content, String author, String classify);


    //根据token查询
    BrandArticle selectBrandArticleId(String token);

    //根据品牌id和openid查询
    List<BrandArticle> selectBrandArticle(String openId, Integer brandId, Integer pag);

    //根据标题进行模糊查询
    List<BrandArticle> selectBrandArticleList(String openId, String title, Integer pag);

    //根据id查询
    BrandArticle selectArticleId(Integer articleId);
    List<BrandArticle> selectArticleBrand(String brandName, String classify, Integer pag);
    List<BrandArticle> selectArticleBran(String brandName, String classify);
    List<BrandArticle> selectArticleBrands(String brandName, Integer pag);
    List<BrandArticle> selectArticleBrans(String brandName);

    List<BrandArticle> selectArticleBrandsOpenId(String openId, String brandName, Integer pag);

    List<BrandArticle> selectArticleBrandOpenId(String brandName, String classify, Integer pag, String openId);

    //根据id更新品牌分类
    int updataBrandName(Integer articleId, Integer brandId, String author, String classify);

    //根据id删除
    int deleteBrandArticle(Integer articleId);

    //根据品牌id删除
    int deleteBrandId(Integer brandId);

    //根据品牌id和品牌名称更新名称
    int updataArticleClassify(Integer brandId, String classify);

    //查询所有品牌文章,返回时间数据
    List<BrandArticle> selectBrandArtTime();

    //查询品牌所有文章
    List<BrandArticle> selectBrandArticleAdmin(Integer pag);

    List<BrandArticle> selectBrandArticleAdmins(Integer pag, String author);

    List<BrandArticle> selectBrandArticl();

    List<BrandArticle> selectArticleTitleAdmin(String title, Integer pag);

    List<BrandArticle> selectArticleTitleAdmins(String title);

    List<BrandArticle> selectBrandArticleSum(String openId);
    //根据openId和页码查询
    List<BrandArticle> selectBrandArticleAdminPag(String openId, Integer pag);
}
