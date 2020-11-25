package com.example.datatoolserver.mapper;


import com.example.datatoolserver.pojo.GroupTop;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface groupTopMapper {
    //查询团队表
    List<GroupTop> selectGroup(String openId);

    GroupTop selectGroupId(Integer groupId);

    //查询团队token
    List<GroupTop> selectGroupTokenList(String openId);

    //根据token来查询团队成员
    List<GroupTop> selectGroupToken(String groupToken);

    //根据token,openId,group_member_openid查询
    GroupTop selectGroupTokenId(String groupToken, String openId, String groupMemberOpenid);

    //根据openid和名字查询
    GroupTop selectGroupName(String openId, String groupName);

    //根据token和group_member_openid来更新显示
    int updataGroupShow(int groupId, int show);

    //根据token和group_member_openid来更新权值
    int updataGroupStart(int groupId, int start);

    int updataOpenIdShow(int show, String groupMemberOpenid);

    //更新查询出来的第一条数据
    int updataOneShow(int show, String groupMemberOpenid);

    //写入一条团队记录
    int insertGroup(String groupToken, String openId, String groupName, String groupMemberOpenid, Integer groupStart, Integer groupShow);

    //更新团队名称
    int updataGroup(String groupToken, String groupName);
    //删除成员
    int deleteGroup(int groupId);
}
