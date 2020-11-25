package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.BrandCourse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface brandCourseMapper {
    //根据品牌名称查找全部
    List<BrandCourse> selectBrandName(String brandName, Integer pag);
    //根据品牌和二级分类查询课程
    List<BrandCourse> selectClassify(String brandName, String classify, Integer pag);

    List<BrandCourse> selectBrandNames(String brandName, Integer pag, String openId);
    //根据品牌和二级分类查询课程
    List<BrandCourse> selectClassifys(String brandName, String classify, Integer pag, String openId);

    //根据课程主键id查询
    BrandCourse selectCourseId(Integer courseId);

    //根据openid查询
    List<BrandCourse> selectOpenId(String openId, Integer pag);

    //根据品牌id和二级分类名称更新二级分类名称
    int updataCourseClassify(Integer brandId, String classify);

    //根据品牌id删除课程
    int deleteBrandId(Integer brandId);

    //阅读量+1
    int updataQuantity(Integer courseId);

    //根据id更新品牌分类
    int updataBrandName(Integer courseId, Integer brandId, String author, String classify);

    //获取课程数量
    List<BrandCourse> selectBrandCourseSum();

    List<BrandCourse> selectTopBrandCourse();
}
