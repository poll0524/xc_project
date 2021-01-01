package com.example.datatoolserver.controller;

import com.example.datatoolserver.common.ReturnVO;
import com.example.datatoolserver.common.WX_Util;
import com.example.datatoolserver.common.WeChatUtil;
import com.example.datatoolserver.mapper.groupTopMapper;
import com.example.datatoolserver.mapper.pushMapper;
import com.example.datatoolserver.mapper.xcuserMapper;
import com.example.datatoolserver.pojo.*;
import com.example.datatoolserver.service.IArticleService;
import com.example.datatoolserver.service.IWeixinInertfaceService;
import com.example.datatoolserver.util.AccessTokenUtil;
import com.example.datatoolserver.util.AliOssUtil;
import com.nimbusds.jose.JOSEException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.example.xcschoolserver.token.AccessTokenUtil;

/**
 * Created by Administrator on 2018/9/18.
 */
@CrossOrigin
@Controller
@RequestMapping("/wxt")
public class WeixinInterfaceController extends HttpServlet {

    @Autowired
    private AccessTokenUtil accessTokenUtil;

    @Autowired
    private IWeixinInertfaceService weixinInertfaceService;

    @Autowired
    private xcuserMapper xcuserMapper;
    @Autowired
    private groupTopMapper groupTopMapper;

    @Autowired
    private pushMapper pushMapper;
    @Autowired
    private IArticleService articleService;

    /**
     * 配置微信公众号基本url，使其获取到token
     * */
    @ResponseBody
    @GetMapping("/xc")
    public void weixininter(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //微信获取解析信息
        String str = WX_Util.check_Url(request);
        PrintWriter out = response.getWriter();
        //输出
        out.print(str);

        //刷新
        out.flush();
        //关闭流
        out.close();
    }



    /**
     * 微信消息功能
     * @param inTestMessage
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/xc", produces = {"application/xml;charset=UTF-8"}, method = RequestMethod.POST)
    public Object handleMessage(@RequestBody InTestMessage inTestMessage) throws JSONException, JOSEException {
        return weixinInertfaceService.handleMessage(inTestMessage);
    }


    /**
     * 添加客服
     */
    @ResponseBody
    @PostMapping("/staff")
    public ReturnVO<String> addStaff(@RequestBody Map<String,Object> data) throws JSONException {

        return new ReturnVO(accessTokenUtil.addStaff(data));
    }


    /**
     * 获取用户授权
     */
//    @ResponseBody
//    @RequestMapping("/person")
//    public String person(String code) throws JSONException {
//        System.out.println(code);
////        通过code换去access_token
//        JSONObject jsonObject = WeChatUtil.getWebAccessToken(code);
//        String access_token = jsonObject.getString("access_token");
//        String openid = jsonObject.getString("openid");
//        //通过access_token和openId拉取用户信息
//        JSONObject userinfo = WeChatUtil.getUserInfo(access_token,openid);
//        System.out.println(userinfo.getString("nickname"));
//        System.out.println("进来了");
//
//        return "person";
//    }

    /**
     * 授权获取用户信息
     * @return
     * @throws JSONException
     */
    @ResponseBody
    @PostMapping(value = "/login")
    public ReturnVO<Map<String,String>> login(@RequestBody Map<String,Object> REurl) throws JSONException, UnsupportedEncodingException {
        String urls = (String) REurl.get("REurl");
        System.out.println(urls);

        String urlstr = URLEncoder.encode(urls,"utf-8");
        String url ="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+WeChatUtil.APPID
                //这个backUrl的域名必须要进行再公众号中进行注册验证，这个地址是成功后的回调地址,在配置文件配置
                + "&redirect_uri="+urlstr
                + "&response_type=code"
                + "&scope=snsapi_userinfo"
                + "&connect_redirect=1"
                + "#wechat_redirect";

        //线下
//        String url ="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8ee656699cb2c84d"
//                //这个backUrl的域名必须要进行再公众号中进行注册验证，这个地址是成功后的回调地址,在配置文件配置
//                + "&redirect_uri="+urlstr
//                + "&response_type=code"
//                + "&scope=snsapi_userinfo"
//                + "&connect_redirect=1"
//                + "#wechat_redirect";

//        String[] a = urls.split("/");
//        String b = "http://joycai.mynatapp.cc/"+a[3];
//        String urlst = URLEncoder.encode(b,"utf-8");
//        String url ="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8ee656699cb2c84d"
//                //这个backUrl的域名必须要进行再公众号中进行注册验证，这个地址是成功后的回调地址,在配置文件配置
//                + "&redirect_uri="+urlst
//                + "&response_type=code"
//                + "&scope=snsapi_userinfo"
//                + "&connect_redirect=1"
//                + "#wechat_redirect";


        System.out.println(url);
        Map<String,String> map = new HashMap<>();
        map.put("url",url);
        return new ReturnVO(map);
    }

