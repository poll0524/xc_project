package com.example.collecttoolserver.service.Impl;

import com.example.collecttoolserver.common.ReturnVO;
import com.example.collecttoolserver.common.WeChatUtil;
import com.example.collecttoolserver.pojo.ShareUser;
import com.example.collecttoolserver.pojo.Video;
import com.example.collecttoolserver.pojo.Xcuser;
import com.example.collecttoolserver.service.IShareService;
import com.example.collecttoolserver.util.WxQrCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.xml.stream.events.StartDocument;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.collecttoolserver.mapper.*;

@Service
public class ShareServiceImpl implements IShareService {
    @Autowired
    private shareUserMapper shareUserMapper;
    @Autowired
    private xcuserMapper xcuserMapper;
    @Autowired
    private videoMapper videoMapper;
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
        //获取title
        Video video = videoMapper.selectVideoId(id);
        //素材标题
        String title = video.getTitle();
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
            Integer share_id = shareUser.getShare_id();
            if (tell == 0){
                shareUserMapper.updataShareUserData(share_id,readTime,title);
            }else if (tell == 1){
                shareUserMapper.updataShareUsersData(share_id,readTime,2,title);
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
        Integer deg = Integer.parseInt((String) data.get("deg"));

        List<ShareUser> shareUsers = new ArrayList<>();
        if (deg == 0){
            //获取当前时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
            String time = "%"+df.format(new Date())+"%";
            shareUsers = shareUserMapper.selectShareUsers(openId,start,time,pag);
        }else if (deg == 1){
            Calendar calendar2 = Calendar.getInstance();
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            calendar2.add(Calendar.DATE, -1);
            String time = "%"+sdf2.format(calendar2.getTime())+"%";
            shareUsers = shareUserMapper.selectShareUsers(openId,start,time,pag);
        }else if (deg == 2){
            //获取当前时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
            String time = "%"+df.format(new Date())+"%";

            Calendar calendar2 = Calendar.getInstance();
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            calendar2.add(Calendar.DATE, -1);
            String endTime = "%"+sdf2.format(calendar2.getTime())+"%";
            shareUsers = shareUserMapper.selectShareUserss(openId,start,time,endTime,pag);
        }
        if (shareUsers.size() == 0){
            return null;
        }
        //整理数据
        List<Map<String,Object>> dataList = new ArrayList<>();
        for (ShareUser shareUser : shareUsers) {
            Xcuser xcuser = xcuserMapper.selectOpenId(shareUser.getRead_open_id());
            Map<String,Object> datas = new HashMap<>();
            datas.put("shareId",shareUser.getShare_id());
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

    /**
     * 更改电话状态
     */
    public Map<String,Object> updataPhoneTell(Map<String,Object> data){
        Integer shareId = (Integer) data.get("shareId");
        ShareUser shareUser = shareUserMapper.selectShareId(shareId);
        Integer tell = shareUser.getTell();
        Map<String,Object> datas = new HashMap<>();
        if (tell == 0){
            shareUserMapper.updataSharePhone(shareId,1);
            datas.put("tell",1);
            return datas;
        }else if (tell == 2){
            shareUserMapper.updataSharePhone(shareId,1);
            datas.put("tell",1);
            return datas;
        }else if (tell == 1){
            datas.put("tell",1);
            return datas;
        }
        return null;
    }

    /**
     * 查询分享信息
     */
    public Map<String,Object> selectShareUser(Map<String,Object> data){
        String shareOpenId = (String) data.get("shareOpenId");
        String readOpenId = (String) data.get("readOpenId");
        Integer start = Integer.parseInt((String) data.get("start"));
        Integer contentId = (Integer) data.get("id");
        ShareUser shareUser = shareUserMapper.selectShareUserData(shareOpenId,readOpenId,start,contentId);
        Map<String,Object> datas = new HashMap<>();
        if (shareUser == null){
            datas.put("report","无分享信息,请调用");
            return datas;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String readTime = df.format(new Date());
        shareUserMapper.updataShareTime(shareOpenId,readOpenId,start,contentId,readTime);
        datas.put("report","有分享信息,不用调用");
        return datas;
    }

    /**
     * 生成二维码图片
     */
    public Map<String,Object> getCode(Map<String,Object> datas){
        Integer id = (Integer) datas.get("id");
        String urls = (String) datas.get("urls");
        Video video = videoMapper.selectVideoId(id);
        //二维码图片
        String popCode = video.getPop_code();

        Map<String,Object> data=new HashMap<>();

        if (popCode != null){
            data.put("twoCodeUrl", popCode);
            data.put("cover","https://xiaocisw-video.oss-cn-chengdu.aliyuncs.com/6d127be4a0f38f895d77778dc08ea4c.jpg");
            return data;
        }

        String accessToken = null;
        try{
            accessToken = WxQrCodeUtil.getAccessToken();
            String twoCodeUrl = WxQrCodeUtil.getminiqrQr(accessToken, WeChatUtil.CONTENT_PATH,urls);
            videoMapper.updataPopCode(id,twoCodeUrl);
            data.put("twoCodeUrl", twoCodeUrl);
            data.put("cover","https://xiaocisw-video.oss-cn-chengdu.aliyuncs.com/6d127be4a0f38f895d77778dc08ea4c.jpg");
            return data;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
