package com.example.collecttoolserver.mapper;

import com.example.collecttoolserver.pojo.ShareUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface shareUserMapper {
    //写入阅读数据
    Integer insertShareUser(String shareOpenId,String readOpenId,Integer contentId,String readTime,Integer start,Integer tell,String title);
    //根据分享人openId查询
    List<ShareUser> selectShareUser(String openId,Integer start,Integer pag);
    //根据分享人和阅读人和start和素材id查询
    ShareUser selectShareUserData(String shareOpenId,String readOpenId,Integer start,Integer contentId);
    //根据分享人和阅读人和start和素材id更新
    Integer updataShareUserData(String shareOpenId,String readOpenId,Integer start,Integer contentId,String readTime);
    //根据分享人和阅读人和start和素材id重复更新
    Integer updataShareUsersData(String shareOpenId,String readOpenId,Integer start,Integer contentId,String readTime,Integer tell);
}
