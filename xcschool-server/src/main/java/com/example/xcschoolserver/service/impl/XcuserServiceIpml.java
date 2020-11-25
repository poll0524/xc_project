package com.example.xcschoolserver.service.impl;

import com.example.xcschoolserver.common.WeChatUtil;
import com.example.xcschoolserver.mapper.adminUserMapper;
import com.example.xcschoolserver.mapper.userShoppingMapper;
import com.example.xcschoolserver.mapper.xcuserMapper;
import com.example.xcschoolserver.pojo.AdminUser;
import com.example.xcschoolserver.pojo.UserShopping;
import com.example.xcschoolserver.pojo.Xcuser;
import com.example.xcschoolserver.service.IXcuserService;
import com.example.xcschoolserver.util.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class XcuserServiceIpml implements IXcuserService {
    @Autowired
    private userShoppingMapper userShoppingMapper;
    @Autowired
    private xcuserMapper xcuserMapper;
    @Autowired
    private adminUserMapper adminUserMapper;

    /**
     * 展示二维码图片
     */
    public Map<String,Object> shopping(Map<String,Object> data){
        String openId = (String) data.get("openId");
        String codeSite = "http://xiaocisw.site:8044/serviceUser/xiaofenyun/hejie.png";
        //到数据库查询是否有数据
        UserShopping userShopping = userShoppingMapper.selectOpenId(openId);
        Map<String,Object> datainfo = new HashMap<>();
        if (userShopping == null){
            userShoppingMapper.insertUserShopping(openId,codeSite);
            datainfo.put("shoppingSite","");
            datainfo.put("codeSite",codeSite);
            datainfo.put("start",false);
        }else {
            if (userShopping.getShopping_site() == null){
                datainfo.put("shoppingSite","");
                datainfo.put("codeSite",userShopping.getCode_site());
                datainfo.put("start",false);
            }else {
                datainfo.put("shoppingSite",userShopping.getShopping_site());
                datainfo.put("codeSite",userShopping.getCode_site());
                datainfo.put("start",true);
            }
        }
        return datainfo;
    }

    /**
     * 修改客服图片
     * @param data
     * @return
     */
    public Map<String,Object> updataCode(Map<String,Object> data){
        //用户唯一标识
        String openId = (String) data.get("openId");
        //用户修改的二维码
        String codeSite = (String) data.get("codeSite");
        //用户旧的二维码
        String formerUrl = (String) data.get("formerUrl");
        if(!formerUrl.equals("http://xiaocisw.site:8044/serviceUser/xiaofenyun/hejie.png")){
            String[] b =  formerUrl.split("/");
            List<String> a = new ArrayList<>();
            a.add(b[3]+"/"+b[4]);
            //将之前的二维码删除
            AliOssUtil.deleteObjects(WeChatUtil.BUCKETNAME,a);
        }
        userShoppingMapper.updataOpenId(openId,codeSite);
        Map<String,Object> a = new HashMap<>();
        a.put("openId",openId);

        return shopping(a);
    }

    /**
     * 查询会员列表
     */
    public Map<String,Object> selectUser(Map<String,Object> data,String userToken){
//        AdminUser adminUser = adminUserMapper.selectToken(userToken);
//        if (adminUser == null){
//            return null;
//        }
        Integer pag = Integer.parseInt((String) data.get("pag"))*20;
        List<Xcuser> xcuserList = xcuserMapper.selectxcuser();
        List<Xcuser> xcusers = xcuserMapper.selectxcusers(pag);
        List<Map<String,Object>> dataList = new ArrayList<>();
        for (Xcuser xcuser : xcusers) {
            Map<String,Object> datas = new HashMap<>();
            datas.put("id",xcuser.getUserid());
            datas.put("openId",xcuser.getOpen_id());
            datas.put("headimgurl",xcuser.getUser_headimgurl());
            datas.put("name",xcuser.getUser_name());
            datas.put("sex",xcuser.getUser_sex());
            datas.put("city",xcuser.getUser_city());
            datas.put("start",xcuser.getStart());
            datas.put("sign",xcuser.getSign());
            if (xcuser.getUser_phone() == null){
                datas.put("phone","");

            }else {
                datas.put("phone",xcuser.getUser_phone());
            }
            datas.put("balance",xcuser.getUser_balance());
            dataList.add(datas);
        }
        Map<String,Object> userdata = new HashMap<>();
        userdata.put("userdata",dataList);
        userdata.put("userquantity",xcuserList.size());
        return userdata;
    }

    /**
     * 查询用户详情
     */
    public Xcuser userInfo(Map<String,Object> data,String userToken){
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser == null){
            return null;
        }
        String openId = (String) data.get("openId");
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);;
        if (xcuser == null){
            return null;
        }
        return xcuser;
    }

    public Map<String,Object> userSearch(Map<String,Object> data,String userToken){
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser == null){
            return null;
        }
        String name = "%" + (String) data.get("name") + "%";
        Integer pag = Integer.parseInt((String) data.get("pag"))*20;
        List<Xcuser> xcuserList = xcuserMapper.selectNameLike(name);
        List<Xcuser> xcusers = xcuserMapper.selectNameLikes(name,pag);
        List<Map<String,Object>> dataList = new ArrayList<>();
        for (Xcuser xcuser : xcusers) {
            Map<String,Object> datas = new HashMap<>();
            datas.put("id",xcuser.getUserid());
            datas.put("openId",xcuser.getOpen_id());
            datas.put("name",xcuser.getUser_name());
            datas.put("sex",xcuser.getUser_sex());
            datas.put("city",xcuser.getUser_city());
            datas.put("start",xcuser.getStart());
            datas.put("sign",xcuser.getSign());
            if (xcuser.getUser_phone() == null){
                datas.put("phone","");
            }else {
                datas.put("phone",xcuser.getUser_phone());
            }
            datas.put("balance",xcuser.getUser_balance());
            dataList.add(datas);
        }
        Map<String,Object> userdata = new HashMap<>();
        userdata.put("userdata",dataList);
        userdata.put("userquantity",xcuserList.size());
        return userdata;
    }

    /**
     * 更新用户信息
     */
    public String updataUser(Map<String,Object> data,String userToken){
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser == null){
            return null;
        }
        //用户唯一id
        String openId = (String) data.get("openId");
        //用户昵称
        String userName = (String) data.get("userName");
        //用户电话
        String userPhone = (String) data.get("userPhone");
        //用户性别
        String userSex = (String) data.get("userSex");
        //所在省份
        String userProvince = (String) data.get("userProvince");
        //所在城市
        String userCity = (String) data.get("userCity");
        //所在国家
        String userCountry = (String) data.get("userCountry");
        //头像地址
        String userHeadimgUrl = (String) data.get("userHeadimgUrl");
        //二维码图片
        String codeUrl = (String) data.get("codeUrl");
        //会员等级
        Integer start = (Integer) data.get("start");
        //可创建品牌数量
        Integer brandQuantity = (Integer) data.get("brandQuantity");
        //邮箱
        String email = (String) data.get("email");
        //微信号
        String wechat = (String) data.get("wechat");
        //形象照
        String selfPortrait = (String) data.get("selfPortrait");
        //公司名称
        String company = (String) data.get("company");

        //查询该用户信息
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);

        String beginTime = "";
        String endTime = "";
        String vipOrderId = "";
        //计算vip时间
        if(xcuser.getStart() != start){
            if (start == 0){
                beginTime = null;
                endTime = null;
                vipOrderId = null;
                brandQuantity = 0;
            }else if (start == 1){
                brandQuantity = 0;
                //获取当前时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                beginTime = df.format(new Date());
                //结束时间
                Calendar calendar2 = Calendar.getInstance();
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                calendar2.add(Calendar.DATE, 7);
                endTime = sdf2.format(calendar2.getTime());
                //用户单号
                vipOrderId = getOrderId();
            }else if (start == 2){
                brandQuantity = 0;
                //获取当前时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                beginTime = df.format(new Date());
                //结束时间
                Calendar calendar2 = Calendar.getInstance();
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                calendar2.add(Calendar.YEAR, 1);
                endTime = sdf2.format(calendar2.getTime());
                //用户单号
                vipOrderId = getOrderId();
            }else if (start == 3){
                brandQuantity = 0;
                //获取当前时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                beginTime = df.format(new Date());
                //结束时间
                Calendar calendar2 = Calendar.getInstance();
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                calendar2.add(Calendar.YEAR, 3);
                endTime = sdf2.format(calendar2.getTime());
                //用户单号
                vipOrderId = getOrderId();
            }else if (start == 4){
                if (brandQuantity == null || brandQuantity == 0){
                    brandQuantity = 1;
                }
                //获取当前时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                beginTime = df.format(new Date());
                //结束时间
                endTime = "0";
                //用户单号
                vipOrderId = getOrderId();
            }else if (start == 5){
                if (brandQuantity == null || brandQuantity < 3){
                    brandQuantity = 3;
                }
                //获取当前时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                beginTime = df.format(new Date());
                //结束时间
                endTime = "0";
                //用户单号
                vipOrderId = getOrderId();
            }
            xcuserMapper.updataUser(openId,userName,userPhone,userSex,userProvince,userCity,userCountry,userHeadimgUrl,codeUrl,start,beginTime,endTime,vipOrderId,brandQuantity,email,wechat,selfPortrait,company);
        }else if (start == 0 && start == xcuser.getStart()){
            xcuserMapper.updataUser(openId,userName,userPhone,userSex,userProvince,userCity,userCountry,userHeadimgUrl,codeUrl,0,null,null,null,0,email,wechat,selfPortrait,company);
        }else if (start != 0 && start == xcuser.getStart()){
            if (start == 4){
                if (brandQuantity == null || brandQuantity == 0){
                    brandQuantity = 1;
                }
            }else if (start == 5){
                if (brandQuantity == null || brandQuantity == 0){
                    brandQuantity = 1;
                }
            }
            xcuserMapper.updataUser(openId,userName,userPhone,userSex,userProvince,userCity,userCountry,userHeadimgUrl,codeUrl,xcuser.getStart(),xcuser.getBegin_time(),xcuser.getEnd_time(),xcuser.getVip_order_id(),brandQuantity,email,wechat,selfPortrait,company);
        }

        return "更新成功";
    }


    /**
     * 生成订单号
     * @return
     */
    public String getOrderId() {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar calendar = Calendar.getInstance();
        String dateName = df.format(calendar.getTime());

        Random ne=new Random();//实例化一个random的对象ne
        int x = ne.nextInt(999-100+1)+100;//为变量赋随机值100-999
        String random_order = String.valueOf(x);
        String orderId = dateName+random_order;
        return orderId;
    }

//    public Map<String,Object> test(Map<String,Object> data){
//        //结束时间
//        String endTime = data.get("endTime");
//        //投入资金
//        Integer
//    }
}