    /**
     * 公众号获取用户信息
     * @return
     */
    @PostMapping(value = "/userCode")
    @ResponseBody
    public ReturnVO<Map> getAccessToken(String code) throws JSONException {
//        System.out.println(code);
//        weixinInertfaceService.login(code);
        return new ReturnVO(weixinInertfaceService.login(code));
    }

//    @ResponseBody
//    @PostMapping("/userInfo")
//    public ReturnVO<Map> getAccessToken(@RequestBody Map<String,Object> data) throws JSONException{
//        return new ReturnVO(weixinInertfaceService.logins(data));
//    }



    @PostMapping(value = "/token")
    @ResponseBody
    public ReturnVO<String> token() throws JSONException {
        return new ReturnVO(accessTokenUtil.getAccessToken());
    }

    /**
     * 根据opid查询用户信息
     */
    @ResponseBody
    @PostMapping("/selectUser")
    public ReturnVO<Xcuser> selectUser(String openId){
        return new ReturnVO(weixinInertfaceService.selectUser(openId));
    }



    @ResponseBody
    @PostMapping("/selectOpenId")
    public ReturnVO<String> selectOpenId(@RequestBody Map<String,Object> openId){
        String openIdInfo = (String) openId.get("openId");
        Xcuser userinfo = xcuserMapper.selectOpenIdName(openIdInfo);
        if(userinfo == null){
            return new ReturnVO().error(40000,"该用户信息为空");
        }
        return new ReturnVO("该用户信息正常");
    }

    /**
     * 获取基本access_token
     */
    @ResponseBody
    @GetMapping("/access_token")
    public ReturnVO<String> accessToken() throws JSONException {
        return new ReturnVO(accessTokenUtil.getAccessToken());
    }

    @ResponseBody
    @PostMapping("/updataUser")
    public ReturnVO<String> updataUser(@RequestBody Map<String,Object> data){
        Integer start = Integer.parseInt((String) data.get("start"));
        if (start == 0){
            return new ReturnVO(weixinInertfaceService.updataUserPhone(data));
        }else if (start == 1){
            return new ReturnVO(weixinInertfaceService.updataUserSelfPortrait(data));
        }else if (start == 2){
            return new ReturnVO(weixinInertfaceService.updataUserWeChat(data));
        }else if (start == 3){
            return new ReturnVO(weixinInertfaceService.updataUserEmail(data));
        }else if (start == 4){
            return new ReturnVO(weixinInertfaceService.updataUserCompany(data));
        }else if (start == 5){
            return new ReturnVO(weixinInertfaceService.updataUserBusiness(data));
        }else if (start == 6){
            return new ReturnVO(weixinInertfaceService.updataUserPosition(data));
        }
        return null;
    }


    /**
     * 查询行业
     */
    @ResponseBody
    @GetMapping("/selectProfession")
    public ReturnVO<List<Map<String,Object>>> profession(){
        return new ReturnVO(weixinInertfaceService.profession());
    }



    /**
     * 上传加好友二维码图片
     */
    @RequestMapping("/addImage")
    @ResponseBody
    public ReturnVO<Xcuser> addImage(@RequestBody Map<String,Object> data) {
        String openId = (String) data.get("openId");
        String formerUrl = (String) data.get("formerUrl");
        String url = (String) data.get("url");
        Integer start = Integer.parseInt((String) data.get("start"));

        //新二维码和旧二维码不一样时到oss中删除旧的二维码
        if (!formerUrl.equals(url)){
            String[] b =  formerUrl.split("/");
            List<String> a = new ArrayList<>();
            a.add(b[3]+"/"+b[4]);
            //将之前的二维码删除
            AliOssUtil.deleteObjects(WeChatUtil.BUCKETNAME,a);
        }
        //将二维码更新到数据库
        if (start == 0){
            xcuserMapper.updataUserCodeurl(openId,url);
        }else if (start == 1){
            xcuserMapper.updataUserTikTok(openId,url);
        }else if (start == 2){
            xcuserMapper.updataUserKuaiShou(openId,url);
        }

//        return new ReturnVO(xcuserMapper.selectOpenId(openId));
        return new ReturnVO(weixinInertfaceService.selectUser(openId));
    }





