package com.example.collecttoolserver.mapper;

import com.example.collecttoolserver.pojo.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface articleMapper {
    //写入文章并返回主键
    Integer insertArticle(String openId,String title,String digest,String coverImg,String originalUrl,String author,String collectTime,String popCode,String popImg,Integer readQuantity,String phone);
    //根据id查询文章
    Article selectArticleId(Integer articleId);
}
