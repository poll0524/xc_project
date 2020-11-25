package com.example.xcschoolserver.mapper;

import com.example.xcschoolserver.pojo.Sentence;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface sentenceMapper {
//    随机选取一条记录
    Sentence selectOneSentence();
}