    /**
     *上传产品图片(线上)
     */
    @PostMapping("/addPrImage")
    @ResponseBody
    public ReturnVO<String> addPrImage(@RequestParam(name = "image_data", required = false) MultipartFile file) {
        //创建空file存放文件
        File targetFile=null;
        String url = "";
        String imgUrl = "";
        //获取文件名
        String fileName=file.getOriginalFilename();
        if (!file.isEmpty()) {
            try {
                // 路径
//                String newCompanyImagepath = "E:\\tupian";
                String newCompanyImagepath = "/item/apache-tomcat-7.0.99/webapps/product";
                //获取文件后缀
                String fileF = fileName.substring(fileName.lastIndexOf("."), fileName.length());
                //新的文件名
                String newCompanyImageName=new Date().getTime()+"_"+new Random().nextInt(1000)+fileF;
                //先判断文件是否存在
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String fileAdd = sdf.format(new Date());
                //获取文件夹路径
                File file1 =new File(newCompanyImagepath+"/"+fileAdd);
                //如果文件夹不存在则创建
                if(!file1 .exists()  && !file1 .isDirectory()){
                    file1 .mkdir();
                }

                targetFile = new File(file1, newCompanyImageName);


                if (!targetFile.exists()) {
                    targetFile.createNewFile();
                }
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(targetFile));
                out.write(file.getBytes());
                //文件路径url
//                url=newCompanyImagepath+"\\"+fileAdd+"\\"+newCompanyImageName;
                url="http://"+"xiaocisw.site:8044/product/"+fileAdd+"/"+newCompanyImageName;
//                imgUrl = "http://xiaocisw.site:8044/userQR/20200306/1583453493441_258.jpg";

                out.flush();
                out.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return new ReturnVO().error(40000,"图片上传失败");
            } catch (IOException e) {
                e.printStackTrace();
                return new ReturnVO().error(40000,"图片上传失败");
            }
        }
//        System.out.println(url);
        return new ReturnVO(url);
    }


    /**
     *上传产品图片(本地)
     */
//    @PostMapping("/addPrImage")
//    @ResponseBody
//    public ReturnVO<String> addPrImage(@RequestParam(name = "image_data", required = false) MultipartFile file) {
//        //创建空file存放文件
//        File targetFile=null;
//        String url = "";
//        String imgUrl = "";
//        //获取文件名
//        String fileName=file.getOriginalFilename();
//        if (!file.isEmpty()) {
//            try {
//                // 路径
//                String newCompanyImagepath = "E:\\开发软件\\tomcat\\apache-tomcat-8.5.53\\webapps\\images";
//                //获取文件后缀
//                String fileF = fileName.substring(fileName.lastIndexOf("."), fileName.length());
//                //新的文件名
//                String newCompanyImageName=new Date().getTime()+"_"+new Random().nextInt(1000)+fileF;
//                //先判断文件是否存在
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//                String fileAdd = sdf.format(new Date());
//                //获取文件夹路径
//                File file1 =new File(newCompanyImagepath+"/"+fileAdd);
//                //如果文件夹不存在则创建
//                if(!file1 .exists()  && !file1 .isDirectory()){
//                    file1 .mkdir();
//                }
//
//                targetFile = new File(file1, newCompanyImageName);
//
//
//                if (!targetFile.exists()) {
//                    targetFile.createNewFile();
//                }
//                BufferedOutputStream out = new BufferedOutputStream(
//                        new FileOutputStream(targetFile));
//                out.write(file.getBytes());
//                //文件路径url
//                url="192.168.0.157:8083/images/"+fileAdd+"/"+newCompanyImageName;
//
//                out.flush();
//                out.close();
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                return new ReturnVO().error(40000,"图片上传失败");
//            } catch (IOException e) {
//                e.printStackTrace();
//                return new ReturnVO().error(40000,"图片上传失败");
//            }
//        }
////        System.out.println(url);
//        return new ReturnVO(url);
//    }

