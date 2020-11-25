package com.example.xcschoolserver.task;

import com.example.xcschoolserver.mapper.*;
import com.example.xcschoolserver.pojo.*;
import com.example.xcschoolserver.service.ISearchService;
import com.example.xcschoolserver.util.AccessTokenUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ArticleTask {
    @Autowired
    private pushMapper pushMapper;

    @Autowired
    private xcuserMapper xcuserMapper;

    @Autowired
    private ISearchService searchService;

    @Autowired
    private AccessTokenUtil accessTokenUtil;

    @Autowired
    private brandNavMapper brandNavMapper;

    @Autowired
    private shareUserMapper shareUserMapper;

    @Autowired
    private vipOredermapper vipOredermapper;

    @Autowired
    private shareCollectMapper shareCollectMapper;

    @Autowired
    private shareVideoMapper shareVideoMapper;

    @Autowired
    private shareArticleMapper shareArticleMapper;



    /**
     * 早安通知
     */
//    @Scheduled(cron = "0/10 * * * * *")//20,秒一次
    @Scheduled(cron = "0 30 07 * * ?")//每天8:0:0的时候执行一次
    @Transactional(rollbackFor = Exception.class)
    public void signMsg() throws JSONException {
        List<Xcuser> xcusers = xcuserMapper.selectxcuser();

        Integer errcode45015 = 0;
        Integer errcodes = 0;
        Integer ok = 0;

        for (Xcuser xcuser : xcusers) {
            String str = "☀"+xcuser.getUser_name()+"早上好！\n" +
                    "\n" +
                    "新的一天开始啦,\uD83D\uDCAA\uD83C\uDFFB加油!!!\n" +
                    "\n" +
                    "您订阅的早安播报已准备好了\n" +
                    "\n" +
                    "\uD83D\uDC49<a href='http://web.xiaocisw.site/?list=%27%E6%8E%A8%E8%8D%90%27&num=0'>今日热文</a>\n" +
                    "\n" +
                    "\uD83D\uDC49<a href='http://web.xiaocisw.site/customer'>点击查看昨日意向客户</a>\n" +
                    "\n" +
                    "<a href='http://web.xiaocisw.site/punchCard'>设置的打卡时间已到，快去领取今天的每日一句吧！~</a>"+
                    "\n"+
                    searchService.pushParticularly(xcuser.getOpen_id())+"\n"+
                    "\n"+
                    "‼<a href='http://web.xiaocisw.site/customer'>今日意向客户等候多时，赶快领走吧!!!</a>\n"+
                    "\n"+
                    "\uD83D\uDCD6<a href='http://web.xiaocisw.site/?list=%27%E6%8E%A8%E8%8D%90%27&num=0'>换一批今日热门文章</a>\n"+
                    "\n"+
                    "\uD83D\uDCFA<a href='http://web.xiaocisw.site/?list=%27%E6%8E%A8%E8%8D%90%27&num=1'>领取营销视频</a>\n"+
                    "\n"+
                    "别忘了订阅明天推送哦!!!";


            String result = accessTokenUtil.staffTxt(str,xcuser.getOpen_id());

            JSONObject jsonResult = new JSONObject(result);
            String errcode = jsonResult.getString("errcode");

            if (errcode.equals("45015")){
                errcode45015 = errcode45015 + 1;
            }else if (!errcode.equals("45015") && !errcode.equals("0")){
                errcodes = errcodes + 1;
            }else if (errcode.equals("0")){
                ok = ok + 1;
            }
        }
        System.out.println("*****************************************************************");
        System.out.println("打卡通知已经发送");
        System.out.println("超过回复时限:"+errcode45015+"人;");
        System.out.println("其他失败原因:"+errcodes+"人;");
        System.out.println("发送成功:"+ok+"人;");
        System.out.println("*****************************************************************");
    }

    /**
     * 中午12点定时推送任务
     */
//    @Scheduled(cron = "0/10 * * * * *")//20,秒一次
    @Scheduled(cron = "0 0 12 * * ?")//每天9:0:0的时候执行一次
    @Transactional(rollbackFor = Exception.class)
    public void pushNooning() throws JSONException {
        //查漏补缺
        List<Xcuser> xcusers = xcuserMapper.selectxcuser();
        for (Xcuser xcuser : xcusers) {
            //根据现有openId查询推送表
            Push pu = pushMapper.selectPush(xcuser.getOpen_id());
            if (pu == null) {
                pushMapper.insertPush(1, 1, 1, 1, 1, 1, xcuser.getOpen_id());
            }
        }

        Integer errcode45015 = 0;
        Integer errcodes = 0;
        Integer ok = 0;

        //发送推送消息
        List<Push> pushes = pushMapper.selectPushAll();
        for (Push push : pushes) {
            if(push.getPush_nooning() == 1){
                Xcuser user = xcuserMapper.selectOpenId(push.getOpen_id());
                if (user == null){
                    continue;
                }
                String username = user.getUser_name();
                String str = "\uD83D\uDD5B亲爱的"+username+"中午好，为你\uD83C\uDF71奉上今日最热的营销内容，中午时段请您重视哦\n" +
                        "\n" +
                        "\uD83C\uDF81<a href='http://web.xiaocisw.site/seniorMember'>11亿微信用户，离不开微信文章和短视频，一键植入广告，浏览量惊人；\uD83C\uDF89</a>\n"+
                        "\n"+
                        searchService.pushParticularly(push.getOpen_id())+"\n"+
                        "\n"+
                        "\uD83D\uDCD6<a href='http://web.xiaocisw.site/?list=%27%E6%8E%A8%E8%8D%90%27&num=0'>换一批今日热门文章</a>\n"+
                        "\n"+
                        "\uD83D\uDCFA<a href='http://web.xiaocisw.site/?list=%27%E6%8E%A8%E8%8D%90%27&num=1'>领取营销视频</a>";
                String result = accessTokenUtil.staffTxt(str,push.getOpen_id());
                JSONObject jsonResult = new JSONObject(result);
                String errcode = jsonResult.getString("errcode");

                if (errcode.equals("45015")){
                    errcode45015 = errcode45015 + 1;
                }else if (!errcode.equals("45015") && !errcode.equals("0")){
                    errcodes = errcodes + 1;
                }else if (errcode.equals("0")){
                    ok = ok + 1;
                }
            }
        }
        System.out.println("*****************************************************************");
        System.out.println("推送消息已经发送");
        System.out.println("超过回复时限:"+errcode45015+"人;");
        System.out.println("其他失败原因:"+errcodes+"人;");
        System.out.println("发送成功:"+ok+"人;");
        System.out.println("*****************************************************************");
    }

    /**
     * 19.30执行数据汇总
     * @throws JSONException
     * @throws ParseException
     */
//    @Scheduled(cron = "0/10 * * * * *")//20,秒一次
    @Scheduled(cron = "0 30 19 * * ?")//每天19:0:0的时候执行一次
    @Transactional(rollbackFor = Exception.class)
    public void dataCollect() throws JSONException {
        //查询用户列表
        List<Xcuser> xcusers = xcuserMapper.selectxcuser();
        Integer errcode40003 = 0;
        Integer errcodes = 0;
        Integer ok = 0;



        for (Xcuser xcuser : xcusers) {
            Integer material = 0;
            Integer share = 0;


            Integer todyMaterial=  0;
            Integer todyshare = 0;
            //计算素材分享量
            List<ShareArticle> shareArticles =  shareArticleMapper.selectShareArtOpenId(xcuser.getOpen_id());
            List<ShareVideo> shareVideos = shareVideoMapper.selectShareVideoOpenId(xcuser.getOpen_id());
            //总分享
            if (shareArticles.size() != 0 || shareVideos.size() != 0){
                share = shareArticles.size()+shareVideos.size();
            }

            //计算素材分享点击量
            List<ShareUser> shareUser = shareUserMapper.selectShareUserList(xcuser.getOpen_id());
            if(shareUser.size() !=  0){
                //计算曝光量
                for (ShareUser user : shareUser) {
                    material = material + user.getDeg();
                }
            }

            //计算今日分享量和点击量
            ShareCollect shareCollect = shareCollectMapper.selectOpenId(xcuser.getOpen_id());
            if (shareCollect == null){
                shareCollectMapper.insertCollect(xcuser.getOpen_id(),0,0);
                todyMaterial = material;
                todyshare = share;
            }else {
                todyMaterial = material - shareCollect.getMaterial();
                todyshare = share - shareCollect.getShare();
                shareCollectMapper.updataOpenId(xcuser.getOpen_id(),material,share);
            }


            String dataInfo = "";
            //生成随机数
            Random random = new Random();
            int i1 = random.nextInt(10000)%(10000-5000)+5000;
            int i2 = random.nextInt(50000)%(50000-10000)+50000;
            if (todyshare == 0 && todyMaterial == 0){
                dataInfo = "您昨日分享了0篇内容，共获得0次跟进机会，今天共有"+i1+"个小伙伴通过分享内容锁客"+i2+"次。坚持就是胜利，千万别错过精准客户！";
            }else {
                dataInfo = "您昨日分享了"+todyshare.toString()+"篇内容，共获得"+todyMaterial.toString()+"次跟进机会。今天共有"+i1+"个小伙伴通过分享内容锁客"+i2+"次。记得继续分享行业相关内容，树立专业形象，加油!!!";
            }


            String strInfo = "还愁没订单？轻松精准获客                                                 \uD83D\uDC49点击轻松精准获客";




            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String time = df.format(new Date());// new Date()为获取当前系统时间


            Map<String,Object> templateData = new HashMap<>();
            templateData.put("openId",xcuser.getOpen_id());
            templateData.put("userName",xcuser.getUser_name());
            templateData.put("time",time);
            templateData.put("dataInfo",dataInfo);
            templateData.put("strInfo",strInfo);
            String title = "叮  您今日汇总数据已发送，请立即查收!";
            templateData.put("title",title);


            String result = accessTokenUtil.sendTemplate(templateData);
            JSONObject jsonResult = new JSONObject(result);
            String errcode = jsonResult.getString("errcode");

            if (errcode.equals("40003")){
                errcode40003 = errcode40003 + 1;
            }else if (!errcode.equals("45015") && !errcode.equals("0")){
                errcodes = errcodes + 1;
            }else if (errcode.equals("0")){
                ok = ok + 1;
            }
        }

        System.out.println("*****************************************************************");
        System.out.println("客户运营日报");
        System.out.println("openId不正确:"+errcode40003+"人;");
        System.out.println("其他失败原因:"+errcodes+"人;");
        System.out.println("发送成功:"+ok+"人;");
        System.out.println("*****************************************************************");

    }


    /**
     * 清晨定时任务21点执行
     */
//    @Scheduled(cron = "0/10 * * * * *")//20,秒一次
    @Scheduled(cron = "0 0 21 * * ?")//每天9:0:0的时候执行一次
    @Transactional(rollbackFor = Exception.class)
    public void pushNight() throws JSONException {
        //查漏补缺
        List<Xcuser> xcusers = xcuserMapper.selectxcuser();
        for (Xcuser xcuser : xcusers) {
            //根据现有openId查询推送表
            Push pu = pushMapper.selectPush(xcuser.getOpen_id());
            if (pu == null) {
                pushMapper.insertPush(1, 1, 1, 1, 1, 1, xcuser.getOpen_id());
            }
        }

        Integer errcode45015 = 0;
        Integer errcodes = 0;
        Integer ok = 0;

        //发送推送消息
        List<Push> pushes = pushMapper.selectPushAll();
        for (Push push : pushes) {
            if(push.getPush_night() == 1){
                Xcuser user = xcuserMapper.selectOpenId(push.getOpen_id());
                if (user == null){
                    continue;
                }
                String username = user.getUser_name();
                String str = "\uD83D\uDD58亲爱的"+username+"辛苦一天啦\n" +
                        "记得早点休息哦✨\n" +
                        "\n"+
                        "\uD83C\uDF1B<a href='http://web.xiaocisw.site/punchCard'>今日晚安语录，点击我分享</a>\n"+
                        "\n"+
                        "\uD83C\uDF81<a href='http://web.xiaocisw.site/seniorMember'>营销利器，自己开通了受益了，然后顺带帮助更多人，快速成功和拥有财富</a>\uD83C\uDF89\n"+
                        "\n"+
                        searchService.pushParticularly(push.getOpen_id())+"\n"+
                        "\n"+
                        "‼<a href='http://web.xiaocisw.site/customer'>今日意向客户等候多时，赶快领走吧!!!</a>\n"+
                        "\n"+
                        "\uD83D\uDCD6<a href='http://web.xiaocisw.site/?list=%27%E6%8E%A8%E8%8D%90%27&num=0'>换一批今日热门文章</a>\n"+
                        "\n"+
                        "\uD83D\uDCFA<a href='http://web.xiaocisw.site/?list=%27%E6%8E%A8%E8%8D%90%27&num=1'>领取营销视频</a>\n"+
                        "\n"+
                        "别忘了订阅明天推送哦!!!";
                String result = accessTokenUtil.staffTxt(str,push.getOpen_id());
                JSONObject jsonResult = new JSONObject(result);
                String errcode = jsonResult.getString("errcode");

                if (errcode.equals("45015")){
                    errcode45015 = errcode45015 + 1;
                }else if (!errcode.equals("45015") && !errcode.equals("0")){
                    errcodes = errcodes + 1;
                }else if (errcode.equals("0")){
                    ok = ok + 1;
                }
            }
        }
        System.out.println("*****************************************************************");
        System.out.println("推送消息已经发送");
        System.out.println("超过回复时限:"+errcode45015+"人;");
        System.out.println("其他失败原因:"+errcodes+"人;");
        System.out.println("发送成功:"+ok+"人;");
        System.out.println("*****************************************************************");
    }


    //    @Scheduled(cron = "0/10 * * * * *")//20,秒一次
    @Scheduled(cron = "0 0 22 * * ?")//每天8:0:0的时候执行一次
    @Transactional(rollbackFor = Exception.class)
    public void interactMsg() throws JSONException, ParseException {
//        System.out.println("执行");
        List<Xcuser> xcusers = xcuserMapper.selectxcuser();

        Integer errcode45015 = 0;
        Integer errcodes = 0;
        Integer ok = 0;

        for (Xcuser xcuser : xcusers) {
            String str = "‼‼不能错过的消息\n" +
                    "\n❣亲爱的"+xcuser.getUser_name()+"，您已经，一天多没有搭理小编了。\n" +
                    "\n\uD83D\uDCB9由于微信平台规则，我们无法与您发送消息通知啦！\uD83C\uDF8A\n" +
                    "\n\uD83D\uDC8C回复【营销】立即解除限制";
            String result = accessTokenUtil.staffTxt(str,xcuser.getOpen_id());
            JSONObject jsonResult = new JSONObject(result);
            String errcode = jsonResult.getString("errcode");

            if (errcode.equals("45015")){
                errcode45015 = errcode45015 + 1;
            }else if (!errcode.equals("45015") && !errcode.equals("0")){
                errcodes = errcodes + 1;
            }else if (errcode.equals("0")){
                ok = ok + 1;
            }


            //提取会员时间检测会员是否到期
            if (xcuser.getEnd_time() != null && !xcuser.getEnd_time().equals("0")){//判断会员到期时间是否为null
                //获取当前时间戳
                Date date = new Date();
                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
                String nowadays = dateFormat.format(date);
                Date now = dateFormat.parse(nowadays);
                long egm = now.getTime();

                //会员到期时间戳
                String[] times = xcuser.getEnd_time().split("\\s+");
                String time = times[0];
                Date end_time = dateFormat.parse(time);
                long end = end_time.getTime();

                if (end < egm){//如果结束时间大于当前时间,结束会员
                    //到数据库更改用户信息
                    xcuserMapper.updataUserVip(null,null,null,xcuser.getOpen_id(),0,0);
                    String string = "‼‼不能错过的消息\n" +
                            "\n❣亲爱的"+xcuser.getUser_name()+"，你的会员已经过期了。\n";
                    accessTokenUtil.staffTxt(string,xcuser.getOpen_id());
                }
            }
        }
        System.out.println("*****************************************************************");
        System.out.println("晚安问候已经发送");
        System.out.println("超过回复时限:"+errcode45015+"人;");
        System.out.println("其他失败原因:"+errcodes+"人;");
        System.out.println("发送成功:"+ok+"人;");
        System.out.println("*****************************************************************");
    }

















}
