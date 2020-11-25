package com.example.datatoolserver.service.impl;

import com.example.datatoolserver.common.WeChatUtil;
import com.example.datatoolserver.mapper.*;
import com.example.datatoolserver.pojo.*;
import com.example.datatoolserver.service.IArticleService;
import com.example.datatoolserver.service.IWeixinInertfaceService;
import com.example.datatoolserver.util.AccessTokenUtil;
import com.example.datatoolserver.util.UserInfo;
import com.nimbusds.jose.JOSEException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.datatoolserver.util.UserInfo;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("xcuser")
public class WeixinInertfaceServiceIpml implements IWeixinInertfaceService {
    @Autowired
    private AccessTokenUtil accessTokenUtil;
    @Autowired
    private xcuserMapper xcuserMapper;
    @Autowired
    private groupTopMapper groupTopMapper;
    @Autowired
    private sentenceMapper sentenceMapper;
    @Autowired
    private shareUserMapper shareUserMapper;
    @Autowired
    private brandNavMapper brandNavMapper;
    @Autowired
    private shareArticleMapper shareArticleMapper;
    @Autowired
    private shareVideoMapper shareVideoMapper;
    @Autowired
    private IArticleService articleService;

    @Autowired
    private professionMapper professionMapper;

    @Autowired
    private industryMapper industryMapper;
    @Autowired
    private UserInfo userInfosss;

    /**
     * 创建微信菜单
     */
    public String createMenu(){
        return null;
    }


//    public Map login(String code) throws JSONException {
//        //通过code换去access_token
//        Map<String,Object> jsonObject = accessTokenUtil.getWebaccessTokens(code);
////        System.out.println(jsonObject);
//        String access_token = (String) jsonObject.get("access_token");
//
////        System.out.println(access_token);
//        String openid = (String) jsonObject.get("openId");
//
//        Map map = new HashMap();
//        map.put("openid",openid);
//        map.put("access_token",access_token);
//        map.put("data","该用户登录成功");
//
//        System.out.println("打开网页");
//
//        return map;
//    }
//
//    public Map logins(Map<String,Object> data) throws JSONException{
//        String access_token = (String) data.get("access_token");
//
////        System.out.println(access_token);
//        String openid = (String) data.get("openId");
//
//        //通过access_token和openId拉取用户信息
//        JSONObject userinfo = accessTokenUtil.getUserInfo(access_token,openid);
//        String openId = userinfo.getString("openid");
//        Xcuser usercount = xcuserMapper.selectOpenId(openId);
//        Map map = new HashMap();
//        if(usercount == null){
//            String userName =  userinfo.getString("nickname");
////            String userName =  "骁爷";
//            String userSex = userinfo.getString("sex");
//            String userProvince = userinfo.getString("province");
//            String userCity = userinfo.getString("city");
//            String userCountry = userinfo.getString("country");
//            String userHeadimgurl = userinfo.getString("headimgurl");
//            Integer sign = 0;
//            xcuserMapper.insertUser(openId,userName,userSex,userProvince,userCity,userCountry,userHeadimgurl,sign,0);
//            brandNavMapper.insertBrand(openId, WeChatUtil.AUTHOR,1);
//            map.put("openid",userinfo);
//            map.put("data","该用户注册成功");
//            Long time = new Date().getTime();
//            System.out.println(time);
//            return map;
//        }
//        map.put("openid",usercount);
//        map.put("data","该用户登录成功");
//        Long time = new Date().getTime();
//        System.out.println(time);
//
//        return map;
//
//    }





    /**
     * 登录获取用户信息
     * @return
     */
    public Map login(String code) throws JSONException {
        //通过code换去access_token
        Map<String,Object> jsonObject = accessTokenUtil.getWebaccessTokens(code);
//        System.out.println(jsonObject);
        String access_token = (String) jsonObject.get("access_token");

//        System.out.println(access_token);
        String openid = (String) jsonObject.get("openId");
        System.out.println(openid);
        //通过access_token和openId拉取用户信息
        JSONObject userinfo = accessTokenUtil.getUserInfo(access_token,openid);
        String openId = userinfo.getString("openid");
        Xcuser usercount = xcuserMapper.selectOpenId(openId);
        Map map = new HashMap();
        if(usercount == null){
            Xcuser userData = userInfosss.insertUser(userinfo);
            map.put("userInfo",userData);
            map.put("data","该用户注册成功");
//            System.out.println("注册成功");
            return map;
        }
        Xcuser userData = userInfosss.updataUser(userinfo,usercount);
        map.put("userInfo",userData);
        map.put("data","该用户登录成功");
//        System.out.println("登录成功");
        return map;
    }




