package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.ShareUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface shareUserMapper {
    int insertShareUser(String openId, String readOpenId, Integer videoId, Integer articleId, Integer courseId, String readTimeBegin, Integer start, Integer deg, Integer tell);

    ShareUser selectShareUserMed(String openId, String readId, String mediaId);

    ShareUser selectShareUserMeds(String openId, String readId, Integer articleId, Integer tell);

    ShareUser selectShareUserVid(String openId, String readId, Integer videoId, Integer tell);

    int updataShareId(Integer shareId, Integer deg);

    List<ShareUser> selectShareUserList(String openId);

    List<ShareUser> selectShareArticle(String openId, Integer pag);

    List<ShareUser> selectShareVideo(String openId, Integer pag);

    List<ShareUser> selectShareCourse(String openId, Integer pag);

    List<ShareUser> selectReadUserList(String openId, String readId);


    List<ShareUser> selectReadUser(String openId, String readId, Integer start);

    List<ShareUser> selectMediaId(String openId, Integer articleId);


    List<ShareUser> selectVideoId(String openId, Integer videoId);


    List<ShareUser> selectArticle(String openId);
    List<ShareUser> selectVideo(String openId);


    ShareUser selectShareUserCid(String openId, String readId, Integer courseId, Integer tell);

    List<ShareUser> selectShareUser();
}