//    @PostMapping("/addPrImage")
//    @ResponseBody
//    public ReturnVO<String> addPrImage(@RequestParam("image_data") MultipartFile file) throws IOException {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
//        //创建布尔
//        boolean bo;
//        //获取文件名
//        String fileName=file.getOriginalFilename();
//        //新文件名
//        DateFormat dateTimeformat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
//        String newFileName = dateTimeformat.format(new Date())+fileName.replaceAll(" ", "");
//
//        // 上传路径
//        String path = "E:\\开发软件\\tomcat\\apache-tomcat-8.5.53\\webapps\\images\\";
//        File f = new File(path);
//
//        if (!f.exists()){
//            f.mkdir();
//        }
//        if (!file.isEmpty()){
//            FileInputStream in = (FileInputStream) file.getInputStream();
//            FileOutputStream out = new FileOutputStream(new File(path + newFileName));
//            try {
//                FileChannel fcIn = in.getChannel();
//                FileChannel fcOut = out.getChannel();
//                ByteBuffer buffer = ByteBuffer.allocate(1024);
//                while (true){
//                    buffer.clear();
//                    int r = fcIn.read(buffer);
//                    if (r == -1){
//                        break;
//                    }
//                    buffer.flip();
//                    fcOut.write(buffer);
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }finally {
//                in.close();
//                out.close();
//            }
//        }
//        bo = true;
//        //文件路径url
//        String url="192.168.0.157:8083/images/"+newFileName;
//        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        System.out.println(dfs.format(new Date()));// new Date()为获取当前系统时间
//        return new ReturnVO(url);
//    }



    /**
     * 创建团队
     */
    @ResponseBody
    @PostMapping("/insertGroup")
    public ReturnVO<Map<String,Object>> insertGroup(@RequestBody Map<String,Object> group){
        Map<String,Object> a = weixinInertfaceService.insetGroup(group);
        if(a.get("info") == "该团队已经存在"){
            return new ReturnVO().error(40000,"该团队已经存在");
        }
        return new ReturnVO(a);
    }



    /**
     * 修改团队名称
     */
    @ResponseBody
    @PostMapping("/updataGroup")
    public ReturnVO<Map<String, Object>> updataGroup(@RequestBody Map<String,Object> group){
        String groupToken = (String) group.get("groupToken");
        String groupName = (String) group.get("groupName");
        groupTopMapper.updataGroup(groupToken,groupName);
        return new ReturnVO(weixinInertfaceService.selectGroupName(group));
    }

    /**
     * 查询团队详情(管理团队)
     */
    @ResponseBody
    @PostMapping("/selectGroupInfo")
    public ReturnVO<Map<String,Object>> selectGroupInfo(@RequestBody Map<String,Object> data){
        return new ReturnVO(weixinInertfaceService.selectGroupInfo(data));
    }

    /**
     * 查询团队(团队数据追踪)
     */
    @ResponseBody
    @PostMapping("/selectGroup")
    public ReturnVO<Map<String, Object>> selectGroup(String openId){
        return new ReturnVO(weixinInertfaceService.selectGroup(openId));
    }

    /**
     * 查询团队列表
     */
    @ResponseBody
    @PostMapping("/selectGroupName")
    public ReturnVO<List> selectGroupName(@RequestBody Map<String,Object> datas){
        List<Map<String,Object>> map = weixinInertfaceService.selectGroupName(datas);
        if (map == null){
            return new ReturnVO().error(40000,"还没有团员哟!!!!");
        }
        return new ReturnVO(map);
    }

    /**
     * 切换团队
     */
    @ResponseBody
    @PostMapping("/switchGroup")
    public ReturnVO<Map<String, Object>> switchGroup(@RequestBody Map<String,Object> group){
        return new ReturnVO(weixinInertfaceService.switchGroup(group));
    }

    /**
     * 邀请成员
     */
    @ResponseBody
    @PostMapping("/insertGroupMember")
    public ReturnVO<String> insertGroupMember(@RequestBody Map<String,Object> group){
        String a = weixinInertfaceService.insertGroupMember(group);
        if(a.equals("没有该用户信息")){
            return new ReturnVO().error(40000,"没有该用户信息");
        }else if(a.equals("已经是该团队成员")){
            return new ReturnVO().error(40000,"已经是该团队成员");
        }
        return new ReturnVO(a);
    }


    /**
     * 设置权限
     */
    @ResponseBody
    @PostMapping("/startGroup")
    public ReturnVO<Map<String, Object>> startGroup(@RequestBody Map<String,Object> start){
        return new ReturnVO(weixinInertfaceService.startGroup(start));
    }

    /**
     * 删除团队成员
     */
    @ResponseBody
    @PostMapping("/deleteGroup")
    public ReturnVO<String> deleteGroup(@RequestBody List<Map<String,Object>> groups){

        return new ReturnVO(weixinInertfaceService.deleteGroup(groups));
    }

    /**
     * 退出团队
     */
    @ResponseBody
    @PostMapping("/exitGroup")
    public ReturnVO<String> exitGroup(@RequestBody Map<String,Object> data){
        return new ReturnVO(weixinInertfaceService.exitGroup(data));
    }

    /**
     * 解散团队
     */
    @ResponseBody
    @PostMapping("/quitGroup")
    public ReturnVO<String> quitGroup(@RequestBody Map<String,Object> group){
        String a = weixinInertfaceService.quitGroup(group);
        if (a.equals("不是创始人")){
            return new ReturnVO().error(40000,"您不是创始人不能解散团队");
        }
        return new ReturnVO(a);
    }



    /**
     * 早晚安打卡
     */
    @ResponseBody
    @PostMapping("/sign")
    public ReturnVO<Map<String,Object>> sign(String openId){
        return new ReturnVO(weixinInertfaceService.sign(openId));
    }





    /**
     * 自动执行的方法
     */
    /**
     * 创建微信菜单
     */
    @Component
    @Order(value = 1)
    public class createMenu implements CommandLineRunner {
        @Override
        public void run(String... strings) throws Exception {
            System.out.println("##################这是创建微信菜单的方法##################");
//            accessTokenUtil.getAccessToken();
            accessTokenUtil.createMenu();
        }
    }

    /**
     * 查询推送设置
     * @param data
     * @return
     */
    @ResponseBody
    @PostMapping("/selectPush")
    public ReturnVO<List<Map<String,Object>>> selectPush(@RequestBody Map<String,Object> data){
        String openId = (String) data.get("openId");
        List<Map<String,Object>> datas = new ArrayList<>();
        Push pu = pushMapper.selectPush(openId);
        Map<String,Object> dataInfo1 = new HashMap<>();
        Map<String,Object> dataInfo2 = new HashMap<>();
        Map<String,Object> dataInfo3 = new HashMap<>();
        Map<String,Object> dataInfo4 = new HashMap<>();
        Map<String,Object> dataInfo5 = new HashMap<>();
        Map<String,Object> dataInfo6 = new HashMap<>();
        if (pu == null){
            pushMapper.insertPush(1,1,1,1,1,1,openId);
            pu = pushMapper.selectPush(openId);

            if(pu.getPush_matinal() == 1){
                dataInfo1.put("time","07:00~08:00");
                dataInfo1.put("start",true);
                datas.add(dataInfo1);
            }else if (pu.getPush_matinal() == 0){
                dataInfo1.put("time","07:00~08:00");
                dataInfo1.put("start",false);
                datas.add(dataInfo1);
            }

            if(pu.getPush_morning() == 1){
                dataInfo2.put("time","09:00~10:00");
                dataInfo2.put("start",true);
                datas.add(dataInfo2);
            }else if (pu.getPush_morning() == 0){
                dataInfo2.put("time","09:00~10:00");
                dataInfo2.put("start",false);
                datas.add(dataInfo2);
            }

            if(pu.getPush_nooning() == 1){
                dataInfo3.put("time","12:00~13:00");
                dataInfo3.put("start",true);
                datas.add(dataInfo3);
            }else if (pu.getPush_nooning() == 0){
                dataInfo3.put("time","12:00~13:00");
                dataInfo3.put("start",false);
                datas.add(dataInfo3);
            }

            if(pu.getPush_afternoon() == 1){
                dataInfo4.put("time","15:00~16:00");
                dataInfo4.put("start",true);
                datas.add(dataInfo4);
            }else if (pu.getPush_afternoon() == 0){
                dataInfo4.put("time","15:00~16:00");
                dataInfo4.put("start",false);
                datas.add(dataInfo4);
            }

            if(pu.getPush_evening() == 1){
                dataInfo5.put("time","18:00~19:00");
                dataInfo5.put("start",true);
                datas.add(dataInfo5);
            }else if (pu.getPush_evening() == 0){
                dataInfo5.put("time","18:00~19:00");
                dataInfo5.put("start",false);
                datas.add(dataInfo5);
            }

            if(pu.getPush_night() == 1){
                dataInfo6.put("time","21:00~22:00");
                dataInfo6.put("start",true);
                datas.add(dataInfo6);
            }else if (pu.getPush_night() == 0){
                dataInfo6.put("time","21:00~22:00");
                dataInfo6.put("start",false);
                datas.add(dataInfo6);
            }

            return new ReturnVO(datas);
        }

        if(pu.getPush_matinal() == 1){
            dataInfo1.put("time","07:00~08:00");
            dataInfo1.put("start",true);
            datas.add(dataInfo1);
        }else if (pu.getPush_matinal() == 0){
            dataInfo1.put("time","07:00~08:00");
            dataInfo1.put("start",false);
            datas.add(dataInfo1);
        }

        if(pu.getPush_morning() == 1){
            dataInfo2.put("time","09:00~10:00");
            dataInfo2.put("start",true);
            datas.add(dataInfo2);
        }else if (pu.getPush_morning() == 0){
            dataInfo2.put("time","09:00~10:00");
            dataInfo2.put("start",false);
            datas.add(dataInfo2);
        }

        if(pu.getPush_nooning() == 1){
            dataInfo3.put("time","12:00~13:00");
            dataInfo3.put("start",true);
            datas.add(dataInfo3);
        }else if (pu.getPush_nooning() == 0){
            dataInfo3.put("time","12:00~13:00");
            dataInfo3.put("start",false);
            datas.add(dataInfo3);
        }

        if(pu.getPush_afternoon() == 1){
            dataInfo4.put("time","15:00~16:00");
            dataInfo4.put("start",true);
            datas.add(dataInfo4);
        }else if (pu.getPush_afternoon() == 0){
            dataInfo4.put("time","15:00~16:00");
            dataInfo4.put("start",false);
            datas.add(dataInfo4);
        }

        if(pu.getPush_evening() == 1){
            dataInfo5.put("time","18:00~19:00");
            dataInfo5.put("start",true);
            datas.add(dataInfo5);
        }else if (pu.getPush_evening() == 0){
            dataInfo5.put("time","18:00~19:00");
            dataInfo5.put("start",false);
            datas.add(dataInfo5);
        }

        if(pu.getPush_night() == 1){
            dataInfo6.put("time","21:00~22:00");
            dataInfo6.put("start",true);
            datas.add(dataInfo6);
        }else if (pu.getPush_night() == 0){
            dataInfo6.put("time","21:00~22:00");
            dataInfo6.put("start",false);
            datas.add(dataInfo6);
        }
        return new ReturnVO(datas);
    }

    /**
     * 更新推送设置
     */
    @ResponseBody
    @PostMapping("/updataPush")
    public ReturnVO<String> updataPush(@RequestBody Map<String,Object> data){
        List<Integer> push = (List<Integer>) data.get("push");
        String openId = (String) data.get("openId");
        pushMapper.updataPush(push.get(0),push.get(1),push.get(2),push.get(3),push.get(4),push.get(5),openId);
        return new ReturnVO("更新成功");
    }


    @RequestMapping("/addImagess")
    @ResponseBody
    public ReturnVO<String> addImagess(@RequestParam(name = "image_data", required = false) MultipartFile file, String path) throws Exception {
        AliOssUtil.uploadOSSFree(file, path);
        return new ReturnVO("成功");
    }

    @RequestMapping("/deleteimg")
    @ResponseBody
    public ReturnVO<String> deleteimg(String bucketName,String bucketUrls){
        System.out.println(bucketName);
        System.out.println(bucketUrls);
        List<String> a = new ArrayList<>();
        a.add(bucketUrls);
        AliOssUtil.deleteObjects(bucketName,a);

        return new ReturnVO("成功");
    }


    /**
     * oss参数
     */
    @ResponseBody
    @PostMapping("/ossInfo")
    public ReturnVO<Map<String,Object>> ossInfo(){
        Map<String,Object> map = new HashMap<>();
        map.put("endpoint",WeChatUtil.ENDPOINT);
        map.put("keyid",WeChatUtil.KEYID);
        map.put("keysecret",WeChatUtil.KEYSECRET);
        map.put("bucketname",WeChatUtil.BUCKETNAME);
        return new ReturnVO(map);
    }
}
