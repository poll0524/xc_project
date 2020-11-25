package com.example.xcschoolserver.service.impl;

import com.example.xcschoolserver.mapper.*;
import com.example.xcschoolserver.pojo.*;
import com.example.xcschoolserver.service.IShareService;
import com.example.xcschoolserver.util.AccessTokenUtil;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ShareServiceIpml implements IShareService {
    @Autowired
    private shareArticleMapper shareArticleMapper;

    @Autowired
    private shareUserMapper shareUserMapper;

    @Autowired
    private xcuserMapper xcuserMapper;

    @Autowired
    private articleMapper articleMapper;

    @Autowired
    private shareVideoMapper shareVideoMapper;

    @Autowired
    private AccessTokenUtil accessTokenUtil;

    @Autowired
    private groupTopMapper groupTopMapper;

    @Autowired
    private shareCourseMapper shareCourseMapper;

    @Autowired
    private courseMapper courseMapper;

    @Autowired
    private courseBuyMapper courseBuyMapper;

    @Autowired
    private courseHourMapper courseHourMapper;

    @Autowired
    private courseCollectMapper courseCollectMapper;

    @Autowired
    private brandCourseHourMapper brandCourseHourMapper;

    @Autowired
    private brandCourseMapper brandCourseMapper;

    /**
     * 阅读人信息
     * @param share
     * @return
     */
    public String insertShareUser(Map<String,Object> share) throws JSONException {
        String openId = (String) share.get("openId");
        String readOpenId = (String) share.get("readOpenId");
        Integer sign = (Integer) share.get("sign");
        if(xcuserMapper.selectOpenId(readOpenId) == null){
            return "没有阅读人该用户信息";
        }
        //判断是否自己阅读
        if(openId == readOpenId){
            return null;
        }


        Long TimeBegin = (Long) share.get("readTimeBegin");
        int start = (int) share.get("start");

        String readTimeBegin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(TimeBegin);

        if (start == 0){
            Integer articleId = (Integer) share.get("id");
            ShareUser shareUser = shareUserMapper.selectShareUserMeds(openId,readOpenId,articleId,sign);
            if (shareUser != null){
                String shareTime = new SimpleDateFormat("yyyy-MM-dd").format(TimeBegin);
                String[] a = shareUser.getRead_time_begin().split("\\s+");
                if (shareTime.equals(a[0])){
                    shareUserMapper.updataShareId(shareUser.getShare_id(),shareUser.getDeg()+1);
                }
                return "添加成功";
            }
            shareUserMapper.insertShareUser(openId,readOpenId,null,articleId,null,readTimeBegin,0,1,sign);
            //发送客服消息
            Xcuser user = xcuserMapper.selectOpenId(openId);
            String userName = user.getUser_name();
            Integer vip = user.getStart();
            Xcuser readUser = xcuserMapper.selectOpenId(readOpenId);
            String readName = readUser.getUser_name();
            Integer artId = shareUserMapper.selectShareUserMeds(openId,readOpenId,articleId,sign).getArticle_id();
            ShareArticle article = new ShareArticle();
            if (sign == 0){
                article = shareArticleMapper.selectShareArtList(openId,artId);
            }else if (sign == 1){
                article = shareArticleMapper.selectShareBraList(openId,artId);
            }


            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String time = df.format(new Date());
            String str = "";
            if (vip == 0){
                str = "来客了请接单\n" +
                        time+"\n" +
                        "敬爱的"+userName+"，刚有客户看了您的作品\n" +
                        "来访客户：<a href='http://web.xiaocisw.site/customer'>***</a>\n" +
                        "访问项目："+article.getAuthor()+"\n" +
                        "访问资源："+article.getTitle()+"\n" +
                        "阅读时长：\n" +
                        "备注：呀，还不招待客户吗？<a href='http://web.xiaocisw.site/customer'>点我</a>就可以查看谁查看过我，快把客户领进店吧";
            }else {
                str = "来客了请接单\n" +
                        time+"\n" +
                        "敬爱的"+userName+"，刚有客户看了您的作品\n" +
                        "来访客户："+readName+"\n" +
                        "访问项目："+article.getAuthor()+"\n" +
                        "访问资源："+article.getTitle()+"\n" +
                        "阅读时长：\n" +
                        "备注：呀，还不招待客户吗？<a href='http://web.xiaocisw.site/customer'>点我</a>就可以查看谁查看过我，快把客户领进店吧";
            }
            accessTokenUtil.staffTxt(str,article.getOpen_id());

            return "添加成功";
        }else if (start == 1){
            int videoId = (int) share.get("videoId");
            ShareUser shareUser = shareUserMapper.selectShareUserVid(openId,readOpenId,videoId,sign);

            if (shareUser != null){
                String shareTime = new SimpleDateFormat("yyyy-MM-dd").format(TimeBegin);
                String[] a = shareUser.getRead_time_begin().split("\\s+");
                if (shareTime.equals(a[0])){
                    shareUserMapper.updataShareId(shareUser.getShare_id(),shareUser.getDeg()+1);
                }
                return "添加成功";
            }

            shareUserMapper.insertShareUser(openId,readOpenId,videoId,null,null,readTimeBegin,1,1,sign);

            //发送客服消息
            Xcuser user = xcuserMapper.selectOpenId(openId);
            String userName = user.getUser_name();
            Integer vip = user.getStart();
            Xcuser readUser = xcuserMapper.selectOpenId(readOpenId);
            String readName = readUser.getUser_name();


            Integer artId = shareUserMapper.selectShareUserVid(openId,readOpenId,videoId,sign).getVideo_id();
            ShareVideo article = new ShareVideo();
            if (sign == 0){
                article = shareVideoMapper.selectShareVid(openId,artId);
            }else if (sign == 1){
                article = shareVideoMapper.selectShareBid(openId,artId);
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String time = df.format(new Date());
            String str = "";
            if (vip == 0){
                str = "来客了请接单\n" +
                        time+"\n" +
                        "敬爱的"+userName+"，刚有客户看了您的作品\n" +
                        "来访客户：<a href='http://web.xiaocisw.site/customer'>***</a>\n" +
                        "访问项目："+article.getAuthor()+"\n" +
                        "访问资源："+article.getVideo_name()+"\n" +
                        "阅读时长：\n" +
                        "备注：呀，还不招待客户吗？<a href='http://web.xiaocisw.site/customer'>点我</a>就可以查看谁查看过我，快把客户领进店吧";
            }else {
                str = "来客了请接单\n" +
                        time+"\n" +
                        "敬爱的"+userName+"，刚有客户看了您的作品\n" +
                        "来访客户："+readName+"\n" +
                        "访问项目："+article.getAuthor()+"\n" +
                        "访问资源："+article.getVideo_name()+"\n" +
                        "阅读时长：\n" +
                        "备注：呀，还不招待客户吗？<a href='http://web.xiaocisw.site/customer'>点我</a>就可以查看谁查看过我，快把客户领进店吧";
            }
            accessTokenUtil.staffTxt(str,article.getOpen_id());

            return "添加成功";
        }
        return null;
    }




    /**
     * 阅读人种类和次数
     */
    public Map<String,Object> shareUserList(Map<String,Object> datac){
        String openId = (String) datac.get("openId");
        Integer start = (Integer) datac.get("start");

        if (start == 0){
            //看我总数
            int sum = 0;
            //今天看了
            int today = 0;
            //向数据库查询该用户所有数据
            List<ShareUser> shareUsers = shareUserMapper.selectShareUserList(openId);
            //如果查询为空返回null
            if (shareUsers.size() == 0){
                return null;
            }
            //获取当前日期用作比对
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String str = sdf.format(d);
            //创建空数组去重存放阅读人openId
            List<String> readUser = new ArrayList<>();
            for (ShareUser shareUser : shareUsers) {
                readUser.add(shareUser.getRead_open_id());
                sum += shareUser.getDeg();
                String time = shareUser.getRead_time_begin().split("\\s+")[0];
                if(str.equals(time)){
                    today += shareUser.getDeg();
                }
            }
            //readUser去重
            TreeSet set = new TreeSet(readUser);
            readUser.clear();
            readUser.addAll(set);

            //创建map用于返回数据
            Map<String,Object> datainfo = new HashMap<>();
            List datas = new ArrayList();
            List datasDay = new ArrayList();


            //向数据库提取各个阅读人信息
            for (String s : readUser) {

                List<ShareUser> users = shareUserMapper.selectReadUser(openId,s,0);

                if (users.size() == 0){
                    continue;
                }

                Map<String,Object> data = new HashMap<>();
                Map<String,Object> dataDay = new HashMap<>();
                int articleTiem = 0;
                int article = 0;
                int articleTiemDay = 0;
                int articleDay = 0;

                for (ShareUser user : users){
                    if (user.getRead_time_begin().split("\\s+")[0].equals(str)) {
                        articleTiemDay += user.getDeg();
                        articleDay += 1;
                    }else{
                        articleTiem += user.getDeg();
                        article += 1;
                    }
                }

                if (articleTiem != 0 && articleTiemDay != 0){
                    dataDay.put("userInfo",xcuserMapper.selectOpenId(s));
                    dataDay.put("time",articleTiemDay);
                    dataDay.put("quantity",articleDay);
                    datasDay.add(dataDay);
                    data.put("userInfo",xcuserMapper.selectOpenId(s));
                    data.put("time",articleTiem);
                    data.put("quantity",article);
                    datas.add(data);
                }else if (articleTiem == 0){
                    data.put("userInfo",xcuserMapper.selectOpenId(s));
                    data.put("time",articleTiemDay);
                    data.put("quantity",articleDay);
                    datasDay.add(data);
                }else if (articleTiemDay == 0){
                    data.put("userInfo",xcuserMapper.selectOpenId(s));
                    data.put("time",articleTiem);
                    data.put("quantity",article);
                    datas.add(data);
                }
            }
            datainfo.put("sum",sum);
            datainfo.put("today",today);
            datainfo.put("datas",datas);
            datainfo.put("datasDay",datasDay);
            return datainfo;
        }else if (start == 1){
            //看我总数
            int sum = 0;
            //今天看了
            int today = 0;
            //向数据库查询该用户所有数据
            List<ShareUser> shareUsers = shareUserMapper.selectShareUserList(openId);
            //如果查询为空返回null
            if (shareUsers.size() == 0){
                return null;
            }
            //获取当前日期用作比对
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String str = sdf.format(d);
            //创建空数组去重存放阅读人openId
            List<String> readUser = new ArrayList<>();
            for (ShareUser shareUser : shareUsers) {
                readUser.add(shareUser.getRead_open_id());
                sum += shareUser.getDeg();
                String time = shareUser.getRead_time_begin().split("\\s+")[0];
                if(str.equals(time)){
                    today += shareUser.getDeg();
                }
            }
            //readUser去重
            TreeSet set = new TreeSet(readUser);
            readUser.clear();
            readUser.addAll(set);

            //创建map用于返回数据
            Map<String,Object> datainfo = new HashMap<>();
            List datas = new ArrayList();
            List datasDay = new ArrayList();


            //向数据库提取各个阅读人信息
            for (String s : readUser) {


                List<ShareUser> users = shareUserMapper.selectReadUser(openId,s,1);

                if (users.size() == 0){
                    continue;
                }


                Map<String,Object> data = new HashMap<>();
                Map<String,Object> dataDay = new HashMap<>();

                int videoTiem = 0;
                int video = 0;
                int videoTiemDay = 0;
                int videoDay = 0;

                for (ShareUser user : users){
                    if (user.getRead_time_begin().split("\\s+")[0].equals(str)){
                        videoTiemDay += user.getDeg();
                        videoDay += 1;
                    }else{
                        videoTiem += user.getDeg();
                        video += 1;
                    }
                }

                if (videoTiem != 0 && videoTiemDay != 0){
                    dataDay.put("userInfo",xcuserMapper.selectOpenId(s));
                    dataDay.put("time",videoTiemDay);
                    dataDay.put("quantity",videoDay);
                    datasDay.add(dataDay);
                    data.put("userInfo",xcuserMapper.selectOpenId(s));
                    data.put("time",videoTiem);
                    data.put("quantity",video);
                    datas.add(data);
                }else if (videoTiem == 0){
                    dataDay.put("userInfo",xcuserMapper.selectOpenId(s));
                    dataDay.put("time",videoTiemDay);
                    dataDay.put("quantity",videoDay);
                    datasDay.add(dataDay);
                }else if (videoTiemDay == 0){
                    data.put("userInfo",xcuserMapper.selectOpenId(s));
                    data.put("time",videoTiem);
                    data.put("quantity",video);
                    datas.add(data);
                }



            }
            datainfo.put("sum",sum);
            datainfo.put("today",today);
            datainfo.put("datas",datas);
            datainfo.put("datasDay",datasDay);
            return datainfo;
        }else if (start == 2){
            //看我总数
            int sum = 0;
            //今天看了
            int today = 0;
            //向数据库查询该用户所有数据
            List<ShareUser> shareUsers = shareUserMapper.selectShareUserList(openId);
            //如果查询为空返回null
            if (shareUsers.size() == 0){
                return null;
            }
            //获取当前日期用作比对
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String str = sdf.format(d);
            //创建空数组去重存放阅读人openId
            List<String> readUser = new ArrayList<>();
            for (ShareUser shareUser : shareUsers) {
                readUser.add(shareUser.getRead_open_id());
                sum += shareUser.getDeg();
                String time = shareUser.getRead_time_begin().split("\\s+")[0];
                if(str.equals(time)){
                    today += shareUser.getDeg();
                }
            }
            //readUser去重
            TreeSet set = new TreeSet(readUser);
            readUser.clear();
            readUser.addAll(set);

            //创建map用于返回数据
            Map<String,Object> datainfo = new HashMap<>();
            List datas = new ArrayList();
            List datasDay = new ArrayList();


            //向数据库提取各个阅读人信息
            for (String s : readUser) {


                List<ShareUser> users = shareUserMapper.selectReadUser(openId,s,2);

                if (users.size() == 0){
                    continue;
                }


                Map<String,Object> data = new HashMap<>();
                Map<String,Object> dataDay = new HashMap<>();

                int courseTiem = 0;
                int course = 0;
                int courseTiemDay = 0;
                int courseDay = 0;

                for (ShareUser user : users){
                    if (user.getRead_time_begin().split("\\s+")[0].equals(str)){
                        courseTiemDay += user.getDeg();
                        courseDay += 1;
                    }else{
                        courseTiem += user.getDeg();
                        course += 1;
                    }
                }

                if (courseTiem != 0 && courseTiemDay != 0){
                    dataDay.put("userInfo",xcuserMapper.selectOpenId(s));
                    dataDay.put("time",courseTiemDay);
                    dataDay.put("quantity",courseDay);
                    datasDay.add(dataDay);
                    data.put("userInfo",xcuserMapper.selectOpenId(s));
                    data.put("time",courseTiem);
                    data.put("quantity",course);
                    datas.add(data);
                }else if (courseTiem == 0){
                    dataDay.put("userInfo",xcuserMapper.selectOpenId(s));
                    dataDay.put("time",courseTiemDay);
                    dataDay.put("quantity",courseDay);
                    datasDay.add(dataDay);
                }else if (courseTiemDay == 0){
                    data.put("userInfo",xcuserMapper.selectOpenId(s));
                    data.put("time",courseTiem);
                    data.put("quantity",course);
                    datas.add(data);
                }
            }
            datainfo.put("sum",sum);
            datainfo.put("today",today);
            datainfo.put("datas",datas);
            datainfo.put("datasDay",datasDay);
            return datainfo;
        }

        return null;

    }


    /**
     * 阅读人阅读详情
     */
    public Map<String,Object> readUser(Map<String,Object> users) throws ParseException {
        String openId = (String) users.get("openId");
        String readId = (String) users.get("readId");
        Integer start = (Integer) users.get("start");


        List<ShareUser> video = shareUserMapper.selectReadUser(openId,readId,1);
        List<ShareUser> article = shareUserMapper.selectReadUser(openId,readId,0);

        List<ShareUser> shareUsers = shareUserMapper.selectReadUser(openId,readId,start);
        Integer articles = article.size();
        Integer videos = video.size();
        Map datainfo = new HashMap();
        List<Map<String,Object>> datas = new ArrayList();
        if (start == 0){
            for (ShareUser shareUser : shareUsers) {
                Map<String,Object> data = new HashMap<>();
                if (shareUser.getTell() == 0){
                    ShareArticle shareArticle = shareArticleMapper.selectShareVid(openId,shareUser.getArticle_id());
                    if (shareArticle.getCover_img() == null){
                        data.put("coverImg",null);
                    }else {
                        data.put("coverImg",shareArticle.getCover_img());
                    }
                    data.put("title",shareArticle.getTitle());
                    data.put("quantity",(int)(Math.random()*5000+3000));
                    data.put("sign",0);
                }else if (shareUser.getTell() == 1){
                    ShareArticle shareArticle = shareArticleMapper.selectShareBid(openId,shareUser.getArticle_id());
                    if (shareArticle.getCover_img() == null){
                        data.put("coverImg",null);
                    }else {
                        data.put("coverImg",shareArticle.getCover_img());
                    }
                    data.put("title",shareArticle.getTitle());

                    data.put("quantity",(int)(Math.random()*5000+3000));
                    data.put("sign",1);
                }

                String time = shareUser.getRead_time_begin();
                //注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
                SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //加上时间
                //必须捕获异常
                Date date=sDateFormat.parse(time);
                data.put("time",date);
                data.put("id",shareUser.getArticle_id());
                data.put("deg",shareUser.getDeg());
                datas.add(data);
            }
        }else if (start == 1){
            for (ShareUser shareUser : shareUsers) {
                Map<String,Object> data = new HashMap<>();
                if (shareUser.getTell() == 0){
                    ShareVideo shareArticle = shareVideoMapper.selectShareVid(openId,shareUser.getVideo_id());
                    if (shareArticle.getCover_url() == null){
                        data.put("coverImg",null);
                    }else {
                        data.put("coverImg",shareArticle.getCover_url());
                    }
                    data.put("title",shareArticle.getVideo_name());
                    data.put("quantity",(int)(Math.random()*5000+3000));
                    data.put("sign",0);
                }else if (shareUser.getTell() == 1){
                    ShareVideo shareArticle = shareVideoMapper.selectShareBid(openId,shareUser.getVideo_id());

                    if (shareArticle.getCover_url() == null){
                        data.put("coverImg",null);
                    }else {
                        data.put("coverImg",shareArticle.getCover_url());
                    }
                    data.put("sign",1);
                    data.put("quantity",(int)(Math.random()*5000+3000));
                    data.put("title",shareArticle.getVideo_name());
                }
                String time = shareUser.getRead_time_begin();
                //注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
                SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //加上时间
                //必须捕获异常
                Date date=sDateFormat.parse(time);
                data.put("time",date);
                data.put("id",shareUser.getVideo_id());
                data.put("deg",shareUser.getDeg());
                datas.add(data);
            }
        }else if (start == 2){//查询课程
            for (ShareUser shareUser : shareUsers) {
                Map<String,Object> data = new HashMap<>();
                if (shareUser.getTell() == 0){//查询平台课程
                    Course shareArticle = courseMapper.selectCourseId(shareUser.getCourse_id());
                    if (shareArticle.getCover_img() == null){
                        data.put("coverImg",null);
                    }else {
                        data.put("coverImg",shareArticle.getCover_img());
                    }
                    data.put("title",shareArticle.getTitle());
                    data.put("price",shareArticle.getPrice());
                    data.put("quantity",(int)(Math.random()*5000+3000));
                    data.put("sign",0);
                }else if (shareUser.getTell() == 1){//查询品牌课程
                    BrandCourse shareArticle = brandCourseMapper.selectCourseId(shareUser.getCourse_id());
                    if (shareArticle.getCover_img() == null){
                        data.put("coverImg",null);
                    }else {
                        data.put("coverImg",shareArticle.getCover_img());
                    }
                    data.put("title",shareArticle.getTitle());
                    data.put("quantity",(int)(Math.random()*5000+3000));
                    data.put("price",shareArticle.getPrice());
                    data.put("sign",1);
                }

                String time = shareUser.getRead_time_begin();
                //注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
                SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //加上时间
                //必须捕获异常
                Date date=sDateFormat.parse(time);
                data.put("time",date);
                data.put("id",shareUser.getCourse_id());
                data.put("deg",shareUser.getDeg());
                datas.add(data);
            }
        }
        Collections.sort(datas, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
//                Integer name1 = Integer.valueOf(o1.get("time").toString());
//                Integer name2 = Integer.valueOf(o2.get("time").toString());
                Date name1 = (Date) o1.get("time");
                Date name2 = (Date) o2.get("time");
                return name2.compareTo(name1);
            }
        });


        datainfo.put("readInfo",xcuserMapper.selectOpenId(readId));
        datainfo.put("dataInfo",datas);
        datainfo.put("article",articles);
        datainfo.put("video",videos);
        return datainfo;

    }


    /**
     * 查询团队列表
     */
    public List<Map<String,Object>> selectGroupList(Map<String,Object> data){
        String openId = (String) data.get("openId");
        //根据openId查询团队列表
        List<GroupTop> groupTops = groupTopMapper.selectGroup(openId);
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
     * 查询分享课程
     */
    public Map<String,Object> selectShareCourse(Map<String,Object> data) throws JSONException {
        String openId = (String) data.get("openId");
        String readOpenid = (String) data.get("readOpenid") ;
        Integer id = (Integer) data.get("id");
        Integer start = (Integer) data.get("start");
        Integer tell = Integer.parseInt((String) data.get("tell"));

        Xcuser xcuser = xcuserMapper.selectOpenId(openId);
        Xcuser readXcuser = xcuserMapper.selectOpenId(readOpenid);
        Integer vip = xcuser.getStart();
        String str = "";
        //获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());

        //创建map用户返回数据
        Map<String,Object> datas = new HashMap<>();

        if (openId != null && readOpenid != null && id != null && start != null){
            //根据openId,id,start到分项表查询
//            ShareCourse shareCourse = shareCourseMapper.selectShareCourse(openId,id,start);
//            if (shareCourse == null){
//                return null;
//            }

            //返回课程详情
            if (start == 0 ){//平台课程
                //阅读量+1
                courseMapper.updataQuantity(id);
                //查询课程及课时
                //课程
                Course course = courseMapper.selectCourseId(id);
                //根据课程查询课时
                List<CourseHour> courseHour = courseHourMapper.selectCourseId(id);

                //查询是否收藏
                CourseCollect courseCollect = courseCollectMapper.selectCollect(openId,id,0,tell);

                //整理返回数据
                //整理课程数据
                Map<String,Object> courseInfo = new HashMap<>();
                courseInfo.put("id",course.getCourse_id());
                courseInfo.put("title",course.getTitle());
                courseInfo.put("author",course.getAuthor());
                courseInfo.put("digest",course.getDigest());
                courseInfo.put("coverImg",course.getCover_img());
                courseInfo.put("quantity",course.getQuantity());
                courseInfo.put("classify",course.getClassify());
                courseInfo.put("publishTime",course.getPublish_time());
                courseInfo.put("tell",course.getTell());
//                courseInfo.put("price",course.getPrice());
                if (courseCollect == null){
                    courseInfo.put("collectId",null);
                }else {
                    courseInfo.put("collectId",courseCollect.getCollect_id());
                }
                if (courseCollect == null){
                    courseInfo.put("collect",false);
                }else {
                    courseInfo.put("collect",true);
                }
                Double a = 0.00;
                List<Integer> b = new ArrayList<>();
                //整理课时数据
                List<Map<String,Object>> courseHourList = new ArrayList<>();
                for (CourseHour hour : courseHour) {
                    Map<String,Object> hourInfo = new HashMap<>();
                    hourInfo.put("hourId",hour.getHour_id());
                    hourInfo.put("title",hour.getTitle());
                    hourInfo.put("downUrl",hour.getDown_url());
                    hourInfo.put("hourSort",hour.getHour_sort());
                    hourInfo.put("hourQuantity",hour.getHour_quantity());
                    //判断是否购买
                    //查询购买表是否有该课时数据
                    CourseBuy courseBuy = courseBuyMapper.selectCourseBuys(readOpenid,course.getCourse_id(),hour.getHour_id(),0,tell);
                    if (courseBuy == null){
                        if (hour.getPrice() == 0.00){
                            hourInfo.put("buy",false);
                            b.add(1);
                            hourInfo.put("price",0.00);
                        }else {
                            hourInfo.put("buy",false);
                            b.add(1);
                            hourInfo.put("price",hour.getPrice());
                        }
                    }else {
                        hourInfo.put("buy",true);
                        b.add(0);
                        hourInfo.put("price",hour.getPrice());
                        a = a + hour.getPrice();
                    }
                    courseHourList.add(hourInfo);
                }

                if (course.getPrice() == 0){
                    courseInfo.put("price",course.getPrice() - a);
                }else if (course.getPrice() - a == 0){
                    courseInfo.put("price",0.00);
                }else if (course.getPrice() - a != 0){
                    courseInfo.put("price",course.getPrice() - a);
                }
                HashSet h = new HashSet(b);
                b.clear();
                b.addAll(h);
                if (b.contains(0) && b.contains(1)){
                    courseInfo.put("course",false);
                }else if (b.contains(0)){
                    courseInfo.put("course",true);
                }else if (b.contains(1)){
                    courseInfo.put("course",false);
                }

                courseInfo.put("courseHour",courseHourList);
                datas.put("dataInfo",courseInfo);
                datas.put("userInfo",xcuser);
                datas.put("start",0);

                if (!openId.equals(readOpenid)){
                    if (vip == 0){
                        str = "来客了请接单\n" +
                                time+"\n" +
                                "敬爱的"+xcuser.getUser_name()+"，刚有客户看了您的作品\n" +
                                "来访客户：<a href='http://web.xiaocisw.site/customer'>***</a>\n" +
                                "访问项目："+course.getAuthor()+"\n" +
                                "访问资源："+course.getTitle()+"\n" +
                                "阅读时长：\n" +
                                "备注：呀，还不招待客户吗？<a href='http://web.xiaocisw.site/customer'>点我</a>就可以查看谁查看过我，快把客户领进店吧";
                    }else {
                        str = "来客了请接单\n" +
                                time+"\n" +
                                "敬爱的"+xcuser.getUser_name()+"，刚有客户看了您的作品\n" +
                                "来访客户："+readXcuser.getUser_name()+"\n" +
                                "访问项目："+course.getAuthor()+"\n" +
                                "访问资源："+course.getTitle()+"\n" +
                                "阅读时长：\n" +
                                "备注：呀，还不招待客户吗？<a href='http://web.xiaocisw.site/customer'>点我</a>就可以查看谁查看过我，快把客户领进店吧";
                    }
                    accessTokenUtil.staffTxt(str,openId);
                }


            }else if (start == 1){
                //阅读量+1
                brandCourseMapper.updataQuantity(id);

                //查询品牌课程
                BrandCourse course = brandCourseMapper.selectCourseId(id);

                //根据课程查询课时
                List<BrandCourseHour> courseHour = brandCourseHourMapper.selectHour(id);

                //查询是否收藏
                CourseCollect courseCollect = courseCollectMapper.selectCollect(readOpenid,id,1,tell);

                //整理返回数据
                //整理课程数据
                Map<String,Object> courseInfo = new HashMap<>();
                courseInfo.put("id",course.getCourse_id());
                courseInfo.put("title",course.getTitle());
                courseInfo.put("author",course.getAuthor());
                courseInfo.put("digest",course.getDigest());
                courseInfo.put("coverImg",course.getCover_img());
                courseInfo.put("quantity",course.getQuantity());
                courseInfo.put("classify",course.getClassify());
                courseInfo.put("publishTime",course.getPublish_time());
                courseInfo.put("tell",course.getTell());
//                courseInfo.put("price",course.getPrice());
                if (courseCollect == null){
                    courseInfo.put("collectId",null);
                }else {
                    courseInfo.put("collectId",courseCollect.getCollect_id());
                }
                if (courseCollect == null){
                    courseInfo.put("collect",false);
                }else {
                    courseInfo.put("collect",true);
                }
                Double a = 0.00;
                List<Integer> b = new ArrayList<>();
                //整理课时数据
                List<Map<String,Object>> courseHourList = new ArrayList<>();
                for (BrandCourseHour hour : courseHour) {
                    Map<String,Object> hourInfo = new HashMap<>();
                    hourInfo.put("hourId",hour.getHour_id());
                    hourInfo.put("title",hour.getTitle());
                    hourInfo.put("downUrl",hour.getDown_url());
                    hourInfo.put("hourSort",hour.getHour_sort());
                    hourInfo.put("hourQuantity",hour.getHour_quantity());
                    //判断是否购买
                    //查询购买表是否有该课时数据
                    CourseBuy courseBuy = courseBuyMapper.selectCourseBuys(readOpenid,course.getCourse_id(),hour.getHour_id(),1,tell);
                    if (courseBuy == null){
                        if (hour.getPrice() == 0.00){
                            hourInfo.put("buy",false);
                            b.add(1);
                            hourInfo.put("price",0.00);
                        }else {
                            hourInfo.put("buy",false);
                            b.add(1);
                            hourInfo.put("price",hour.getPrice());
                        }
                    }else {
                        hourInfo.put("buy",true);
                        b.add(0);
                        hourInfo.put("price",hour.getPrice());
                        a = a + hour.getPrice();
                    }
                    courseHourList.add(hourInfo);
                }

                if (course.getPrice() == 0){
                    courseInfo.put("price",course.getPrice() - a);
                }else if (course.getPrice() - a == 0){
                    courseInfo.put("price",0.00);
                }else if (course.getPrice() - a != 0){
                    courseInfo.put("price",course.getPrice() - a);
                }
                HashSet h = new HashSet(b);
                b.clear();
                b.addAll(h);
                if (b.contains(0) && b.contains(1)){
                    courseInfo.put("course",false);
                }else if (b.contains(0)){
                    courseInfo.put("course",true);
                }else if (b.contains(1)){
                    courseInfo.put("course",false);
                }

                courseInfo.put("courseHour",courseHourList);
                datas.put("dataInfo",courseInfo);
                datas.put("userInfo",xcuser);
                datas.put("start",1);
                if (!openId.equals(readOpenid)){
                    if (vip == 0){
                        str = "来客了请接单\n" +
                                time+"\n" +
                                "敬爱的"+xcuser.getUser_name()+"，刚有客户看了您的作品\n" +
                                "来访客户：<a href='http://web.xiaocisw.site/customer'>***</a>\n" +
                                "访问项目："+course.getAuthor()+"\n" +
                                "访问资源："+course.getTitle()+"\n" +
                                "阅读时长：\n" +
                                "备注：呀，还不招待客户吗？<a href='http://web.xiaocisw.site/customer'>点我</a>就可以查看谁查看过我，快把客户领进店吧";
                    }else {
                        str = "来客了请接单\n" +
                                time+"\n" +
                                "敬爱的"+xcuser.getUser_name()+"，刚有客户看了您的作品\n" +
                                "来访客户："+readXcuser.getUser_name()+"\n" +
                                "访问项目："+course.getAuthor()+"\n" +
                                "访问资源："+course.getTitle()+"\n" +
                                "阅读时长：\n" +
                                "备注：呀，还不招待客户吗？<a href='http://web.xiaocisw.site/customer'>点我</a>就可以查看谁查看过我，快把客户领进店吧";
                    }
                    accessTokenUtil.staffTxt(str,openId);
                }

            }

            if (!openId.equals(readOpenid)){
                //
                ShareUser shareUser = shareUserMapper.selectShareUserCid(openId,readOpenid,id,start);
                if (shareUser == null){
                    shareUserMapper.insertShareUser(openId,readOpenid,null,null,id,time,2,1,start);
                }else {
                    shareUserMapper.updataShareId(shareUser.getShare_id(),shareUser.getDeg()+1);
                }

            }

        }
        return datas;

    }


}