    /**
     * 查询所有用户,计算签到天数
     * @return
     */
    public List<Xcuser> selectxcuser(){
        return xcuserMapper.selectxcuser();
    }

    /**
     * 更新用户数据
     */
    public int updataxcuser(String openId,Integer sign){
        return xcuserMapper.updataXcuser(openId,sign);
    }

    /**
     * 根据openId查询用户信息
     */
    public Xcuser selectUser(String openId){

        Xcuser xcuser = xcuserMapper.selectOpenId(openId);

        if(xcuser.getUser_phone() == null){
            xcuser.setUser_phone("");
        }
        if(xcuser.getCode_url() == null){
            xcuser.setCode_url("");
        }
        if (xcuser.getStart() == null){
            xcuserMapper.updataUserStart(openId,0);
            xcuser.setStart(0);
        }
        if(xcuser.getBegin_time() != null && xcuser.getEnd_time().equals("0")){
            String beginTime = xcuser.getBegin_time().split("\\s+")[0];
            String endTime = xcuser.getEnd_time().split("\\s+")[0];
            xcuser.setBegin_time(beginTime);
            xcuser.setEnd_time(endTime);
        }else if (xcuser.getBegin_time() != null && xcuser.getEnd_time() != null){
            String beginTime = xcuser.getBegin_time().split("\\s+")[0];
            String endTime = xcuser.getEnd_time().split("\\s+")[0];
            xcuser.setBegin_time(beginTime);
            xcuser.setEnd_time(endTime);
        }
        if (xcuser.getEnd_time() == null){
            return xcuser;
        }else if (xcuser.getEnd_time().equals("0")){
            xcuser.setEnd_time("永久");
        }
        return xcuser;
    }

    /**
     * 根据openId更新电话号码
     */
    public String updataUserPhone(Map<String,Object> userPhone){
        String openId = (String) userPhone.get("openId");

        String phone = (String) userPhone.get("userdata");

        if(xcuserMapper.selectOpenId(openId) == null){
            return "没有该用户";
        }
        xcuserMapper.updataUserPhone(openId,phone);

        return "添加成功";
    }

    /**
     * 根据openId更新形象照
     */
    public String updataUserSelfPortrait(Map<String,Object> userPhone){
        String openId = (String) userPhone.get("openId");

        String selfPortrait = (String) userPhone.get("userdata");

        if(xcuserMapper.selectOpenId(openId) == null){
            return "没有该用户";
        }
        xcuserMapper.updataUserSelfPortrait(openId,selfPortrait);

        return "添加成功";
    }

    /**
     * 根据openId更新公司信息
     */
    public String updataUserCompany(Map<String,Object> userPhone){
        String openId = (String) userPhone.get("openId");

        String company = (String) userPhone.get("userdata");

        if(xcuserMapper.selectOpenId(openId) == null){
            return "没有该用户";
        }
        xcuserMapper.updataUserCompany(openId,company);

        return "添加成功";
    }

    /**
     * 根据openId更新微信号
     */
    public String updataUserWeChat(Map<String,Object> userPhone){
        String openId = (String) userPhone.get("openId");

        String weChat = (String) userPhone.get("userdata");

        if(xcuserMapper.selectOpenId(openId) == null){
            return "没有该用户";
        }
        xcuserMapper.updataUserWeChat(openId,weChat);

        return "添加成功";
    }


    /**
     * 根据openId更新邮箱
     */
    public String updataUserEmail(Map<String,Object> userPhone){
        String openId = (String) userPhone.get("openId");

        String email = (String) userPhone.get("userdata");

        if(xcuserMapper.selectOpenId(openId) == null){
            return "没有该用户";
        }
        xcuserMapper.updataUserEmail(openId,email);

        return "添加成功";
    }

    /**
     * 根据openId更新行业
     */
    public String updataUserBusiness(Map<String,Object> userPhone){
        String openId = (String) userPhone.get("openId");

        String business = (String) userPhone.get("userdata");

        if(xcuserMapper.selectOpenId(openId) == null){
            return "没有该用户";
        }
        xcuserMapper.updataUserBusiness(openId,business);

        return "添加成功";
    }

