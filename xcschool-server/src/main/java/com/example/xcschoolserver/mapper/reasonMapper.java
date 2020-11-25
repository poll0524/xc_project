package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.Reason;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface reasonMapper {
    //查询驳回理由
    List<Reason> selectReason(Integer start);
    //根据id查询
    Reason selectId(Integer reasonId);
}
