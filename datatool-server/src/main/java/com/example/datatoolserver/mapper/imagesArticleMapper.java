package com.example.datatoolserver.mapper;

import com.example.datatoolserver.pojo.ImagesArticle;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface imagesArticleMapper {
    //写入一条对应关系
    int insertImg(Integer articleId, Integer videoId, Integer pathUrl);

    //根据文章id查询
    ImagesArticle selectImg(Integer articleId);

    //根据视频id查询
    ImagesArticle selectVideo(Integer videoId);

    //根据文章id删除
    int deleteimg(Integer articleId);

    //根据视频id删除
    int deleteVideo(Integer videoId);
}
