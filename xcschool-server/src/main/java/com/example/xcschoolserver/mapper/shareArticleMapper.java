package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.ShareArticle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface shareArticleMapper {
    //查询所有
    ShareArticle selectShareArtList(String openId, Integer id);

    ShareArticle selectShareBraList(String openId, Integer id);

    int deleteShare(Integer id);

    ShareArticle selectShareArtLists(String openId, Integer shareId);

    List<ShareArticle> selectShareArtOpenId(String openId);
    List<ShareArticle> selectShareArtOpenIds(String openId, Integer pag);

    List<ShareArticle> selectShareArtGroup(String openId);

    List<ShareArticle> selectShareArtGroupInfo(String openId);

    List<ShareArticle> selectShareArtGroupInfos(List openId, Integer pag);

    List<ShareArticle> selectShareArtOpenIdList(String openId, Integer pag);

    int insertShareArticle(String openId, String title, String thumbMediaId, String author, String digest, String content, String url, String coverImg, Integer showCoverPic, Integer articleClassify, String advertising1, Integer brandArticleId, Integer articleId);

    int updataShareArticle(String openId, String title, String thumbMediaId, String author, String digest, String content, String url, String coverImg, Integer showCoverPic, Integer articleClassify, String advertising1, Integer brandArticleId, Integer articleId);
    int updataShareArticleB(String openId, String title, String thumbMediaId, String author, String digest, String content, String url, String coverImg, Integer showCoverPic, Integer articleClassify, String advertising1, Integer brandArticleId, Integer articleId);

    //根据id查询
    ShareArticle selectShareArtId(int share_id);

    ShareArticle selectShareVid(String openId, Integer articleId);

    ShareArticle selectShareBid(String openId, Integer brandArticleId);

    ShareArticle selectShareVids(String openId, Integer articleId);

    ShareArticle selectShareBids(String openId, Integer brandArticleId);





    ShareArticle selectSharebraId(int share_id);

    //根据openId和封面查询
    ShareArticle selectShareCover(String openId, String coverImg);
}
