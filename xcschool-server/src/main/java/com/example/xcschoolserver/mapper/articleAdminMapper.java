package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.ArticleAdmin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface articleAdminMapper {
    //写入一篇文章
    int insertArticle(String title, String thumbMediaId, String author, String digest, String content, String coverImg, Integer articleClassify, Integer special, Integer quantity, String contentImg);

    //根据thumbMediaId查询文章
    ArticleAdmin selectarticle(String thumbMediaId);
}
