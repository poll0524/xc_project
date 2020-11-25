package com.example.datatoolserver.util;

import com.example.datatoolserver.common.HttpUtil;
import com.example.datatoolserver.common.WeChatUtil;
import com.example.datatoolserver.pojo.AccessToken;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.datatoolserver.mapper.accessTokenMapper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class AccessTokenUtil {
    @Autowired
    private accessTokenMapper accessTokenMapper;



    /**
     * 获取基本的access_token
     */
    public String getAccessToken() throws JSONException {
        //向数据库提取access_token记录
        AccessToken accessTokenSql = accessTokenMapper.selectNameAccessToken(WeChatUtil.ACCESSTOKEN);
        Long expires = null;
        //将时间戳转为long类型
        if(accessTokenSql.getToken_time() != null){
            String expiresTime =  accessTokenSql.getToken_time();
            expires = Long.parseLong(expiresTime);
        }

        Long time = new Date().getTime();
        //当access_token为null或失效才进行重新获取
        if( accessTokenSql.getToken_info() == null || accessTokenSql.getToken_time() == null){
            //发起get请求获取access_token
            System.out.println(WeChatUtil.APPID);
            //本地
            String result = HttpUtil.get(WeChatUtil.GET_ACCESSTOKEN_URL.replace("APPID","wx8ee656699cb2c84d").replace("APPSECRET","c82093ea8fdffd9178cee6152c40cf90"));
            //线上
//            String result = HttpUtil.get(WeChatUtil.GET_ACCESSTOKEN_URL.replace("APPID","wx03cab7644b8bddb8").replace("APPSECRET","803dd8cc93a68f1c0db2e8121534003e"));
            JSONObject jsonObject = new JSONObject(new String(result));

            //得到access_token
            String accessToken = jsonObject.getString("access_token");
            //有效期
            Long expires_in =  jsonObject.getLong("expires_in");
            //设置凭据的失效时间
            Long tokenTimes = new Date().getTime() + ((expires_in-60) * 1000);
            //将失效时间转为string,存入数据库
            String tokenTime = Long.toString(tokenTimes);
            String tokenName = WeChatUtil.ACCESSTOKEN;
            accessTokenMapper.updataNameAccessToken(tokenName,accessToken,tokenTime);

            return accessToken;
        }else if (time > expires){
            System.out.println(WeChatUtil.APPID);
            //发起get请求获取access_token
            //本地
            String result = HttpUtil.get(WeChatUtil.GET_ACCESSTOKEN_URL.replace("APPID","wx8ee656699cb2c84d").replace("APPSECRET","c82093ea8fdffd9178cee6152c40cf90"));
            //线上
//            String result = HttpUtil.get(WeChatUtil.GET_ACCESSTOKEN_URL.replace("APPID","wx03cab7644b8bddb8").replace("APPSECRET","803dd8cc93a68f1c0db2e8121534003e"));

            //string转json对象
            JSONObject jsonObject = new JSONObject(new String(result));

            //得到access_token
            String accessToken = jsonObject.getString("access_token");
            //有效期
            Long expires_in =  jsonObject.getLong("expires_in");
            //设置凭据的失效时间
            Long tokenTimes = new Date().getTime() + ((expires_in-60) * 1000);
            //将失效时间转为string,存入数据库
            String tokenTime = Long.toString(tokenTimes);
            accessTokenMapper.updataNameAccessToken(WeChatUtil.ACCESSTOKEN,accessToken,tokenTime);

            return accessToken;
        }
        return accessTokenSql.getToken_info();
    }

    /**
     * 获取网页授权的access_token
     * @return
     */
    public Map<String,Object> getWebAccessToken(String code) throws JSONException {
        //到数据库获取refresh_token
        AccessToken RefreshToken = accessTokenMapper.selectNameAccessToken(WeChatUtil.REFRESHTOKEN);

        //获取refresh_token时间戳
        Long tokenTimess = null;

        if (RefreshToken.getToken_time() != null){
            String refreshTime = RefreshToken.getToken_time();
            tokenTimess = Long.parseLong(refreshTime);
        }

        //获取当前时间戳
        Long time = new Date().getTime();

        //到数据库查询
        AccessToken webaccesstoken = accessTokenMapper.selectNameAccessToken(WeChatUtil.WEBACCESSTOKEN);

        //获取数据库时间戳
        Long expires = null;
        //将时间戳转为long类型
        if(webaccesstoken.getToken_time() != null){
            String expiresTime =  webaccesstoken.getToken_time();
            expires = Long.parseLong(expiresTime);
        }

        //判断当前时间戳是否大于有效时间戳
        if (RefreshToken.getToken_time() == null || RefreshToken.getToken_info() == null){//当RefreshToken或时间为空时
            return getWebaccessTokens(code);
        }else if (time > tokenTimess){
            return getWebaccessTokens(code);
        }else if (time < tokenTimess){
            //当webaccesstoken失效或者为null是重新获取授权
            if( webaccesstoken.getToken_info() == null || webaccesstoken.getToken_time() == null){

                //发起get请求获取webaccess_token
                //本地
                String result = HttpUtil.get(WeChatUtil.GET_REFRESH_TOKEN.replace("APPID","wx8ee656699cb2c84d").replace("REFRESH_TOKEN",accessTokenMapper.selectNameAccessToken(WeChatUtil.REFRESHTOKEN).getToken_info()));
                //线上
//                String result = HttpUtil.get(WeChatUtil.GET_REFRESH_TOKEN.replace("APPID","wx03cab7644b8bddb8").replace("REFRESH_TOKEN",accessTokenMapper.selectNameAccessToken(WeChatUtil.REFRESHTOKEN).getToken_info()));

                //result转json类型
                JSONObject jsonObject = new JSONObject(new String(result));

                //获取openId
                String openId = jsonObject.getString("openid");

                //获取网页授权access_token
                String access_token = jsonObject.getString("access_token");

                //有效期
                Long expires_in =  jsonObject.getLong("expires_in");
                //设置凭据的失效时间
                Long tokenTimes = new Date().getTime() + ((expires_in-60) * 1000);
                //将失效时间转为string,存入数据库
                String tokenTime = Long.toString(tokenTimes);

                accessTokenMapper.updataNameAccessToken(WeChatUtil.WEBACCESSTOKEN,access_token,tokenTime);


                Map<String,Object> map = new HashMap<>();
                map.put("openId",openId);
                map.put("access_token",accessTokenMapper.selectNameAccessToken(WeChatUtil.WEBACCESSTOKEN).getToken_info());
                return map;
            }else if (time > expires || time <expires){
                //发起get请求获取webaccess_token
                //本地
                String result = HttpUtil.get(WeChatUtil.GET_REFRESH_TOKEN.replace("APPID","wx8ee656699cb2c84d").replace("REFRESH_TOKEN",accessTokenMapper.selectNameAccessToken(WeChatUtil.REFRESHTOKEN).getToken_info()));
                //线上
//                String result = HttpUtil.get(WeChatUtil.GET_REFRESH_TOKEN.replace("APPID","wx03cab7644b8bddb8").replace("REFRESH_TOKEN",accessTokenMapper.selectNameAccessToken(WeChatUtil.REFRESHTOKEN).getToken_info()));

                //result转json类型
                JSONObject jsonObject = new JSONObject(new String(result));

                //获取openId
                String openId = jsonObject.getString("openid");

                //获取网页授权access_token
                String access_token = jsonObject.getString("access_token");

                //有效期
                Long expires_in =  jsonObject.getLong("expires_in");
                //设置凭据的失效时间
                Long tokenTimes = new Date().getTime() + ((expires_in-60) * 1000);
                //将失效时间转为string,存入数据库
                String tokenTime = Long.toString(tokenTimes);

                accessTokenMapper.updataNameAccessToken(WeChatUtil.WEBACCESSTOKEN,access_token,tokenTime);


                Map<String,Object> map = new HashMap<>();
                map.put("openId",openId);
                map.put("access_token",accessTokenMapper.selectNameAccessToken(WeChatUtil.WEBACCESSTOKEN).getToken_info());
                return map;
            }
        }
        return null;
    }

    /**
     * 获取网页授权方法
     */
    public Map<String,Object> getWebaccessTokens(String code) throws JSONException {
        //发起get请求获取webaccess_token
        //本地
        String result = HttpUtil.get(WeChatUtil.GET_WEBACCESSTOKEN_URL.replace("APPID","wx8ee656699cb2c84d").replace("SECRET","c82093ea8fdffd9178cee6152c40cf90").replace("CODE",code));
        //线上
//        String result = HttpUtil.get(WeChatUtil.GET_WEBACCESSTOKEN_URL.replace("APPID","wx03cab7644b8bddb8").replace("SECRET","803dd8cc93a68f1c0db2e8121534003e").replace("CODE",code));

        //result转json类型
        JSONObject jsonObject = new JSONObject(new String(result));

        //获取openId
        String openId = jsonObject.getString("openid");

//            //到数据库查询判断该openId是否存在
//            Xcuser xcuser = xcuserMapper.selectOpenId(openId);
//            if (xcuser != null){
//                Map<String,Object> map = new HashMap<>();
//                map.put("openid",openId);
//                map.put("data","该用户注册成功");
//                return map;
//            }

        //获取网页授权access_token
        String access_token = jsonObject.getString("access_token");

//        //有效期
//        Long expires_in =  jsonObject.getLong("expires_in");
//        //设置凭据的失效时间
//        Long tokenTimes = new Date().getTime() + ((expires_in-60) * 1000);
//        //将失效时间转为string,存入数据库
//        String tokenTime = Long.toString(tokenTimes);
//
//        //获取refresh_token
//        String refresh_token = jsonObject.getString("refresh_token");
//        //设置refresh_token失效时间
//        Long tokenTimeref = new Date().getTime() + 2505600000L;
//        System.out.println(tokenTimeref);
//        //将失效时间转为string,存入数据库
//        String tokenTimerefs = Long.toString(tokenTimeref);
//
//        accessTokenMapper.updataNameAccessToken(WeChatUtil.WEBACCESSTOKEN,access_token,tokenTime);
//
//        accessTokenMapper.updataNameAccessToken(WeChatUtil.REFRESHTOKEN,refresh_token,tokenTimerefs);

        Map<String,Object> map = new HashMap<>();
        map.put("openId",openId);
        map.put("access_token",access_token);
        return map;
    }

    /**
     * 拉取用户信息方法
     * @throws JSONException
     */
    public JSONObject getUserInfo(String accessToken, String openId) throws JSONException {
        String result = HttpUtil.get(WeChatUtil.GET_USERINFO_URL.replace("ACCESS_TOKEN",accessToken).replace("OPENID",openId));
        JSONObject jsonObject = new JSONObject(new String(result));
        return jsonObject;
    }

    /**
     * 获取文章数量
     */
    public JSONObject getArticleCount() throws JSONException {
        //获取图文素材数量
        String articles = HttpUtil.get(WeChatUtil.GET_ARTICLEQTL_URL.replace("ACCESS_TOKEN",getAccessToken()));

        JSONObject article = new JSONObject(new String(articles));
        return article;
    }

    /**
     * 获取文章列表
     * @return
     */
    public JSONObject postArticleList(String type, int offset, int count) throws JSONException {
        //设置获取类型及数量
        Map map = new HashMap();
        map.put("type",type);
        map.put("offset",offset);
        map.put("count",count);
        JSONObject jsonObject = new JSONObject(map);
        String result = HttpUtil.post(WeChatUtil.POST_ARTICLE_URL.replace("ACCESS_TOKEN",getAccessToken()),jsonObject.toString());
        JSONObject jsonResult = new JSONObject(result);
        return jsonResult;
    }

    /**
     * 新增图文方法
     */
    public String postAddArticle(String articles) throws JSONException {
        String result = HttpUtil.post(WeChatUtil.POST_ADDARTICLE_URL.replace("ACCESS_TOKEN",getAccessToken()),articles);
        return result;
    }

    /**
     * 获取素材信息
     */
    public JSONObject postMaterial(String mediaId) throws JSONException {
        Map map = new HashMap();
        map.put("media_id",mediaId);
        JSONObject jsonObject = new JSONObject(map);
        String result = HttpUtil.post(WeChatUtil.POST_MATERIAL_URL.replace("ACCESS_TOKEN",getAccessToken()),jsonObject.toString());
        JSONObject jsonResult = new JSONObject(result);
        return jsonResult;
    }

    /**
     * 创建jssdk
     */
    public String getTicket() throws JSONException {
        //向数据库提取ticket
        AccessToken accessTokenSql = accessTokenMapper.selectNameAccessToken("Ticket");
        Long expires = null;
        //如果时间戳不能null就转为long类型
        if(accessTokenSql.getToken_time() != null){
            String expiresTime =  accessTokenSql.getToken_time();
            expires = Long.parseLong(expiresTime);
        }
        Long time = new Date().getTime();

        //当access_token为null或失效才进行重新获取
        if( accessTokenSql.getToken_info() == null || accessTokenSql.getToken_time() == null){
            String result = HttpUtil.get(WeChatUtil.GET_TICKET_URL.replace("ACCESS_TOKEN",getAccessToken()));
            JSONObject jsonObject = new JSONObject(new String(result));
            String ticket = jsonObject.getString("ticket");
            //有效期
            Long expires_in =  jsonObject.getLong("expires_in");
            //设置凭据的失效时间
            Long tokenTimes = new Date().getTime() + ((expires_in-60) * 1000);
            //将失效时间转为string,存入数据库
            String tokenTime = Long.toString(tokenTimes);
            accessTokenMapper.updataNameAccessToken("Ticket",ticket,tokenTime);
            return ticket;
        }else if (time > expires){
            String result = HttpUtil.get(WeChatUtil.GET_TICKET_URL.replace("ACCESS_TOKEN",getAccessToken()));
            JSONObject jsonObject = new JSONObject(new String(result));
            String ticket = jsonObject.getString("ticket");
            //有效期
            Long expires_in =  jsonObject.getLong("expires_in");
            //设置凭据的失效时间
            Long tokenTimes = new Date().getTime() + ((expires_in-60) * 1000);
            //将失效时间转为string,存入数据库
            String tokenTime = Long.toString(tokenTimes);
            accessTokenMapper.updataNameAccessToken("Ticket",ticket,tokenTime);
            return ticket;

        }
        return accessTokenSql.getToken_info();
    }


    /**
     * sha1加密
     */
    public String SHA1(String decript){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }




    /**
     * 创建菜单方法
     */
    public String createMenu() throws JSONException {
//        String string = "{\n" +
//                "\"button\":[\n" +
//                "    {\n" +
//                "       \"name\":\"云素材\",\n" +
//                "       \"sub_button\":[\n" +
//                "           {\n" +
//                "               \"type\":\"view\",\n" +
//                "               \"name\":\"首页\",\n" +
//                "               \"url\":\"http://web.xiaocisw.site/home?index=0\"\n" +
//                "           },\n" +
//                "           {\n" +
//                "               \"type\":\"view\",\n" +
//                "               \"name\":\"课程\",\n" +
//                "               \"url\":\"http://web.xiaocisw.site/onlineCourse?index=3\"\n" +
//                "           },\n" +
//                "           {\n" +
//                "               \"type\":\"view\",\n" +
//                "               \"name\":\"文章/视频\",\n" +
//                "               \"url\":\"http://web.xiaocisw.site/drainage?list=%27%E6%8E%A8%E8%8D%90%27&num=0&index=1\"\n" +
//                "           },\n" +
//                "           {\n" +
//                "               \"type\":\"view\",\n" +
//                "               \"name\":\"我的素材\",\n" +
//                "               \"url\":\"http://web.xiaocisw.site/dataBase?sortNum=0\"\n" +
//                "           },\n" +
//                "           {\n" +
//                "               \"type\":\"view\",\n" +
//                "               \"name\":\"素材采集工具\",\n" +
//                "               \"url\":\"http://web.xiaocisw.site/useTools?index=2\"\n" +
//                "           }\n" +
//                "       ]\n" +
//                "   },\n" +
//                "   {\n" +
//                "       \"name\":\"云数据\",\n" +
//                "       \"sub_button\":[\n" +
//                "           {\n" +
//                "               \"type\":\"view\",\n" +
//                "               \"name\":\"个人分享\",\n" +
//                "               \"url\":\"http://web.xiaocisw.site/customer\"\n" +
//                "           },\n" +
//                "           {\n" +
//                "               \"type\":\"view\",\n" +
//                "               \"name\":\"团队分享\",\n" +
//                "               \"url\":\"http://web.xiaocisw.site/teamDetails\"\n" +
//                "           },\n" +
//                "           {\n" +
//                "               \"type\":\"view\",\n" +
//                "               \"name\":\"分享有礼\",\n" +
//                "               \"url\":\"http://web.xiaocisw.site/shareCourtesy\"\t\n" +
//                "           }\n" +
//                "       ]\n" +
//                "   },\n" +
//                "   {\n" +
//                "       \"name\":\"个人云\",\n" +
//                "       \"sub_button\":[\n" +
//                "           {\n" +
//                "               \"type\":\"view\",\n" +
//                "               \"name\":\"个人中心\",\n" +
//                "               \"url\":\"http://web.xiaocisw.site/settings?index=3\"\n" +
//                "           },\n" +
//                "           {\n" +
//                "               \"type\":\"view\",\n" +
//                "               \"name\":\"我的团队\",\n" +
//                "               \"url\":\"http://web.xiaocisw.site/teamLists\"\n" +
//                "           },\n" +
//                "           {\n" +
//                "               \"type\":\"view\",\n" +
//                "               \"name\":\"我的品牌\",\n" +
//                "               \"url\":\"http://web.xiaocisw.site/stationMain?mark=0\"\n" +
//                "           },\n" +
//                "           {\n" +
//                "               \"type\":\"view\",\n" +
//                "               \"name\":\"我的会员\",\n" +
//                "               \"url\":\"http://web.xiaocisw.site/seniorMember\"\n" +
//                "           },\n" +
//                "           {\n" +
//                "               \"type\":\"view\",\n" +
//                "               \"name\":\"帮助中心\",\n" +
//                "               \"url\":\"http://web.xiaocisw.site/helpCenter\"\n" +
//                "           }\n" +
//                "       ]\n" +
//                "     }\n" +
//                "   ]\n" +
//                "}";


        String string = "{\n" +
                "\"button\":[\n" +
                "    {\n" +
                "       \"type\":\"view\",\n" +
                "       \"name\":\"首页\",\n" +
                "       \"url\":\"http://web.xiaocisw.site/home?index=0\",\n" +
                "       \"sub_button\":[ ]\n" +
                "   },\n" +
                "   {\n" +
                "       \"name\":\"云数据\",\n" +
                "       \"sub_button\":[\n" +
                "           {\n" +
                "               \"type\":\"view\",\n" +
                "               \"name\":\"个人分享\",\n" +
                "               \"url\":\"http://web.xiaocisw.site/customer\"\n" +
                "           },\n" +
                "           {\n" +
                "               \"type\":\"view\",\n" +
                "               \"name\":\"团队分享\",\n" +
                "               \"url\":\"http://web.xiaocisw.site/teamDetails\"\n" +
                "           },\n" +
                "           {\n" +
                "               \"type\":\"view\",\n" +
                "               \"name\":\"分享有礼\",\n" +
                "               \"url\":\"http://web.xiaocisw.site/shareCourtesy\"\t\n" +
                "           }\n" +
                "       ]\n" +
                "   },\n" +
                "   {\n" +
                "       \"name\":\"个人云\",\n" +
                "       \"sub_button\":[\n" +
                "           {\n" +
                "               \"type\":\"view\",\n" +
                "               \"name\":\"个人中心\",\n" +
                "               \"url\":\"http://web.xiaocisw.site/settings?index=3\"\n" +
                "           },\n" +
                "           {\n" +
                "               \"type\":\"view\",\n" +
                "               \"name\":\"我的团队\",\n" +
                "               \"url\":\"http://web.xiaocisw.site/teamLists\"\n" +
                "           },\n" +
                "           {\n" +
                "               \"type\":\"view\",\n" +
                "               \"name\":\"我的品牌\",\n" +
                "               \"url\":\"http://web.xiaocisw.site/stationMain?mark=0\"\n" +
                "           },\n" +
                "           {\n" +
                "               \"type\":\"view\",\n" +
                "               \"name\":\"我的会员\",\n" +
                "               \"url\":\"http://web.xiaocisw.site/seniorMember\"\n" +
                "           },\n" +
                "           {\n" +
                "               \"type\":\"view\",\n" +
                "               \"name\":\"帮助中心\",\n" +
                "               \"url\":\"http://web.xiaocisw.site/helpCenter\"\n" +
                "           }\n" +
                "       ]\n" +
                "     }\n" +
                "   ]\n" +
                "}";
        //发起请求到指定的接口,并且带上菜单json数据
        String result = HttpUtil.post(WeChatUtil.CREATE_MENU_URL.replace("ACCESS_TOKEN",getAccessToken()),string);
        System.out.println(result);
        return result;
    }

    /**
     * 添加客服
     */
    public String addStaff(Map<String,Object> data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        String result = HttpUtil.post(WeChatUtil.POST_ADDSTAFF_URL.replace("ACCESS_TOKEN",getAccessToken()),jsonObject.toString());
        return result;
    }

    /**
     * 发送客服文本消息
     */
    public String staffTxt(String content,String openId) throws JSONException {

        String a = "{\n" +
                "    \"touser\":\""+openId+"\",\n" +
                "    \"msgtype\":\"text\",\n" +
                "    \"text\":\n" +
                "    {\n" +
                "         \"content\":\""+content+"\"\n" +
                "    }\n" +
                "}";


        String result = HttpUtil.post(WeChatUtil.POST_STAFFTXT_URL.replace("ACCESS_TOKEN",getAccessToken()),a);
        return result;
    }

    /**
     * 发送客服图片消息
     */
    public String staffImage(String MEDIA_ID,String openId,String type) throws JSONException {
        String str = "{\n" +
                "    \"touser\":\""+openId+"\",\n" +
                "    \"msgtype\":\""+type+"\",\n" +
                "    \"image\":\n" +
                "    {\n" +
                "      \"media_id\":\""+MEDIA_ID+"\"\n" +
                "    }\n" +
                "}";
        String result = HttpUtil.post(WeChatUtil.POST_STAFFTXT_URL.replace("ACCESS_TOKEN",getAccessToken()),str);
        return result;
    }

    /**
     * 发送阅读人模板消息
     * @return
     */
    public String sendTemplate(Map<String,Object> data) throws JSONException {

        String str = "{\n" +
                "       \"touser\":\""+data.get("openId")+"\",\n" +
                "       \"template_id\":\"hCZLVWHOIp2v8jx6RTy46OgrCIF-9yky5C82Pf7wjkE\",\n" +
//                "       \"template_id\":\"aJo7TmTlzuC7kwnUQ4TQc6nnKdt9qvJkd-APqJIKlzk\",\n" +
                "       \"url\":\"http://web.xiaocisw.site/customer\",  \n" +
                "       \"data\":{\n" +
                "               \"first\": {\n" +
                "                   \"value\":\""+data.get("title")+"\",\n" +
                "                   \"color\":\"#444444\"\n" +
                "               },\n" +
                "               \"keyword1\":{\n" +
                "                   \"value\":\""+data.get("userName")+"\",\n" +
                "                   \"color\":\"#444444\"\n" +
                "               },\n" +
                "               \"keyword2\": {\n" +
                "                   \"value\":\""+data.get("time")+"\",\n" +
                "                   \"color\":\"#444444\"\n" +
                "               },\n" +
                "               \"keyword3\": {\n" +
                "                   \"value\":\""+data.get("dataInfo")+"\",\n" +
                "                   \"color\":\"#FF0000\"\n" +
                "               },\n" +
                "               \"remark\":{\n" +
                "                   \"value\":\""+data.get("strInfo")+"\",\n" +
                "                   \"color\":\"#0000FF\"\n" +
                "               }\n" +
                "       }\n" +
                "}";
        String result = HttpUtil.post(WeChatUtil.SEND_TEMPLATE.replace("ACCESS_TOKEN",getAccessToken()),str);
        return result;
    }

    /**
     * @throws JSONException
     */
    public void sendSign(String openId,String userName) throws JSONException {
        String string = "{\n" +
                "           \"touser\":\""+openId+"\",\n" +
                "           \"template_id\":\"1SPzkdkPyHuHLFcEtP-2gGHV2OX7xNknQsz_cjsVyWA\",\n" +
                "           \"url\":\"http://joycai.mynatapp.cc/punchCard\",  \n" +
                "          \n" +
                "           \"data\":{\n" +
                "                   \"userName\": {\n" +
                "                       \"value\":\""+userName+"\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"textInfo\":{\n" +
                "                       \"value\":\"点击快速打卡\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   }\n" +
                "           }\n" +
                "       }";
        String result = HttpUtil.post(WeChatUtil.SEND_TEMPLATE.replace("ACCESS_TOKEN",getAccessToken()),string);
        System.out.println(result);
    }

    /**
     * 发送提示通知
     */
    public void sendHint(String openId,String userName,String url) throws JSONException {
        String string = "{\n" +
                "           \"touser\":\""+openId+"\",\n" +
                "           \"template_id\":\"1uBKp9WJtnpLlRV84jr4wRNYaU2XBZV3_J40efFk3r0\",\n" +
                "           \"url\":\""+url+"\",  \n" +
                "          \n" +
                "           \"data\":{\n" +
                "                   \"userName\": {\n" +
                "                       \"value\":\""+userName+"\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"textInfo\":{\n" +
                "                       \"value\":\"回复【营销】立即解除限制\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   }\n" +
                "           }\n" +
                "       }";
        String result = HttpUtil.post(WeChatUtil.SEND_TEMPLATE.replace("ACCESS_TOKEN",getAccessToken()),string);
        System.out.println(result);
    }


}