    /**
     * 根据openId更新职业
     */
    public String updataUserPosition(Map<String,Object> userPhone){
        String openId = (String) userPhone.get("openId");

        String position = (String) userPhone.get("userdata");

        if(xcuserMapper.selectOpenId(openId) == null){
            return "没有该用户";
        }
        xcuserMapper.updataUserPosition(openId,position);

        return "添加成功";
    }



    /**
     * 查询用户行业
     * @return
     */
    public List<Map<String,Object>> profession(){
        //查询所有行业
        List<Industry> industrys = industryMapper.selectIndustry();
        List<Map<String,Object>> dataInfo = new ArrayList<>();
        for (Industry industry : industrys) {
            //根据行业查询职业
            List<Profession> professions = professionMapper.selectProfession(industry.getIndustry_id());
            List<String> professionList = new ArrayList<>();
            for (Profession profession : professions) {
                professionList.add(profession.getProfession_name());
            }
            //整理数据
            Map<String,Object> datas = new HashMap<>();

            datas.put("industrie",industry.getIndustry_name());
            datas.put("profession",professionList);
            dataInfo.add(datas);
        }
        return dataInfo;
    }



    /**
     * 创建团队
     */
    @Override
    public Map<String,Object> insetGroup(Map<String,Object> group){
        String openId = (String) group.get("openId");
        String groupMemberOpenid = openId;
        String groupName = (String) group.get("groupName");
        int start = 0;
        int show = 0;
        if(groupTopMapper.selectGroupName(openId,groupName)!=null){
            Map<String,Object> data = new HashMap<>();
            data.put("info","该团队已经存在");
            return data;
        }
        //生成团队唯一值
        Map<String,Object> map = new HashMap<>();
        //建立载荷,这些数据根据业务,自己定义
        map.put("openId",openId);
        map.put("groupName",groupName);
        //生成时间
        map.put("sta", new Date().getTime());
        //过期时间
        map.put("exp", new Date().getTime() + 6);
        try {
            String token = com.example.datatoolserver.util.token.creatToken(map);
            List<GroupTop> groupTops = groupTopMapper.selectGroup(openId);
            if (groupTops != null){
                for (GroupTop groupTop : groupTops) {
                    if (groupTop.getGroup_show() == 0){
                        groupTopMapper.updataGroupShow(groupTop.getGroup_id(),1);
                        break;
                    }
                }
            }
            groupTopMapper.insertGroup(token,openId,groupName,groupMemberOpenid,start,show);
            return selectGroup(openId);
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 查询团队详情(管理团队)
     */
    public Map<String,Object> selectGroupInfo(Map<String,Object> data){
        //获取团队token
        String groupToken = (String) data.get("groupToken");
        //获取openId;
        String openId = (String) data.get("openId");
        //根据groupToken查询该团队所有成员
        List<GroupTop> groupTops = groupTopMapper.selectGroupToken(groupToken);
        //整理该团队数据返回
        Map<String,Object> group = new HashMap<>();
        List<Map<String,Object>> userList = new ArrayList<>();
        String groupName = "";
        for (GroupTop groupTop : groupTops) {
            groupName = groupTop.getGroup_name();
            Xcuser xcuser = xcuserMapper.selectOpenId(groupTop.getGroup_member_openid());
            Map<String,Object> map = new HashMap<>();
            map.put("openId",xcuser.getOpen_id());
            map.put("head",xcuser.getUser_headimgurl());
            map.put("userName",xcuser.getUser_name());
            map.put("start",groupTop.getGroup_start());
            map.put("show",groupTop.getGroup_show());
            map.put("groupId",groupTop.getGroup_id());
            if (openId.equals(groupTop.getGroup_member_openid())){
                map.put("self",true);
            }else {
                map.put("self",false);
            }
            userList.add(map);
        }
        group.put("groupName",groupName);
        group.put("groupUser",userList);
        group.put("size",userList.size());
        return group;
    }



    /**
     * 查询团队(团队数据追踪)
     */
    public Map<String, Object> selectGroup(String openId){
        //通过openid查询团队数量
        List<GroupTop> groupTops = groupTopMapper.selectGroup(openId);

        //判断团队是否为空
        if(groupTops.isEmpty()){
            return null;
        }
        //创建空对象用于返回数据
        Map<String,Object> groupMap = new HashMap<>();
        List groupList = new ArrayList();

        List<GroupTop> group = new ArrayList<>();


        if (groupTops != null){//团队不为空
            for (GroupTop groupTop : groupTops) {//循环每一个团队
                if (groupTop.getGroup_show() == 0){//
                    //根据token查出团队成员名称
                    group = groupTopMapper.selectGroupToken(groupTop.getGroup_token());

                    for (GroupTop top : group) {
                        // 存放用户信息+团队信息
                        Map<String,Object> groupA = new HashMap<>();
                        groupA.put("groupId",top.getGroup_id());
                        groupA.put("groupToken",top.getGroup_token());
                        groupA.put("groupName",top.getGroup_name());
                        groupA.put("openId",top.getGroup_member_openid());
                        groupA.put("groupStart",top.getGroup_start());
                        groupA.put("groupShow",top.getGroup_show());
                        groupA.put("userinfo",selectUser(top.getGroup_member_openid()));
                        List<ShareUser> shareUsers = shareUserMapper.selectShareUserList(top.getGroup_member_openid());
                        Integer article = 0;
                        Integer video = 0;
                        Integer course = 0;
                        for (ShareUser shareUser : shareUsers) {
                            if (shareUser.getVideo_id() != null){
                                ShareVideo shareVideo = new ShareVideo();
                                if (shareUser.getTell() == 0){
                                   shareVideo = shareVideoMapper.selectShareVid(shareUser.getOpen_id(),shareUser.getVideo_id());
                                }else if (shareUser.getTell() == 1){
                                    shareVideo = shareVideoMapper.selectShareBid(shareUser.getOpen_id(),shareUser.getVideo_id());
                                }
                                if (shareVideo == null){
                                    continue;
                                }
                                video = video + 1;
                            }else if (shareUser.getArticle_id() != null){
//                                ShareArticle shareVideo = new ShareArticle();
//                                if (shareUser.getTell() == 0){
//                                    shareVideo = shareArticleMapper.selectShareVid(shareUser.getOpen_id(),shareUser.getArticle_id());
//                                }else if (shareUser.getTell() == 1){
//                                    shareVideo = shareArticleMapper.selectShareBid(shareUser.getOpen_id(),shareUser.getArticle_id());
//                                }
//                                if (shareVideo == null){
//                                    continue;
//                                }
                                article = article + 1;
                            }else if (shareUser.getCourse_id() != null){
                                course = course + 1;
                            }
                        }
                        groupA.put("videoOrder",video);
                        groupA.put("articleOrder",article);
                        groupA.put("courseOrder",course);
                        groupList.add(groupA);
                    }
                    break;
                }
            }
        }
        groupMap.put("group",groupTops);
        groupMap.put("groupList",groupList);
        return groupMap;
    }

    /**
     * 查询团队列表
     */
    public List selectGroupName(Map<String,Object> data){
        String openId = (String) data.get("openId");
        //根据openId查询团队列表
        List<GroupTop> groupTops = groupTopMapper.selectGroup(openId);
        if (groupTops.size() == 0){
            return null;
        }
        List<Map<String,Object>> datalist = new ArrayList<>();
        //整理数据
        for (GroupTop groupTop : groupTops) {
            Map<String,Object> datas = new HashMap<>();
            datas.put("groupId",groupTop.getGroup_id());
            datas.put("groupName",groupTop.getGroup_name());
            datas.put("groupToken",groupTop.getGroup_token());
            datalist.add(datas);
        }
        return datalist;
    }

    /**
     *  切换团队
     */
    public Map<String, Object> switchGroup(Map<String,Object> group){
        int groupId = (int) group.get("groupId");
        String openId = (String) group.get("openId");
        //根据openId查询团队
        List<GroupTop> groupTops = groupTopMapper.selectGroup(openId);
        for (GroupTop groupTop : groupTops) {
            if(groupTop.getGroup_show() == 0){
                groupTopMapper.updataGroupShow(groupTop.getGroup_id(),1);
                break;
            }
        }
        groupTopMapper.updataGroupShow(groupId,0);
        return selectGroup(openId);
    }

    /**
     * 添加成员
     */
    public String insertGroupMember(Map<String,Object> group){
        String groupToken = (String) group.get("groupToken");
        String groupName = (String) group.get("groupName");
        String openId = (String) group.get("openId");
        String groupMemberOpenid = (String) group.get("groupMemberOpenid");

        GroupTop groupTopA = groupTopMapper.selectGroupTokenId(groupToken,openId,groupMemberOpenid);
        if(selectUser(groupMemberOpenid) == null){
            return "没有该用户信息";
        }else if(groupTopA != null){
            return "已经是该团队成员";
        }else {
            List<GroupTop> groupTops = groupTopMapper.selectGroup(groupMemberOpenid);
            if(groupTops != null){
                for (GroupTop groupTop : groupTops) {
                    if (groupTop.getGroup_show() == 0){
                        groupTopMapper.updataGroupShow(groupTop.getGroup_id(),1);
                        break;
                    }
                }
            }

            groupTopMapper.insertGroup(groupToken,openId,groupName,groupMemberOpenid,2,0);
            return "添加成功";
        }

    }

    /**
     * 权限设置
     * @return
     */
    public Map<String, Object> startGroup(Map<String,Object> start){
        int groupId = (int) start.get("groupId");
        Integer groupStart = Integer.parseInt((String) start.get("groupStart"));
        //获取团队token
        String groupToken = (String) start.get("groupToken");
        //获取openId;
        String openId = (String) start.get("openId");

        groupTopMapper.updataGroupStart(groupId,groupStart);

        return selectGroupInfo(start);
    }


    /**
     * 删除成员
     */
    public String deleteGroup(List<Map<String,Object>> groups){
        for (Map<String, Object> group : groups) {
            String openId = (String) group.get("openId");
            Integer show = (Integer) group.get("show");
            Integer groupId = (Integer) group.get("groupId");
            groupTopMapper.deleteGroup(groupId);
            List<GroupTop> groupTops = groupTopMapper.selectGroup(openId);

            if(groupTops.size() == 1){
                groupTopMapper.updataOpenIdShow(0,openId);
                continue;
            }else if (show == 0 && groupTops.size() > 1){
                for (GroupTop groupTop : groupTops) {
                    if (groupTop.getGroup_show() == 0){
                        continue;
                    }
                }
                groupTopMapper.updataOneShow(0,openId);

            }else if (show == 1 && groupTops.size() > 1){
                for (GroupTop groupTop : groupTops) {
                    if (groupTop.getGroup_show() == 0){
                        continue;
                    }
                }
                groupTopMapper.updataOneShow(0,openId);
                continue;
            }
        }

        return "删除成功";
    }

    /**
     * 退出团队
     */
    public String exitGroup(Map<String,Object> data){
        Integer groupId = (Integer) data.get("groupId");
        String openId = (String) data.get("openId");
        Integer show = (Integer) data.get("show");
        groupTopMapper.deleteGroup(groupId);

        List<GroupTop> groupTops = groupTopMapper.selectGroup(openId);
        if(groupTops.size() == 1){
            groupTopMapper.updataOpenIdShow(0,openId);
        }else if (show == 0 && groupTops.size() > 1){
            for (GroupTop groupTop : groupTops) {
                if (groupTop.getGroup_show() == 0){
                    break;
                }
            }
            groupTopMapper.updataOneShow(0,openId);
        }else if (show == 1 && groupTops.size() > 1){
            for (GroupTop groupTop : groupTops) {
                if (groupTop.getGroup_show() == 0){
                    break;
                }
            }
            groupTopMapper.updataOneShow(0,openId);
        }
        return "退出成功";
    }

    /**
     * 解散团队
     * @param group
     * @return
     */
    public String quitGroup(Map<String,Object> group){
        Integer groupId = (Integer) group.get("groupId");
        GroupTop groupTop = groupTopMapper.selectGroupId(groupId);

        String openId = groupTop.getOpen_id();
        String me = groupTop.getGroup_member_openid();

        if (openId.equals(me)){
            List<GroupTop> groupTopList = groupTopMapper.selectGroupToken(groupTop.getGroup_token());

            for (GroupTop top : groupTopList) {
                groupTopMapper.deleteGroup(top.getGroup_id());
            }

            groupTopMapper.updataOneShow(0,openId);


            return "解散成功";
        }

        return "不是创始人";
    }




    /**
     * 随机名人名言
     */
    public Map<String,Object> sign(String openId){
        Map<String,Object> data = new HashMap();
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sde = new SimpleDateFormat("HH:mm:ss");
        String date = sdf.format(d);
        String time = sde.format(d);

        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

;
        data.put("userinfo",selectUser(openId));
        data.put("date",date);
        data.put("time",time);
        data.put("week",weekDays[w]);
        data.put("sentence",sentenceMapper.selectOneSentence().getSentence());
        return data;
    }


    /**
     * 微信消息回复
     */
    public Object handleMessage(InTestMessage inTestMessage) throws JSONException, JOSEException {
        Boolean flag = true;
        //创建消息实体类
        OutTestMessage outTestMessage = new OutTestMessage();
        //发送发
        outTestMessage.setFromUserName(inTestMessage.getToUserName());
        //接收方
        outTestMessage.setToUserName(inTestMessage.getFromUserName());
        //创建消息创建时间
        outTestMessage.setCreateTime(new Date().getTime());
        String msgType = inTestMessage.getMsgType();
        if (msgType.equals("text")){//自动回复
            //设置消息类型
            outTestMessage.setMsgType("text");
            //设置消息内容
            if(inTestMessage.getContent().contains("营销")){
                String a = "\uD83D\uDC49<a href='http://web.xiaocisw.site/?list=%27%E6%8E%A8%E8%8D%90%27&num=0'>营销爆文</a>";
                String b = "\uD83D\uDC49<a href='http://web.xiaocisw.site/?list=%27%E6%8E%A8%E8%8D%90%27&num=1'>营销视频</a>";
                accessTokenUtil.staffTxt(a,inTestMessage.getFromUserName());
                accessTokenUtil.staffTxt(b,inTestMessage.getFromUserName());
                return null;
            }else if (inTestMessage.getContent().indexOf("https://mp.weixin.qq.com") != -1){//为公众号文章链接时
                //发送消息
                accessTokenUtil.staffTxt("文章采集中请稍候...采集完成后,请点开文章进行保存!!!!",inTestMessage.getFromUserName());

                //调用采集文章方法
                Map data = articleService.getArticles(inTestMessage.getContent(),inTestMessage.getFromUserName());

                //回复图文
                outTestMessage.setMsgType("news");
                //图文个数
                outTestMessage.setArticleCount(1);
                //设置图文明细列表
                ArticleItem item = new ArticleItem();
                item.setTitle((String) data.get("title"));
                item.setPicUrl((String) data.get("coverImg"));
                item.setDescription((String) data.get("title"));
                item.setUrl((String) data.get("url"));
                outTestMessage.setItem(new ArticleItem[]{item});
            }else if (inTestMessage.getContent().indexOf("抖音") != -1 || inTestMessage.getContent().indexOf("douyin") != -1){//为抖音视频时
                //发送消息
                accessTokenUtil.staffTxt("视频采集中请稍候...采集完成后,请点开视频进行保存!!!!",inTestMessage.getFromUserName());

                //调用采集视频方法
                Map<String,Object> data = articleService.getVideos(inTestMessage.getContent(),inTestMessage.getFromUserName());

                //回复图文
                outTestMessage.setMsgType("news");
                //图文个数
                outTestMessage.setArticleCount(1);
                //设置图文明细列表
                ArticleItem item = new ArticleItem();
                item.setTitle((String) data.get("title"));
                item.setPicUrl((String) data.get("coverImg"));
                item.setDescription((String) data.get("title"));
                item.setUrl((String) data.get("url"));
                outTestMessage.setItem(new ArticleItem[]{item});
            }else if (inTestMessage.getContent().indexOf("快手") != -1 || inTestMessage.getContent().indexOf("kuaishou") != -1){//为抖音视频时
                //发送消息
                accessTokenUtil.staffTxt("视频采集中请稍候...采集完成后,请点开视频进行保存!!!!",inTestMessage.getFromUserName());

                //调用采集视频方法
                Map<String,Object> data = articleService.getVideos(inTestMessage.getContent(),inTestMessage.getFromUserName());

                //回复图文
                outTestMessage.setMsgType("news");
                //图文个数
                outTestMessage.setArticleCount(1);
                //设置图文明细列表
                ArticleItem item = new ArticleItem();
                item.setTitle((String) data.get("title"));
                item.setPicUrl((String) data.get("coverImg"));
                item.setDescription((String) data.get("title"));
                item.setUrl((String) data.get("url"));
                outTestMessage.setItem(new ArticleItem[]{item});
            }
//            else if(inTestMessage.getContent().contains("地址")){
//                outTestMessage.setContent("广元");
//            }else {
//                outTestMessage.setContent(inTestMessage.getContent());
//            }


        }else if(msgType.equals("image")){
            //设置消息类型
            outTestMessage.setMsgType("image");
            //设置消息内容
            outTestMessage.setMediaId(new String[]{inTestMessage.getMediaId()});

        }else if(msgType.equals("event")){
            if (inTestMessage.getEvent().equals("subscribe")){
                List<OutTestMessage> list = new ArrayList<>();
                //回复文本

//                outTestMessage.setMsgType("text");
//                outTestMessage.setContent("欢迎光临!!!");
                Xcuser xcuser = xcuserMapper.selectOpenId(inTestMessage.getFromUserName());
                String str = "";
                if(xcuser != null){
                     str = xcuser.getUser_name()+"，你好。\n" +
                            "\n好开心，有你加入，营销如此轻松。\n" +
                            "\n" +
                            "\uD83D\uDCE2<a href='http://web.xiaocisw.site/seniorMember'>百倍业绩裂变,尽在来找客~</a>\n" +
                            " \n" +
                            "\uD83D\uDD09我们为你提供\n" +
                            "\n" +
                            "➡广告、微信、电话植入功能\n" +
                            "➡百业引流，自动插入你的广告\n" +
                            "➡精准锁定客户，及时提醒沟通\n" +
                            "➡分享推广，业绩爆单助人助己\n" +
                            " \n" +
                            "\uD83D\uDC49<a href='http://web.xiaocisw.site/personalInfo'>呐，我介绍完了，到你做一个自我介绍了，点击我设置你的个人专属名片吧</a>\n" +
                            " \n" +
                            "\uD83D\uDE4B\uD83C\uDFFB\u200D♀️悄悄告诉你，现在回复【营销】可以获取更多\uD83D\uDCA5营销爆文！或者到菜单栏看看，总有惊喜在等你哦！";
                }else {
                    str = "亲爱的用户,您好。\n" +
                            "\n好开心，有你加入，营销如此轻松。\n" +
                            "\n" +
                            "\uD83D\uDCE2<a href='http://web.xiaocisw.site/seniorMember'>百倍业绩裂变,尽在来找客~</a>\n" +
                            " \n" +
                            "\uD83D\uDD09我们为你提供\n" +
                            "\n" +
                            "➡广告、微信、电话植入功能\n" +
                            "➡百业引流，自动插入你的广告\n" +
                            "➡精准锁定客户，及时提醒沟通\n" +
                            "➡分享推广，业绩爆单助人助己\n" +
                            " \n" +
                            "\uD83D\uDC49<a href='http://web.xiaocisw.site/personalInfo'>呐，我介绍完了，到你做一个自我介绍了，点击我设置你的个人专属名片吧</a>\n" +
                            " \n" +
                            "\uD83D\uDE4B\uD83C\uDFFB\u200D♀️悄悄告诉你，现在回复【营销】可以获取更多\uD83D\uDCA5营销爆文！或者到菜单栏看看，总有惊喜在等你哦！";
                }
                accessTokenUtil.staffTxt(str,inTestMessage.getFromUserName());
                accessTokenUtil.staffImage("uhkt0sGYLFtwlFK14FNSSnqLZY9GPhXUALTCD_WitXU",inTestMessage.getFromUserName(),"image");


                //回复图文
                outTestMessage.setMsgType("news");
                //图文个数
                outTestMessage.setArticleCount(1);
                //设置图文明细列表
                ArticleItem item = new ArticleItem();
                item.setTitle("1分钟完善您的名片");
                item.setPicUrl("http://xiaocisw.site:8044/userQR/20200306/1583451617050_612.png");
                item.setDescription("使用文章/视频采集工具，一键即可生成你的专属文章/视频");
                item.setUrl("http://web.xiaocisw.site/personalInfo");
                outTestMessage.setItem(new ArticleItem[]{item});
//                return list;
            }
//            else if (inTestMessage.getEvent().equals("CLICK")){
//                System.out.println("点击");
//                String eventKey = inTestMessage.getEventKey();
//                if ("classinfo".equals(eventKey)){
//                    outTestMessage.setContent("还在开发中请稍后");
//                }else if("address".equals(eventKey)){
//                    outTestMessage.setContent("还在开发中");
//                }
//                outTestMessage.setMsgType("text");
//            }
        }

        return outTestMessage;
    }
}
