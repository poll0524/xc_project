package com.example.collecttoolserver.service.Impl;

import com.example.collecttoolserver.common.ReturnVO;
import com.example.collecttoolserver.common.WXHttpCertUtils;
import com.example.collecttoolserver.common.WeChatUtil;
import com.example.collecttoolserver.pojo.Xcuser;
import com.example.collecttoolserver.service.IOrderService;
import com.example.collecttoolserver.util.HierarchyUtil;
import com.example.collecttoolserver.util.MD5Util;
import com.example.collecttoolserver.util.WXConfigUtil;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.collecttoolserver.mapper.*;
import com.example.collecttoolserver.util.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class OrederServiceImpl implements IOrderService {
    @Autowired
    private xcuserMapper xcuserMapper;
    @Autowired
    private vipOrederMapper vipOrederMapper;
    @Autowired
    private HierarchyUtil hierarchyUtil;
    @Autowired
    private cashMapper cashMapper;

    public static final String SPBILL_CREATE_IP = "127.0.0.1";
    //开通会员回调地址
//    public static final String NOTIFY_URL = "https://poll.mynatapp.cc/collecttool/order/notify";
    public static final String NOTIFY_URL = "https://xiaocisw.site/collecttool/order/notify";


    public static final String TRADE_TYPE_APP = "JSAPI";

    /**
     * 调起支付
     */
    @Override
    public Map doUnifiedOrder(Map<String, Object> dataplay) throws Exception {
        //上级openId
        String oneOpenId = (String) dataplay.get("oneOpenId");
        //下级openId也就是本人openId
        String twoOpenId = (String) dataplay.get("twoOpenId");
        //会员等级
        Integer start = Integer.parseInt((String) dataplay.get("start"));
        Double price = Double.parseDouble(dataplay.get("price").toString())*100;
        if (price != 990.0 && price != 2490.0 && price != 9900.0 && price != 99900.0 && price != 999900.0){
            return null;
        }
        //计算价格
        Double total_fee = hierarchyUtil.vipMoney(twoOpenId,start,price);

        String attach = "";

        if (oneOpenId.equals(twoOpenId)){
            attach = "{twoOpenId:"+twoOpenId+",oneOpenId:"+oneOpenId+",start:"+start+"}";
        }

        try {
            WXConfigUtil config = new WXConfigUtil();
            WXPay wxpay = new WXPay(config);
            Map<String,String> data = new HashMap<>();
            //生成商户订单号，不可重复
            data.put("appid", config.getAppID());
            //商户号
            data.put("mch_id", config.getMchID());
            //随机字符串
            String nonce_str = WXPayUtil.generateNonceStr();
            data.put("nonce_str", nonce_str);
            //订单名称
            data.put("body","开通会员");
            //订单编号
            String orderId = getOrderId();
            data.put("out_trade_no", orderId);
            //价格
            data.put("total_fee", String.valueOf(new Double(total_fee).intValue()));
//            data.put("total_fee","1");
            //用户openId
            data.put("openid", twoOpenId);
            //服务器ip地址
            data.put("spbill_create_ip", SPBILL_CREATE_IP);
            //异步通知地址（请注意必须是外网）,回调地址
            data.put("notify_url", NOTIFY_URL);
            //交易类型
            data.put("trade_type", TRADE_TYPE_APP);
            //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
            data.put("attach", attach);
            //sign签名,md5加密
            String sign = WXPayUtil.generateSignature(data, config.getKey(), WXPayConstants.SignType.MD5);
            data.put("sign", sign);

            //使用官方API请求预付订单
            Map<String, String> response = wxpay.unifiedOrder(data);

            System.out.println(response);

            if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数
                //返回参数拉取支付页面
                //创建map对象
                Map<String, String> param = new HashMap<>();
                //appId
                param.put("appId", config.getAppID());
                //订单id
                param.put("package", "prepay_id=" + response.get("prepay_id"));
                //随机字符串
                param.put("nonceStr", nonce_str);
                //时间戳
                param.put("timeStamp", System.currentTimeMillis() / 1000 + "");
                //加密方式
                param.put("signType", "MD5");

                //按照accs排列参数,进行md5加密
                String str = "appId=" + config.getAppID() + "&nonceStr=" + nonce_str + "&package=prepay_id=" + response.get("prepay_id") + "&signType=MD5&timeStamp=" + System.currentTimeMillis() / 1000 + "" + "&key=" + config.getKey();
                param.put("sign", MD5Util.encode(str).toUpperCase());

                return param;
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception("下单失败");
        }
        throw new Exception("下单失败");
    }

    /**
     * 回调支付结果
     */
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
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//商户订单号
                //获取支付金额
                String total_fee = notifyMap.get("total_fee");
//                String total_fee = "299900";//商户订单号

                String attach = notifyMap.get("attach");
                //string转json类型
                JSONObject jsonObject = new JSONObject(attach.toString());
                System.out.println(jsonObject.toString());
                //获取用户openId
                String oneOpenId = jsonObject.getString("oneOpenId");
                String twoOpenId = jsonObject.getString("twoOpenId");
                Integer start = Integer.parseInt(jsonObject.getString("start"));
                String beginTime = "";
                String endTime = "";
                if (return_code.equals("SUCCESS")) {
                    log.info("微信手机支付回调成功订单号:{}", out_trade_no);
                    xmlBack = "<xml>\n" + "<return_code><![CDATA[SUCCESS]]></return_code>\n" + "<return_msg><![CDATA[OK]]></return_msg>\n" + "</xml> ";

                    Integer a = Integer.parseInt(total_fee);
                    Double orderMoney = Double.valueOf(a);
                    //计算一级分销
                    hierarchyUtil.brokerage(oneOpenId,twoOpenId,orderMoney);

                    if (start == 1){
                        //获取当前时间
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        beginTime = df.format(new Date());
                        //结束时间
                        Calendar calendar2 = Calendar.getInstance();
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        calendar2.add(Calendar.MONTH, 1);
                        endTime = sdf2.format(calendar2.getTime());
                    }else if (start == 2){
                        //获取当前时间
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        beginTime = df.format(new Date());
                        //结束时间
                        Calendar calendar2 = Calendar.getInstance();
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        calendar2.add(Calendar.MONTH, 3);
                        endTime = sdf2.format(calendar2.getTime());
                    }else if (start == 3){
                        //获取当前时间
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        beginTime = df.format(new Date());
                        //结束时间
                        Calendar calendar2 = Calendar.getInstance();
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        calendar2.add(Calendar.YEAR, 1);
                        endTime = sdf2.format(calendar2.getTime());
                    }else if (start == 4){
                        //获取当前时间
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        beginTime = df.format(new Date());
                        //结束时间
                        endTime = "0";
                    }else if (start == 5){
                        //获取当前时间
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        beginTime = df.format(new Date());
                        //结束时间
                        endTime = "0";
                    }

                    //更新会员时间
                    xcuserMapper.updataVip(twoOpenId,start,beginTime,endTime);
                }
            }
        } catch (Exception e) {
            log.error("手机支付回调通知失败", e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;

    }

    /**
     * 发起提现
     */
    public String outMoney(Map<String,Object> data) throws Exception {
        String openId = (String) data.get("openId");
        String moneys = (String) data.get("money");
        Double money = Double.valueOf(moneys);

        String cashName = (String) data.get("cashName");

        //查询余额
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);

        //余额
        Double balance = xcuser.getBalance();

        //判断余额是否能够提现
        if (balance - money < 0){
            return "余额不足";
        }

        //获取当前提现时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());
        //向数据库写入提现记录
        cashMapper.insertCash(getOrderId(),openId,cashName,money,time);

        remittance(money,openId);
        return "发起提现成功";
    }


    /**
     * 转账到用户
     */
    public String remittance(Double moneys,String openId) throws Exception {
        Double moneyss = moneys*100;
        moneyss = moneyss - moneyss * 0.06;
        Integer money = moneyss.intValue();
        ;

        System.out.println(money);

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
                "<amount>"+money.toString()+"</amount>\n" +
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
            //更新用户余额
            xcuserMapper.updataBalances(openId,moneys);
            return "支付成功";
        }
        return "转账失败";
    }

    /**
     * 余额查询
     */
    public Double selectBalance(Map<String,Object> data){
        Xcuser xcuser = xcuserMapper.selectOpenId((String) data.get("openId"));
        return xcuser.getBalance();
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

    public static void main(String[] args){
        String a = "collecttool/logo/2.jpg";
        List b = new ArrayList();
        b.add(a);
        String c = AliyunOsskeyUtil.deleteObjects(WeChatUtil.BUCKETNAME,b);
        System.out.println(c);
    }
}
