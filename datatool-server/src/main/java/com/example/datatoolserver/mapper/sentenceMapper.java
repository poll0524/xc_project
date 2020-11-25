package com.example.datatoolserver.mapper;

import com.example.datatoolserver.pojo.Sentence;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface sentenceMapper {
//    随机选取一条记录
    Sentence selectOneSentence();
}
