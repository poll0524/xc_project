package com.example.xcschoolserver.mapper;

import com.example.xcschoolserver.pojo.ArticleClassify;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface articleClassifyMapper {
    ArticleClassify selectIdArticle(Integer classify_id);

    List<ArticleClassify> selectClassifyList();

    ArticleClassify selectNameArticle(String classifyName);

    List<ArticleClassify> selectClassify();
    List<ArticleClassify> selectClassifys();
}
