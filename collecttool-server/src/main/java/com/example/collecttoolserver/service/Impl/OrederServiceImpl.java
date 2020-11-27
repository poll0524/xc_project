package com.example.collecttoolserver.service.Impl;

import com.example.collecttoolserver.common.WeChatUtil;
import com.example.collecttoolserver.pojo.Xcuser;
import com.example.collecttoolserver.service.IOrderService;
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

    public static final String SPBILL_CREATE_IP = "127.0.0.1";
    //开通会员回调地址
    public static final String NOTIFY_URL = "https://poll.mynatapp.cc/datatool/order/notify";
//    public static final String NOTIFY_URL = "https://xiaocisw.site/datatool/order/notify";


    public static final String TRADE_TYPE_APP = "JSAPI";

    /**
     * 调起支付
     */
    @Override
    public Map doUnifiedOrder(Map<String, Object> dataplay) throws Exception {
        String openId = (String) dataplay.get("openId");
        Double price = Double.parseDouble(dataplay.get("price").toString())*100;
        if (price != 990.00 || price != 2490.00){
            return null;
        }

        String attach = "{openId"+openId+"}";

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

                String attach = notifyMap.get("attach");
                //string转json类型
                JSONObject jsonObject = new JSONObject(attach.toString());
                System.out.println(jsonObject.toString());
                //获取用户openId
                String openId = jsonObject.getString("openId");
                String beginTime = "";
                String endTime = "";
                if (return_code.equals("SUCCESS")) {
                    log.info("微信手机支付回调成功订单号:{}", out_trade_no);
                    xmlBack = "<xml>\n" + "<return_code><![CDATA[SUCCESS]]></return_code>\n" + "<return_msg><![CDATA[OK]]></return_msg>\n" + "</xml> ";

                    //查询用户信息
                    Xcuser xcuser = xcuserMapper.selectOpenId(openId);
                    Integer start = xcuser.getStart();
                    if (start == null){
                        //计算会员时间
                        if (total_fee.equals("990")){
                            //获取当前时间
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                            beginTime = df.format(new Date());
                            //结束时间
                            Calendar calendar2 = Calendar.getInstance();
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            calendar2.add(Calendar.MONTH, 1);
                            endTime = sdf2.format(calendar2.getTime());
                        }else if (total_fee.equals("2490")){
                            //获取当前时间
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                            beginTime = df.format(new Date());
                            //结束时间
                            Calendar calendar2 = Calendar.getInstance();
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            calendar2.add(Calendar.MONTH, 3);
                            endTime = sdf2.format(calendar2.getTime());
                        }
                        xcuserMapper.updataVip(openId,start,beginTime,endTime);
                    }else {
                        if (total_fee.equals("990")){
                            //会员到期时间
                            //获取当前时间
                            endTime = getLastMonth(xcuser.getEnd_time(),0,1,0);
                            xcuserMapper.updataVipGo(openId,start,endTime);
                        }else if (total_fee.equals("2490")){
                            //会员到期时间
                            //获取当前时间
                            endTime = getLastMonth(xcuser.getEnd_time(),0,3,0);
                            xcuserMapper.updataVipGo(openId,start,endTime);
                        }
                    }
                    Integer a = Integer.parseInt(total_fee)/100;
                    Double orderMoney = Double.valueOf(a);
                    //获取当前时间
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    String orderTime = df.format(new Date());
                    vipOrederMapper.insertOrder(openId,out_trade_no,orderMoney,orderTime,"买会员");
                }
            }
        } catch (Exception e) {
            log.error("手机支付回调通知失败", e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;

    }

    public static String getLastMonth(String dateStr,int addYear, int addMonth, int addDate) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date sourceDate = sdf.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sourceDate);
            cal.add(Calendar.YEAR,addYear);
            cal.add(Calendar.MONTH, addMonth);
            cal.add(Calendar.DATE, addDate);

            java.text.SimpleDateFormat returnSdf = new java.text.SimpleDateFormat("yyyy-MM");
            String dateTmp = returnSdf.format(cal.getTime());
            Date returnDate = returnSdf.parse(dateTmp);
            return dateTmp;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
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
}
