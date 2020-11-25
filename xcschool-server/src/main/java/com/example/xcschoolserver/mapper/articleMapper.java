package com.example.xcschoolserver.mapper;



import com.example.xcschoolserver.pojo.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface articleMapper {
    //查询所有
    List<Article> selectArticle(String show_cover_pic);
    List<Article> selectArticles(Integer classifyId, String show_cover_pic);


    //根据id查询
    Article selectArticleId(Integer id);

    //查询文章列表
    List<Article> selectTitle(Integer bag);


    //根据media_id查询
    Article selectMediaIdArticle(String thumb_media_id);

    //存储文章
    int insertArticle(String title, String thumb_media_id, String author, String digest, String content, String url, Integer show_cover_pic, Integer article_classify, Integer quantity);

    //根据分类查询文章
    List<Article> selectClassify(int classifyId, int bag);

    //添加封面图片
    int updataMediaIdImg(String mediaId, String coverImgUrl);

    //根据标题模糊查询列表
    List<Article> selectArticleTitle(String title, Integer pag);

    //随机查询6个文章
    List<Article> selectRaArticle();

    //根据分类随机查询6个文章
    List<Article> selectClaRaArticle(Integer articleClassify);

    //根据专题查询文章
    List<Article> selectSpecialArticle(Integer bag, Integer special);

    //阅读量加1
    int updataQuantity(Integer id);
    //管理后台存文章
    int insertArticleAdmin(String title, String thumb_media_id, String author, String digest, String content, String url, String coverImg, String show_cover_pic, Integer article_classify, Integer quantity, String time);

    //根据id跟新发布状态
    int updataShowCoverPic(Integer id, String show_cover_pic);

    //查询文章列表
    List<Article> selectTitleAdmin(Integer bag, Integer show_cover_pic);
    List<Article> selectTitleAdmins(Integer bag, Integer classifyId, Integer show_cover_pic);

    //根据id删除
    int deleteArticle(Integer id);

    //管理后台根据名字模糊查询
    List<Article> selectArticleTitleAdmin(String title, String show_cover_pic, Integer pag);

    List<Article> selectArticleTitleAdmins(String title, String show_cover_pic);

    //更新文章
    int updataArticleAdmin(Integer id, String title, String author, String digest, String content, String coverImg, String show_cover_pic, Integer article_classify, Integer quantity, String time);

    //查询所有文章返回时间
    List<Article> selectArticleTime();

}
