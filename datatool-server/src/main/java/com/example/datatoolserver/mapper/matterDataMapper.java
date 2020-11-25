package com.example.datatoolserver.mapper;


import com.example.datatoolserver.pojo.MatterData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface matterDataMapper {
    //查询数据
    List<MatterData> selectMatterData();

    //更新数据
    int updataMatterData(String matterName, Integer matterSum);
}
