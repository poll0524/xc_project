package com.example.datatoolserver.mapper;


import com.example.datatoolserver.pojo.Material;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface materialMapper {
    //写入产品对应素材表
    int insertMaterial(Integer articleId, Integer videoId, Integer productId);
    //删除素材对应产品
    int deleteMaterialArticle(Integer articleId);

    int deleteMaterialVideo(Integer videoId);

    //根据素材id查询产品
    List<Material> selectMaterialArticle(Integer articleId);

    List<Material> selectMaterialVideo(Integer videoId);
}
