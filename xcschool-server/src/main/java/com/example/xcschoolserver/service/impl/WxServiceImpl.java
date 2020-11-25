package com.example.xcschoolserver.service.impl;

import com.example.xcschoolserver.common.*;
import com.example.xcschoolserver.mapper.*;
import com.example.xcschoolserver.pojo.*;
import com.example.xcschoolserver.service.IWxService;
import com.example.xcschoolserver.task.VipInfo;
import com.example.xcschoolserver.util.AccessTokenUtil;
import com.example.xcschoolserver.util.MD5Util;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author saodiseng
 * @date 2019/2/18
 */
@Service
@Slf4j
public class WxServiceImpl implements IWxService {
    @Autowired
    private AccessTokenUtil accessTokenUtil;
    @Autowired
    private VipInfo vipInfo;
    @Autowired
    private xcuserMapper xcuserMapper;
    @Autowired
    private cashMapper cashMapper;
    @Autowired
    private vipOredermapper vipOredermapper;
    @Autowired
    private courseBuyMapper courseBuyMapper;
    @Autowired
    private shineUponMapper shineUponMapper;


    public static final String SPBILL_CREATE_IP = "127.0.0.1";
    //开通会员回调地址
//    public static final String NOTIFY_URL = "http://poll.mynatapp.cc/order/notify";
    public static final String NOTIFY_URL = "http://xiaocisw.site/order/notify";


    public static final String TRADE_TYPE_APP = "JSAPI";


