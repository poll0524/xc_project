package com.example.xcschoolserver.util;

import com.example.xcschoolserver.mapper.*;
import com.example.xcschoolserver.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassifyNav {
    @Autowired
    private userBrandMapper userBrandMapper;
    @Autowired
    private brandNavMapper brandNavMapper;
    @Autowired
    private brandClassifyMapper brandClassifyMapper;
    @Autowired
    private userClassifyMapper userClassifyMapper;
    @Autowired
    private articleClassifyMapper articleClassifyMapper;


    /**
     * 用户选择的品牌
     */
    public List<Map<String,Object>> userBrand(String openId){
        List<Map<String,Object>> dataList = new ArrayList<>();
        //查询用户选择的品牌
        List<UserBrand> userBrands = userBrandMapper.selectUserBrands(openId);

        if (userBrands.size() == 0){
            return null;
        }else {

            for (UserBrand userBrand : userBrands) {
                //查询品牌的名称
                BrandNav brandNav = brandNavMapper.selectBrandId(userBrand.getBrand_id());
                //通过品牌查询二级分类
                List<BrandClassify> brandClassifies = brandClassifyMapper.selectBrandClassify(userBrand.getBrand_id());
                Map<String,Object> datas = new HashMap<>();
                if (brandClassifies.size() == 0){
                    datas.put("id",brandNav.getBrand_id());
                    datas.put("name",brandNav.getBrand_name());
                    List<String> a = new ArrayList<>();
                    datas.put("classify",a);
                    datas.put("start",1);
                }else {
                    datas.put("id",brandNav.getBrand_id());
                    datas.put("name",brandNav.getBrand_name());
                    List<Map<String,Object>> classifyList = new ArrayList<>();
                    for (BrandClassify brandClassify : brandClassifies) {
                        Map<String,Object> classifydatas = new HashMap<>();
                        classifydatas.put("classifyId",brandClassify.getClassify_id());
                        classifydatas.put("classifyName",brandClassify.getBrand_classify_name());
                        classifyList.add(classifydatas);
                    }
                    datas.put("classify",classifyList);
                    datas.put("start",1);
                }
                //将计算出来的用户选择的品牌加入到返回的数据中
                dataList.add(datas);
            }
        }
        return dataList;
    }


    /**
     * 用户选择的分类
     */
    public List<Map<String,Object>> userClassify(String openId){
        List<Map<String,Object>> dataList = new ArrayList<>();
        //查询用户选择的分类
        List<UserClassify> userClassifies = userClassifyMapper.selectUserClassify(openId);
        if (userClassifies.size() == 0){

        }else {
            for (UserClassify userClassify : userClassifies) {
                //通过用户选择的分类到分类表中查询分类详情
                ArticleClassify articleClassify = articleClassifyMapper.selectIdArticle(userClassify.getClassify_id());
                Map<String,Object> datas = new HashMap<>();
                datas.put("id",articleClassify.getClassify_id());
                datas.put("name",articleClassify.getClassify_name());
                List<String> a = new ArrayList<>();
                datas.put("classify",a);
                datas.put("start",0);
                //将计算出来的用户选择的系统分类加入到返回的数据中
                dataList.add(datas);
            }
        }
        return dataList;
    }
}
