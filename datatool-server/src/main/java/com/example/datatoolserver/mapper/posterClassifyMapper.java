package com.example.datatoolserver.mapper;

import com.example.datatoolserver.pojo.PosterClassify;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface posterClassifyMapper {
    //查询打卡海报分类
    List<PosterClassify> selectPosterClassify();

    //查询分享有你分类
    List<PosterClassify> selectSharePoster();

}
