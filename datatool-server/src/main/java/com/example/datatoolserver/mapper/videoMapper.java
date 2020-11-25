package com.example.datatoolserver.mapper;


import com.example.datatoolserver.pojo.Video;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface videoMapper {
    //写入一条视频
    int insertVideo(String mediaId, String videoName, String coverUrl, Integer videoClassify);

    //写入一条未发布视频
    int insertVideos(String videoName, String coverUrl, String downUrl, Integer videoClassify, Integer quantity, String digest, Integer videoLike, String author, Integer special, String showCoverPic, String thumbMediaId, String time);


    int updataVideos(Integer id, String videoName, String coverUrl, String downUrl, Integer videoClassify, Integer quantity, String digest, Integer videoLike, String author, String showCoverPic, String time);
    //根据video_id查询
    Video selectVideoId(Integer videoId);

    //阅读量+1
    int updataQuantity(Integer videoId);

    //点赞数+1
    int updataLike(Integer videoId);

    int updataLikeNo(Integer videoId);


    //查询所有视频
    List<Video> selectVideo();

    //查询所有视频
    List<Video> selectVideos(String show_cover_pic);
    List<Video> selectVideoss(Integer classifyId, String show_cover_pic);

    //查询视频列表
    List<Video> selectVideoTiele(Integer pag);
    //根据media_id更新视频
    int updataVideo(String mediaId, String downUrl);
    //根据media_id查询视频
    Video selectMediaIdVideo(String mediaId);

    //根据查询视频
    List<Video> selectClaVideo(Integer videoClassify, Integer pag);

    //根据标题模糊查询列表
    List<Video> selectVideoTitleList(String title, Integer pag);

    //随机查询2条视频
    List<Video> selectRaVideo();

    //根据分类查询2条视频
    List<Video> selectRaClVideo(Integer videoClassify);

    //根据专题查询视频
    List<Video> selectSpecialVideo(Integer pag);

    int updataShowCoverPic(Integer id, String show_cover_pic);

    List<Video> selectVideoTieleAdmin(Integer pag, Integer show_cover_pic);
    List<Video> selectVideoTieleAdmins(Integer pag, Integer classifyId, Integer show_cover_pic);

    //根据id删除
    int deleteVideo(Integer id);

    List<Video> selectVideoTitleListAdmin(String title, String show_cover_pic, Integer pag);
    List<Video> selectVideoTitleListAdmins(String title, String show_cover_pic);

    //查询所有视频
    List<Video> selectVideoSum();
}

