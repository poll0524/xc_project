package com.example.datatoolserver.mapper;


import com.example.datatoolserver.pojo.ShareVideo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface shareVideoMapper {
    ShareVideo selectShareVideo(String openId, Integer videoId);

    ShareVideo selectShareVid(String openId, Integer videoId);
    ShareVideo selectShareBid(String openId, Integer videoId);

    ShareVideo selectShareVids(String openId, Integer videoId);
    ShareVideo selectShareBids(String openId, Integer videoId);


    int updataShareVid(String openId, Integer videoId, String videoName, String coverUrl, String downUrl, Integer videoClassify, Integer brandVideoId, String advertising1, Integer quantity, String digest, Integer videoLike, String author, String popImg, Integer popCode);
    int updataShareBid(String openId, Integer videoId, String videoName, String coverUrl, String downUrl, Integer videoClassify, Integer brandVideoId, String advertising1, Integer quantity, String digest, Integer videoLike, String author, String popImg, Integer popCode);

    //点赞数+1
    int updataLike(String openId, Integer videoId);

    int updataLikeNo(String openId, Integer videoId);

    int updataLikeB(String openId, Integer brandVideoId);

    int updataLikeNoB(String openId, Integer brandVideoId);


    ShareVideo selectShareVideoId(Integer videoId);


    List<ShareVideo> selectShareVideoOpenId(String openId);

    List<ShareVideo> selectShareVideoGroup(String openId);

    List<ShareVideo> selectShareVideoGroups(List openId, Integer pag);

    List<ShareVideo> selectShareVideoOpenIdList(String openId, Integer pag);

    int insertShareVideo(String openId, Integer videoId, String videoName, String coverUrl, String downUrl, Integer videoClassify, Integer brandVideoId, String advertising1, Integer quantity, String digest, Integer videoLike, String author, String popImg, Integer popCode);

    int updataShareVideo(String openId, Integer videoId, String videoName);
}
