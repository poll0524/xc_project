package com.example.xcschoolserver.service.impl;

import com.example.xcschoolserver.common.WXConfigUtil;
import com.example.xcschoolserver.common.WXHttpCertUtils;
import com.example.xcschoolserver.common.WeChatUtil;
import com.example.xcschoolserver.mapper.adminUserMapper;
import com.example.xcschoolserver.mapper.cashMapper;
import com.example.xcschoolserver.mapper.reasonMapper;
import com.example.xcschoolserver.mapper.xcuserMapper;
import com.example.xcschoolserver.pojo.AdminUser;
import com.example.xcschoolserver.pojo.Cash;
import com.example.xcschoolserver.pojo.Reason;
import com.example.xcschoolserver.pojo.Xcuser;
import com.example.xcschoolserver.service.IOrderAdminService;
import com.example.xcschoolserver.util.AccessTokenUtil;
import com.example.xcschoolserver.util.MD5Util;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderAdminServiceIpml implements IOrderAdminService {
    @Autowired
    private cashMapper cashMapper;
    @Autowired
    private xcuserMapper xcuserMapper;
    @Autowired
    private reasonMapper reasonMapper;
    @Autowired
    private WxServiceImpl wxService;
    @Autowired
    private AccessTokenUtil accessTokenUtil;
    @Autowired
    private adminUserMapper adminUserMapper;

    /**
     * 管理后台查询提现记录
     * @param data
     * @return
     */
    public Map<String,Object> selectOrder(Map<String,Object> data){
        Integer start =Integer.parseInt((String) data.get("start"));
        Integer pag = Integer.parseInt((String) data.get("pag"))*10;

        Map<String,Object> datalist = new HashMap<>();
        List<Map<String,Object>> dataCash = new ArrayList<>();
        if(start == 0){//未处理的数据
            //到数据库查询提现列表
            List<Cash> cashs = cashMapper.selectCashAdmin(0,pag);
            List<Cash> cashList = cashMapper.selectCashAdmins(0);
            for (Cash cash : cashs) {
                Map<String,Object> ca = new HashMap<>();
                ca.put("cashId",cash.getCash_id());
                ca.put("orderId",cash.getOrder_id());
                ca.put("cashTime",cash.getCash_time());
                ca.put("cashType",cash.getCash_name());

                Xcuser xcuser = xcuserMapper.selectOpenId(cash.getOpen_id());
                if (xcuser == null){
                    ca.put("userName","用户名异常");
                }else {
                    ca.put("userName",xcuser.getUser_name());
                }
                ca.put("cashMoney",cash.getCash_quantity());
                //结果
                ca.put("result","");
                //理由
                ca.put("reason","");
                //操作时间
                ca.put("dispose_time","");
                dataCash.add(ca);
            }
            datalist.put("dataList",dataCash);
            datalist.put("dataQuantity",cashList.size());
        }else if(start == 1){//处理后的数据
            //到数据库查询提现列表
            List<Cash> cashs = cashMapper.selectCashAdminNo(0,pag);
            List<Cash> cashList = cashMapper.selectCashAdminNos(0);
            for (Cash cash : cashs) {
                Map<String,Object> ca = new HashMap<>();
                ca.put("cashId",cash.getCash_id());
                ca.put("orderId",cash.getOrder_id());
                ca.put("cashTime",cash.getCash_time());
                ca.put("cashType",cash.getCash_name());
                Xcuser xcuser = xcuserMapper.selectOpenId(cash.getOpen_id());
                if (xcuser == null){
                    ca.put("userName","用户名异常");
                }else {
                    ca.put("userName",xcuser.getUser_name());
                }
                ca.put("cashMoney",cash.getCash_quantity());
                List<String> a = new ArrayList<>();
                if (cash.getStart() == 1){
                    ca.put("result",true);
                    ca.put("reason",a);
                }else if (cash.getStart() == 2){
                    ca.put("result",false);
                    ca.put("reason",getList(cash.getReason()));
                }
                ca.put("dispose_time","");
                dataCash.add(ca);
            }
            datalist.put("dataList",dataCash);
            datalist.put("dataQuantity",cashList.size());
        }


        return datalist;
    }

    /**
     * 管理后台提现记录按照条件查询
     */
    public Map<String,Object> selectTimeOrder(Map<String,Object> data,String userToken){
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser == null){
            return null;
        }

        String time = (String) data.get("time");
        String name = (String) data.get("name");
        Integer start = Integer.parseInt((String) data.get("start"));
        Integer pag = Integer.parseInt((String) data.get("pag"));

        Map<String,Object> cashList = new HashMap<>();
        List<Map<String,Object>> dataCash = new ArrayList<>();

        if (!time.equals("") && !name.equals("")){//时间和名称都不为空
            time = "%"+time+"%";
            name = "%"+name+"%";
            List<Xcuser> xcusers = xcuserMapper.selectNameLike(name);
            List openids = new ArrayList();
            if (xcusers.size() != 0) {
                for (Xcuser xcuser : xcusers) {
                    openids.add(xcuser.getOpen_id());
                }
                if (start == 0){
                    List<Cash> cashs = cashMapper.selectCashAdminTimeName(time,openids,0,pag*10);
                    List<Cash> cashss = cashMapper.selectCashAdminTimeNames(time,openids,0);
                    if (cashs.size() != 0){
                        for (Cash cash : cashs) {
                            Map<String,Object> ca = new HashMap<>();
                            ca.put("cashId",cash.getCash_id());
                            ca.put("orderId",cash.getOrder_id());
                            ca.put("cashTime",cash.getCash_time());
                            ca.put("cashType",cash.getCash_name());
                            Xcuser xcuser = xcuserMapper.selectOpenId(cash.getOpen_id());
                            if (xcuser == null){
                                ca.put("userName","用户名异常");
                            }else {
                                ca.put("userName",xcuser.getUser_name());
                            }
                            ca.put("cashMoney",cash.getCash_quantity());
                            ca.put("dispose_time","");
                            //结果
                            ca.put("result","");
                            //理由
                            ca.put("reason","");
                            //操作时间
                            ca.put("dispose_time","");
                            dataCash.add(ca);
                        }
                        cashList.put("dataList",dataCash);
                        cashList.put("dataQuantity",cashss.size());
                    }
                }else if (start == 1){
                    List<Cash> cashs = cashMapper.selectCashAdminTimeNameNo(time,openids,0,pag*10);
                    List<Cash> cashss = cashMapper.selectCashAdminTimeNameNos(time,openids,0);
                    if (cashs.size() != 0){
                        for (Cash cash : cashs) {
                            Map<String,Object> ca = new HashMap<>();
                            ca.put("cashId",cash.getCash_id());
                            ca.put("orderId",cash.getOrder_id());
                            ca.put("cashTime",cash.getCash_time());
                            ca.put("cashType",cash.getCash_name());
                            Xcuser xcuser = xcuserMapper.selectOpenId(cash.getOpen_id());
                            if (xcuser == null){
                                ca.put("userName","用户名异常");
                            }else {
                                ca.put("userName",xcuser.getUser_name());
                            }
                            ca.put("cashMoney",cash.getCash_quantity());
                            ca.put("dispose_time","");
                            if (cash.getStart() == 1){
                                ca.put("result",true);
                                ca.put("reason","");
                            }else if (cash.getStart() == 2){
                                ca.put("result",false);
                                ca.put("reason",getList(cash.getReason()));
                            }
                            ca.put("dispose_time","");
                            dataCash.add(ca);
                        }
                        cashList.put("dataList",dataCash);
                        cashList.put("dataQuantity",cashss.size());
                    }
                }

            }

        }else if (!time.equals("") && name.equals("")){
            time = "%"+time+"%";
            if (start == 0){
                List<Cash> cashs = cashMapper.selectCashAdminTime(time,0,pag*10);
                List<Cash> cashss = cashMapper.selectCashAdminTimes(time,0);
                if (cashs.size() != 0){
                    for (Cash cash : cashs) {
                        Map<String,Object> ca = new HashMap<>();
                        ca.put("cashId",cash.getCash_id());
                        ca.put("orderId",cash.getOrder_id());
                        ca.put("cashTime",cash.getCash_time());
                        ca.put("cashType",cash.getCash_name());
                        Xcuser xcuser = xcuserMapper.selectOpenId(cash.getOpen_id());
                        if (xcuser == null){
                            ca.put("userName","用户名异常");
                        }else {
                            ca.put("userName",xcuser.getUser_name());
                        }
                        ca.put("cashMoney",cash.getCash_quantity());
                        ca.put("dispose_time","");
                        //结果
                        ca.put("result","");
                        //理由
                        ca.put("reason","");
                        //操作时间
                        ca.put("dispose_time","");
                        dataCash.add(ca);
                    }
                    cashList.put("dataList",dataCash);
                    cashList.put("dataQuantity",cashss.size());
                }
            }else if (start == 1){
                List<Cash> cashs = cashMapper.selectCashAdminTimeNo(time,0,pag*10);
                List<Cash> cashss = cashMapper.selectCashAdminTimeNos(time,0);
                if (cashs.size() != 0){
                    for (Cash cash : cashs) {
                        Map<String,Object> ca = new HashMap<>();
                        ca.put("cashId",cash.getCash_id());
                        ca.put("orderId",cash.getOrder_id());
                        ca.put("cashTime",cash.getCash_time());
                        ca.put("cashType",cash.getCash_name());
                        Xcuser xcuser = xcuserMapper.selectOpenId(cash.getOpen_id());
                        if (xcuser == null){
                            ca.put("userName","用户名异常");
                        }else {
                            ca.put("userName",xcuser.getUser_name());
                        }
                        ca.put("cashMoney",cash.getCash_quantity());
                        ca.put("dispose_time","");
                        if (cash.getStart() == 1){
                            ca.put("result",true);
                            ca.put("reason","");
                        }else if (cash.getStart() == 2){
                            ca.put("result",false);
                            ca.put("reason",getList(cash.getReason()));
                        }
                        ca.put("dispose_time","");
                        dataCash.add(ca);
                    }
                    cashList.put("dataList",dataCash);
                    cashList.put("dataQuantity",cashss.size());
                }
            }

        }else if (time.equals("") && !name.equals("")){
            name = "%"+name+"%";
            List<Xcuser> xcusers = xcuserMapper.selectNameLike(name);
            List openids = new ArrayList();
            if (xcusers.size() != 0){
                for (Xcuser xcuser : xcusers) {
                    openids.add(xcuser.getOpen_id());
                }
                if (start == 0){
                    List<Cash> cashs = cashMapper.selectCashOpenIdA(openids,0,pag*10);
                    List<Cash> cashss = cashMapper.selectCashOpenIdAs(openids,0);
                    if (cashs.size() != 0){
                        for (Cash cash : cashs) {
                            Map<String,Object> ca = new HashMap<>();
                            ca.put("cashId",cash.getCash_id());
                            ca.put("orderId",cash.getOrder_id());
                            ca.put("cashTime",cash.getCash_time());
                            ca.put("cashType",cash.getCash_name());
                            ca.put("userName",xcuserMapper.selectOpenId(cash.getOpen_id()).getUser_name());
                            ca.put("cashMoney",cash.getCash_quantity());
                            //结果
                            ca.put("result","");
                            //理由
                            ca.put("reason","");
                            //操作时间
                            ca.put("dispose_time","");
                            dataCash.add(ca);
                        }
                    }

                    cashList.put("dataList",dataCash);
                    cashList.put("dataQuantity",cashss.size());
                }else if (start == 1){
                    for (Xcuser xcuser : xcusers) {
                        openids.add(xcuser.getOpen_id());
                    }
                    List<Cash> cashss = cashMapper.selectCashOpenIdNos(openids,0);
                    List<Cash> cashs = cashMapper.selectCashOpenIdNo(openids,0,pag);
                    if (cashs.size() != 0){
                        for (Cash cash : cashs) {
                            Map<String,Object> ca = new HashMap<>();
                            ca.put("cashId",cash.getCash_id());
                            ca.put("orderId",cash.getOrder_id());
                            ca.put("cashTime",cash.getCash_time());
                            ca.put("cashType",cash.getCash_name());
                            ca.put("userName",xcuserMapper.selectOpenId(cash.getOpen_id()).getUser_name());
                            ca.put("cashMoney",cash.getCash_quantity());
                            if (cash.getStart() == 1){
                                ca.put("result",true);
                                ca.put("reason","");
                            }else if (cash.getStart() == 2){
                                ca.put("result",false);
                                ca.put("reason",getList(cash.getReason()));
                            }
                            ca.put("dispose_time","");
                            dataCash.add(ca);
                        }
                        cashList.put("dataList",dataCash);
                        cashList.put("dataQuantity",cashss.size());
                    }
                }


            }
        }else {
            return null;
        }
        return cashList;
    }


    /**
     * 驳回提现记录理由查询
     */
    public List<Reason> selectReason(){
        List<Reason> reasons = reasonMapper.selectReason(1);

        return reasons;
    }

    /**
     * 驳回提现记录
     */
    public String updataCash(Map<String,Object> data,String userToken){
        AdminUser adminUser = adminUserMapper.selectToken(userToken);
        if (adminUser == null){
            return null;
        }

        Integer cashId = Integer.parseInt ((String) data.get("cashId"));
        List<Integer> reasons = (List<Integer>) data.get("reason");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());
        List<String> b = new ArrayList<>();
        for (Integer reason : reasons) {
            b.add(reasonMapper.selectId(reason).getReason_text());
        }

        Integer a = cashMapper.updataCash(cashId,2,b.toString(),time);

        if (a != 0){
            return "修改成功";
        }
        return "修改失败";
    }


    /**
     * 提现记录通过发起转账
     */
    public String outMoney(Map<String,Object> data) throws Exception {
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
            String partner_trade_no = wxService.getOrderId();
            //签名
            String str = "amount="+money+"&check_name=NO_CHECK&desc=收益提现&mch_appid="+WXConfigUtil.APP_ID+"&mchid="+WXConfigUtil.MCH_ID+"&nonce_str="+nonce_str+"&openid="+openId+"&partner_trade_no="+partner_trade_no+"&spbill_create_ip="+wxService.SPBILL_CREATE_IP+"&key="+WXConfigUtil.KEY;
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
                    "<spbill_create_ip>"+wxService.SPBILL_CREATE_IP+"</spbill_create_ip>\n" +
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
     * string转list 方法
     */
    public List<String> getList(String str){
        String[] q = str.split("\\[");
        String[] w = q[1].split("\\]");
        List<String> list = Arrays.asList(w[0].split(","));
        return list;
    }
}
