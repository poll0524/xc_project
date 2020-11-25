package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.Poster;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface posterMapper {
    //查询海报
    List<Poster> selectPoster(Integer pag);
    //根据分类查询海报
    List<Poster> selectPosterClassify(Integer posterClassify, Integer pag);

}
