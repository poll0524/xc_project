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
    List<ShareUser> selectShareUsers(String openId,Integer start,String time,Integer pag);
    List<ShareUser> selectShareUserss(String openId,Integer start,String time,String endTime,Integer pag);
    //根据分享人openId查询
    List<ShareUser> selectShareUserSum(String openId);
    //根据分享人和阅读人和start和素材id查询
    ShareUser selectShareUserData(String shareOpenId,String readOpenId,Integer start,Integer contentId);
    //根据分享人和阅读人和start和素材id更新
    Integer updataShareUserData(Integer shareId,String readTime,String title);
    //根据分享人和阅读人和start和素材id重复更新
    Integer updataShareUsersData(Integer shareId,String readTime,Integer tell,String title);
    //根据id更新电话状态
    Integer updataSharePhone(Integer shareId,Integer tell);
    //根据id查询分享数据
    ShareUser selectShareId(Integer shareId);

    Integer updataShareTime(String shareOpenId,String readOpenId,Integer start,Integer contentId,String readTime);
}
