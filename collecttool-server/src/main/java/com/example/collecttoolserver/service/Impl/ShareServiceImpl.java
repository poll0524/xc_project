package com.example.collecttoolserver.service.Impl;

import com.example.collecttoolserver.common.ReturnVO;
import com.example.collecttoolserver.pojo.ShareUser;
import com.example.collecttoolserver.pojo.Xcuser;
import com.example.collecttoolserver.service.IShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.stream.events.StartDocument;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.collecttoolserver.mapper.*;

@Service
public class ShareServiceImpl implements IShareService {
    @Autowired
    private shareUserMapper shareUserMapper;
    @Autowired
    private xcuserMapper xcuserMapper;
    /**
     * 写入分享
     */
    public String insertShare(Map<String,Object> data){
        //获取start:0为文章1为视频
        Integer start = Integer.parseInt((String) data.get("start"));
        //获取阅读人openId
        String readOpenId = (String) data.get("readOpenId");
        //获取分享人openId
        String shareOpenId = (String) data.get("shareOpenId");
        //获取id
        Integer id = (Integer) data.get("id");
        //素材标题
        String title = (String) data.get("title");
        //阅读时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String readTime = df.format(new Date()); // new Date()为获取当前系统时间
        //是否拨打过电话
        Integer tell = 0;
        ShareUser shareUser = shareUserMapper.selectShareUserData(shareOpenId,readOpenId,start,id);

        if (shareUser == null){
            shareUserMapper.insertShareUser(shareOpenId,readOpenId,id,readTime,start,tell,title);
        }else {
            tell = shareUser.getTell();
            if (tell == 0){
                shareUserMapper.updataShareUserData(shareOpenId,readOpenId,start,id,readTime);
            }else if (tell == 1){
                shareUserMapper.updataShareUsersData(shareOpenId,readOpenId,start,id,readTime,2);
            }
        }
        return "写入成功";
    }

    /**
     * 查询分享数据
     */
    public List<Map<String,Object>> selectShare(Map<String,Object> data){
        Integer start = Integer.parseInt((String) data.get("start"));
        String openId = (String) data.get("openId");
        Integer pag = Integer.parseInt((String) data.get("pag"))*10;
        List<ShareUser> shareUsers = shareUserMapper.selectShareUser(openId,start,pag);
        //整理数据
        List<Map<String,Object>> dataList = new ArrayList<>();
        for (ShareUser shareUser : shareUsers) {
            Xcuser xcuser = xcuserMapper.selectOpenId(shareUser.getRead_open_id());
            Map<String,Object> datas = new HashMap<>();
            datas.put("userHeadimgurl",xcuser.getUser_headimgurl());
            datas.put("userName",xcuser.getUser_name());
            datas.put("title",shareUser.getTitle());
            datas.put("readTime",shareUser.getRead_time());
            datas.put("userPhone",xcuser.getUser_phone());
            datas.put("tell",shareUser.getTell());
            dataList.add(datas);
        }
        return dataList;
    }
}