    @Override
    public String payBack(String resXml) {
        WXConfigUtil config = null;
        try {
            config = new WXConfigUtil();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WXPay wxpay = new WXPay(config);
        String xmlBack = "";
        Map<String, String> notifyMap = null;
        try {
            notifyMap = WXPayUtil.xmlToMap(resXml);         // 调用官方SDK转换成map类型数据
            System.out.println(notifyMap.toString());
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {//验证签名是否有效，有效则进一步处理

                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//商户订单号

                String attach = notifyMap.get("attach");
                //string转json类型
                JSONObject jsonObject = new JSONObject(attach.toString());
                System.out.println(jsonObject.toString());
                //获取用户唯一标识
                String openId = jsonObject.getString("b");
                //获取分享人唯一标识
                String readOpenId = jsonObject.getString("c");
                //获取支付类型:0开通会员,1购买品牌,2购买课程,3修改品牌名字,4删除品牌
                String type = jsonObject.getString("a");
                //获取支付方式:0为自己支付,1为通过分享人分享支付
                String start = jsonObject.getString("d");
                //获取会员开通等级
                String grade = jsonObject.getString("e");






                //获取支付金额
                String total_fee = notifyMap.get("total_fee");
//                String total_fee = "299900";
//                System.out.println(total_fee);
                if (return_code.equals("SUCCESS")) {
                    if (out_trade_no != null) {
                        // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户的订单状态从退款改成支付成功
                        // 注意特殊情况：微信服务端同样的通知可能会多次发送给商户系统，所以数据持久化之前需要检查是否已经处理过了，处理了直接返回成功标志
                        //业务数据持久化
                        log.info("微信手机支付回调成功订单号:{}", out_trade_no);
                        xmlBack = "<xml>\n" + "<return_code><![CDATA[SUCCESS]]></return_code>\n" + "<return_msg><![CDATA[OK]]></return_msg>\n" + "</xml> ";


                        //调用二级分销方法
                        vipInfo.shareOrder(openId,readOpenId,total_fee,out_trade_no,start,type);
                        //调用设置会员方法
                        if (type.equals("0")){//支付方式为开通会员时
                            //写入支付记录
                            vipInfo.userVip(openId,out_trade_no,grade);
                            Xcuser xcuser = xcuserMapper.selectOpenId(openId);
//                            if (readXcuser != null){//当分享人信息不为null
//                                //把分享人邀请开通数+1
//                                xcuserMapper.updataReadVip(readOpenId);
//                                //再次查询分享人信息
//                                Xcuser readXcusers = xcuserMapper.selectOpenId(readOpenId);
//                               if (readXcusers.getRead_vip() >= 5){//当邀请人为5大于5时
//                                   if (readXcuser.getStart()<2){//判断分享人等级是否小于2如果小于2时,提升会员等级到2
//                                       //获取当前时间
//                                       SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//                                       String beginTime = df.format(new Date());
//                                       //结束时间
//                                       Calendar calendar2 = Calendar.getInstance();
//                                       SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                       calendar2.add(Calendar.YEAR, 1);
//                                       String endTime = sdf2.format(calendar2.getTime());
//                                       //更新会员等级
//                                       xcuserMapper.updataUserStarts(readOpenId,2,beginTime,endTime);
//                                   }
//                               }
//                            }
                            String string = "‼‼不能错过的消息\n" +
                                    "\n❣亲爱的"+xcuser.getUser_name()+"，恭喜您开通孝粉云学堂会员\n";
                            String result = accessTokenUtil.staffTxt(string,xcuser.getOpen_id());
                            JSONObject jsonResult = new JSONObject(result);
                            String errcode = jsonResult.getString("errcode");
                            System.out.println("*****************************************************************");
                            if (errcode.equals("45015")){
                                System.out.println("用户:"+xcuser.getUser_name()+",支付成功");
                                System.out.println("支付消息发送:失败");
                                System.out.println("原因:45015,回复时间超过限制");
                            }else if (errcode.equals("40003")){
                                System.out.println("用户:"+xcuser.getUser_name()+",支付成功");
                                System.out.println("支付消息发送:失败");
                                System.out.println("原因:40003,不合法的 OpenID ，请开发者确认 OpenID （该用户）是否已关注公众号，或是否是其他公众号的 OpenID");
                            }else if (errcode.equals("0")){
                                System.out.println("用户:"+xcuser.getUser_name()+",支付成功");
                                System.out.println("支付消息发送:成功");
                            }
                            System.out.println("*****************************************************************");
                        }else if (type.equals("1")){//支付方式为购买品牌时
                            //向数据库更新品牌数量
                            xcuserMapper.updataBrandQuantity(openId);
                        }else if (type.equals("2")){//支付方式为购买课程时
                            //将课程购买记录写入课程购买记录表
                            Double orderMoney = Double.valueOf(total_fee);

                            String tell = jsonObject.getString("i");
                            //获取当前时间
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                            String beginTime = df.format(new Date());
                            //获取课程主键id
                            String courseIds = jsonObject.getString("g");
                            //获取区分平台课程和品牌课程的标识
                            String tags = jsonObject.getString("f");
                            Integer courseId = Integer.parseInt(courseIds);
                            System.out.println(tags);
                            Integer tag = Integer.parseInt(tags);
                            String courseHourIds = jsonObject.getString("h");
                            String[] arrStr = courseHourIds.split("a");
                            List<String> courseHourId = Arrays.asList(arrStr);
                            for (String s : courseHourId) {
                                Integer ss = Integer.parseInt(s);
                                courseBuyMapper.insertCourseBuy(openId,courseId,ss,orderMoney,beginTime,tag,Integer.parseInt(tell));
                            }
                        }


                    } else {
                        log.info("微信手机支付回调失败订单号:{}", out_trade_no);
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }
                }
                return xmlBack;
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                //失败的数据要不要存储？
                log.error("手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            log.error("手机支付回调通知失败", e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }

    /**
     * 拉取支付
     */
    @Override
    public Map doUnifiedOrder(Map<String, Object> dataplay) throws Exception {
        //获取openId
        String openId = (String) dataplay.get("openId");
        //获取分享人openId
        String readOpenId = (String) dataplay.get("readOpenId");
        //支付类型:0开通会员,1购买品牌,2购买课程,3修改品牌名字,4删除品牌
        String start = (String) dataplay.get("start");

        String fy = (String) dataplay.get("tell");
        //获取价格
//        Integer price = Integer.parseInt((String) dataplay.get("price"))*100;

        Double price = Double.parseDouble(dataplay.get("price").toString())*100;


        //获取用户开通会员等级,其他支付传0,1为7天体验会员,2为一年会员,3为3年会员,4为品牌合伙人,5为城市合伙人
        Integer grade = Integer.parseInt((String) dataplay.get("grade"));

        //课程id
        Integer courseId = (Integer) dataplay.get("courseId");

        //课时id
        List<String> courseHourIds = (List<String>) dataplay.get("courseHourIds");
        String courseHourId = "";
        if (courseHourIds != null){
            if (courseHourIds.size() != 0){
                courseHourId = Joiner.on("a").join(courseHourIds);
            }
        }else {
            courseHourId = null;
        }
        //区分平台课程和品牌课程
        String tags = (String) dataplay.get("sign");

        //当价格为0时
        if (price == 0 && start.equals("2")){

            //获取当前时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String beginTime = df.format(new Date());
            for (String s : courseHourIds) {
                Integer ss = Integer.parseInt(s);
                //要改正
                Integer c = Integer.parseInt(fy);
                CourseBuy f = courseBuyMapper.selectCourseBuys(openId,courseId,ss,Integer.parseInt(tags),c);
                if (f == null){
                    courseBuyMapper.insertCourseBuy(openId,courseId,ss,0.00,beginTime,Integer.parseInt(tags),c);
                }else {
                    continue;
                }
            }
            Map<String,Object> p = new HashMap<>();
            p.put("info","支付成功");
            return p;
        }

        //调用方法,判断用户是否存在对应关系
        Map<String,Object> dataInfo = vipInfo.shineUpon(openId,readOpenId);
        //如果tell为0不存在对应关系,可新建对应关系
        //如果tell为1纯在对应关系
        String tell = (String) dataInfo.get("start");

        //整理数据,用于用户支付成功后,需要用到的数据
        //type:支付类型:0开通会员,1购买品牌,2购买课程,3修改品牌名字,4删除品牌
        // openid:用户唯一标识,readOpenId:分享用户唯一标识,start:0为自己支付,1为通过分享人分享支付,grade:用户开通会员等级,其他支付传0
        String attach = "";

        if (tell.equals("0")){
            attach = "{a:"+start+",b:"+openId+",c:"+readOpenId+",d:"+tell+",e:"+grade+",f:"+tags+",g:"+courseId+",h:"+courseHourId+",i:"+fy+"}";
        }else {
            attach = "{a:"+start+",b:"+openId+",c:"+dataInfo.get("readOpenId")+",d:"+tell+",e:"+grade+",f:"+tags+",g:"+courseId+",h:"+courseHourId+",i:"+fy+"}";
        }

        try {
            WXConfigUtil config = new WXConfigUtil();
            WXPay wxpay = new WXPay(config);
            Map<String, String> data = new HashMap<>();
            //生成商户订单号，不可重复
            data.put("appid", config.getAppID());
            //商户号
            data.put("mch_id", config.getMchID());
            //随机字符串
            String nonce_str = WXPayUtil.generateNonceStr();
            data.put("nonce_str", nonce_str);
            //订单名称
            String body = "";
            if (start.equals("0")){
                body = "开通会员";
            }else if (start.equals("1")){
                body = "购买品牌";
            }else if (start.equals("2")){
                body = "购买课程";
            }else if (start.equals("3")){
                body = "修改品牌名称";
            }else if (start.equals("4")){
                body = "删除品牌";
            }

            data.put("body", body);
            //订单编号
            String orderId = getOrderId();
            data.put("out_trade_no", orderId);
            //价格
            data.put("total_fee", String.valueOf(new Double(price).intValue()));
//            data.put("total_fee","1");
            //用户openId
            data.put("openid", openId);
            //服务器ip地址
            data.put("spbill_create_ip", SPBILL_CREATE_IP);
            //异步通知地址（请注意必须是外网）,回调地址
            data.put("notify_url", NOTIFY_URL);
            //交易类型
            data.put("trade_type", TRADE_TYPE_APP);
            //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
            data.put("attach", attach);
            //sign签名,md5加密
            String sign = WXPayUtil.generateSignature(data, config.getKey(),
                    WXPayConstants.SignType.MD5);
            data.put("sign", sign);

            //使用官方API请求预付订单
            Map<String, String> response = wxpay.unifiedOrder(data);

            System.out.println(response.toString());

            if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数
                //返回参数拉取支付页面
                //创建map对象
                Map<String, String> param = new HashMap<>();
                //appId
                param.put("appId", config.getAppID());
                //订单id
                param.put("package", "prepay_id="+response.get("prepay_id"));
                //随机字符串
                param.put("nonceStr", nonce_str);
                //时间戳
                param.put("timeStamp", System.currentTimeMillis() / 1000 + "");
                //加密方式
                param.put("signType","MD5");

//                param.put("sign", WXPayUtil.generateSignature(param, config.getKey(),
//                        WXPayConstants.SignType.MD5));

                //按照accs排列参数,进行md5加密
                String str = "appId="+config.getAppID()+"&nonceStr="+nonce_str+"&package=prepay_id="+response.get("prepay_id")+"&signType=MD5&timeStamp="+System.currentTimeMillis() / 1000 + ""+"&key="+config.getKey();
                param.put("sign", MD5Util.encode(str).toUpperCase());
                Map<String,Object> datas = new HashMap<>();
                if (tell.equals("0")){
                    datas.put("param",param);
                    datas.put("dataInfo","");
                }else if (tell.equals("1")){
                    datas.put("param",param);
                    datas.put("dataInfo",dataInfo);
                }
                return datas;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("下单失败");
        }
        throw new Exception("下单失败");
    }





    /**
     * 生成订单号
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


    /**
     * 收益展示数据
     */
    public Map<String,Object> earnings(Map<String,Object> data){
        //获取openId
        String openId = (String) data.get("openId");

        //创建map对象用于存放返回数据
        Map<String,Object> ear = new HashMap<>();

        //查询用户收益余额
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);
        Double balance = xcuser.getUser_balance();
        ear.put("balance",balance);

        //获取提现中的记录
        List<Cash> proposerCashs = cashMapper.selectCash(openId,0);
        if (proposerCashs.size() == 0){
            ear.put("proposer_cash",0.00);
        }else {
            Double money = 0.00;
            for (Cash proposerCash : proposerCashs) {
                money = money + proposerCash.getCash_quantity();
            }
            ear.put("proposer_cash",money);
        }


        //获取已经提现记录
        List<Cash> cashList = cashMapper.selectCash(openId,1);
        if (cashList.size()==0){
            ear.put("cash_out",0.00);
        }else {
            Double money = 0.00;
            for (Cash cash : cashList) {
                money = money + cash.getCash_quantity();
            }
            ear.put("cash_out",money);
        }
        return ear;
    }

    /**
     * 提现记录查询
     */
    public List<Map<String,Object>> moneyRecord(Map<String,Object> data){
        //获取openid
        String openId = (String) data.get("openId");
        //获取提现状态
        String starts = (String) data.get("start");
        Integer start = Integer.parseInt(starts);

        //创建数组用于返回数据
        List<Map<String,Object>> dataList = new ArrayList<>();
        //提取提现记录
        List<Cash> cashList = cashMapper.selectCash(openId,start);
        if (cashList.size() == 0){//当数据为空时
            return dataList;
        }else {
            for (Cash cash : cashList) {
                Map<String,Object> cashData = new HashMap<>();
                cashData.put("cashName",cash.getCash_name());
                cashData.put("cashTime",cash.getCash_time());
                cashData.put("Money",cash.getCash_quantity());


                if (start == 0){
                    cashData.put("start","提现中");
                }else if (start == 1){
                    cashData.put("start","已到账");
                }else if (start == 2){
                    cashData.put("start","已驳回");
                    String[] a = cash.getReason().split("\\[");
                    String[] b = a[1].split("\\]");
                    List<String> list1 = Arrays.asList(b[0].split(","));
                    cashData.put("reason",list1);
                }
                dataList.add(cashData);
            }
        }

        return dataList;
    }


    /**
     * 收益记录
     */
    public List<Map<String,Object>> earningsRecord(Map<String,Object> data){
        String openId = (String) data.get("openId");

        //查询收益记录
        List<VipOreder> earningsRecords = vipOredermapper.selectReadOpenId(openId);

        //创建数组用于返回数据
        List<Map<String,Object>> dataList = new ArrayList<>();

        if (earningsRecords.size() == 0){
            return dataList;
        }else {
            for (VipOreder earningsRecord : earningsRecords) {
                Map<String,Object> earnings = new HashMap<>();
                earnings.put("earningsName","推荐会员收益");
                earnings.put("earningsTime",earningsRecord.getOrder_time());
                earnings.put("Money",earningsRecord.getBrokerage());
                earnings.put("balance",earningsRecord.getRead_balance());
                dataList.add(earnings);
            }
        }
        return dataList;
    }

    /**
     * 转账到用户
     */
    public String remittance(Map<String,Object> data) throws Exception {
        String orderId = (String) data.get("orderId");
        Cash cash = cashMapper.selectOrderId(orderId);
        String openId = cash.getOpen_id();
        Integer moneys = cash.getCash_quantity().intValue()*100;
        String money = moneys.toString();


        if (cash.getStart() == 0){
            Map<String,String> parm = new HashMap<>();
            //随机字符串
            String nonce_str = WXPayUtil.generateNonceStr();
            //订单编号
            String partner_trade_no = getOrderId();
            //签名
            String str = "amount="+money+"&check_name=NO_CHECK&desc=收益提现&mch_appid="+WXConfigUtil.APP_ID+"&mchid="+WXConfigUtil.MCH_ID+"&nonce_str="+nonce_str+"&openid="+openId+"&partner_trade_no="+partner_trade_no+"&spbill_create_ip="+SPBILL_CREATE_IP+"&key="+WXConfigUtil.KEY;
            parm.put("sign",MD5Util.encode(str).toUpperCase());

            String parXml = "<xml>\n" +
                    "\n" +
                    "<mch_appid>"+WXConfigUtil.APP_ID+"</mch_appid>\n" +
                    "\n" +
                    "<mchid>"+WXConfigUtil.MCH_ID+"</mchid>\n" +
                    "\n" +
                    "<nonce_str>"+nonce_str+"</nonce_str>\n" +
                    "\n" +
                    "<partner_trade_no>"+partner_trade_no+"</partner_trade_no>\n" +
                    "\n" +
                    "<openid>"+openId+"</openid>\n" +
                    "\n" +
                    "<check_name>NO_CHECK</check_name>\n" +
                    "\n" +
                    "<amount>"+money+"</amount>\n" +
                    "\n" +
                    "<desc>收益提现</desc>\n" +
                    "\n" +
                    "<spbill_create_ip>"+SPBILL_CREATE_IP+"</spbill_create_ip>\n" +
                    "\n" +
                    "<sign>"+MD5Util.encode(str).toUpperCase()+"</sign>\n" +
                    "\n" +
                    "</xml>";

//        String a = HttpUtil.post(WeChatUtil.POST_TRANSFERS,parXml);
            String resXml = WXHttpCertUtils.doPost(WeChatUtil.POST_TRANSFERS,parXml);
            System.out.println(resXml);

            Map notifyMap = null;
            notifyMap = WXPayUtil.xmlToMap(resXml);
            String return_code = (String) notifyMap.get("return_code"); //状态
            if(return_code.equals("SUCCESS")){
                System.out.println("支付成功");
                cashMapper.updataOrderId(orderId);
                Xcuser xcuser = xcuserMapper.selectOpenId(openId);
                String string = "‼‼不能错过的消息\n" +
                        "\n❣亲爱的"+xcuser.getUser_name()+"，你有一笔提现到账。\n"+
                        "\n金额: "+cash.getCash_quantity();
                accessTokenUtil.staffTxt(string,xcuser.getOpen_id());
                return "支付成功";
            }
            return "转账失败";
        }
        return "已经转过账了";
        
    }

    /**
     * 发起提现
     */
    public String outMoney(Map<String,Object> data){
        String openId = (String) data.get("openId");
        String moneys = (String) data.get("money");
        Double money = Double.valueOf(moneys);

        String cashName = (String) data.get("cashName");

        //获取当前提现时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());
        //向数据库写入提现记录
        cashMapper.insertCash(getOrderId(),openId,cashName,money,time,0);
        //到user表中余额总数减去100
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);
        if (xcuser.getUser_balance() == 0.00){
            return "0";
        }else if (xcuser.getUser_balance() - money < 0){
            return "1";
        }
        Double userBalance = xcuser.getUser_balance() - money;
        xcuserMapper.updataBalance(openId,userBalance);
        return "发起提现成功";
    }

    public Map<String,Object> cashStart(Map<String,Object> data){
        String openId = (String) data.get("openId");
        //根据openId到对应关系表中查询
        List<ShineUpon> shineUpons = shineUponMapper.selectReadOpenId(openId);
        List<Map<String,Object>> datas = new ArrayList<>();
        if (shineUpons.size() != 0){
            for (ShineUpon shineUpon : shineUpons) {
                Map<String,Object> datainfo = new HashMap<>();
                //查询用户信息
                Xcuser xcuser1 = xcuserMapper.selectOpenId(shineUpon.getOpen_id());
                datainfo.put("userName",xcuser1.getUser_name());
                datainfo.put("userHeadimgurl",xcuser1.getUser_headimgurl());
                datas.add(datainfo);
            }
        }
        Map<String,Object> dataList = new HashMap<>();
        dataList.put("quantity",shineUpons.size());
        dataList.put("userInfo",datas);

        return dataList;

    }
}
