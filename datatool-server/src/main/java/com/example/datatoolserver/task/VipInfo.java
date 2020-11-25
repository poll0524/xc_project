package com.example.datatoolserver.task;

import com.example.datatoolserver.mapper.shineUponMapper;
import com.example.datatoolserver.mapper.vipOredermapper;
import com.example.datatoolserver.mapper.xcuserMapper;
import com.example.datatoolserver.pojo.ShineUpon;
import com.example.datatoolserver.pojo.Xcuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class VipInfo {
    @Autowired
    private xcuserMapper xcuserMapper;
    @Autowired
    private vipOredermapper vipOredermapper;
    @Autowired
    private shineUponMapper shineUponMapper;

    /**
     * 计算会员有效时期,并更改会员等级
     */
    public void userVip(String openId,String orderId,String vipType){
        //支付成功
        //计算会员有效天数
        //获取vip等级
        Integer day = 0;
        if (vipType.equals("1")){
            day = 7;
        }else if (vipType.equals("2")){
            day = 365;
        }else if (vipType.equals("3")){
            day = 365*3;
        }
        System.out.println("会员有效时间:"+day);
        //获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String beginTime = df.format(new Date());


        //计算会员结束日期
        String three_days_after = "";
        if (day != 0){
            Calendar calendar2 = Calendar.getInstance();
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            calendar2.add(Calendar.DATE, day);
            three_days_after = sdf2.format(calendar2.getTime());
        }else {
            three_days_after = "0";
        }

        //根据回调信息到数据库更改用户数据
        if (vipType.equals("4")){
            xcuserMapper.updataUserVip(beginTime,three_days_after,orderId,openId,Integer.parseInt(vipType),1);
        }else {
            xcuserMapper.updataUserVip(beginTime,three_days_after,orderId,openId,Integer.parseInt(vipType),0);
        }

    }


    /**
     * 编写二级分销订单
     */
    public String shareOrder(String openId,String readOpenId,String total_fee,String out_trade_no,String start,String type){
        //计算金额
        Double orderMoney = Double.valueOf(total_fee)/100;
        //获取当前时间
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        Double readbalance = 0.00;
        if (!readOpenId.equals("来找客")){
            readbalance = xcuserMapper.selectOpenId(readOpenId).getUser_balance();
        }

        //判断支付类型
        String payType = "";
        if (type.equals("0")){
            payType = "开通会员";
        }else if (type.equals("1")){
            payType = "购买品牌";
        }else if (type.equals("2")){
            payType = "购买课程";
        }else if (type.equals("3")){
            payType = "修改品牌名";
        }else if (type.equals("4")){
            payType = "删除品牌";
        }

        //判断是否用户自己开通
        if (start.equals("0")){//如果用户自己在平台开通
            //判断openid是否已经存在上级
            //查询数据库是否存在
            ShineUpon shineUpon = shineUponMapper.selectOpenId(openId);
            //shineUpon为null说明该用户属于第一次支付
            if (shineUpon == null){// 用户第一次开通
                if (readOpenId.equals("来找客")){//自己在平台开通
                    //写入一条对应关系记录
                    shineUponMapper.insertShine(openId,readOpenId);
                    //将支付记录写入表中
                    vipOredermapper.insetVipOrder(openId,readOpenId,out_trade_no,orderMoney,0.00,time,payType,0.00);
                }else {//用户第一次在平台开通并推荐人不是来找客
                    return "推荐人错误";
                }
            }else if (shineUpon.getRead_open_id().equals(readOpenId) && readOpenId.equals("来找客")){//用户第二次开通推荐人和数据库匹配一致
                //将支付记录写入表中
                vipOredermapper.insetVipOrder(openId,readOpenId,out_trade_no,orderMoney,0.00,time,payType,0.00);
            }else if (!shineUpon.getRead_open_id().equals(readOpenId) && readOpenId.equals("来找客")){//用户第二次自己支付,但是数据有推荐人
                //调用二级分销计算方法
                readOrder(openId,readOpenId,orderMoney,out_trade_no,time,payType,readbalance);
            }
            else{
                return "推荐人错误";
            }
        }else if(start.equals("1")){//用户通过分享开通
            //判断openid是否已经存在上级
            //查询数据库是否存在
            ShineUpon shineUpon = shineUponMapper.selectOpenId(openId);
            //判断用户是否第一次开通
            if (shineUpon == null){//如果用户第一次开通
                if (!readOpenId.equals("来找客")){//用户第一次分享开通,并推荐人不是来找客
                    //写入一条对应关系记录
                    shineUponMapper.insertShine(openId,readOpenId);
//                    //将支付记录写入表中
//                    vipOredermapper.insetVipOrder(openId,readOpenId,out_trade_no,orderMoney,0.00,time);
                    readOrder(openId,readOpenId,orderMoney,out_trade_no,time,payType,readbalance);

                }else {//用户通过分享第一次开通且推荐人为来找客
                    return "推荐人错误";
                }
            }else if (shineUpon.getRead_open_id().equals(readOpenId)){//用户通过分享第二次开通,且推荐人与数据库一次
                //调用二级分销计算方法
                readOrder(openId,readOpenId,orderMoney,out_trade_no,time,payType,readbalance);
            }else if (!shineUpon.getRead_open_id().equals(readOpenId)){//用户通过分享第二次开通,且推荐人月数据库不一致

                readOrder(openId,shineUpon.getRead_open_id(),orderMoney,out_trade_no,time,payType,readbalance);
            }
            else {
                return "推荐人错误";
            }
        }

        return "成功";
    }

    /**
     * 二级分销收益算法
     */
    public void readOrder(String openId,String readOpenId,Double orderMoney,String out_trade_no,String time,String payType,Double readbalance){
        //获取开通人用户信息
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);
        //获取推荐人用户信息
        Xcuser readUser = xcuserMapper.selectOpenId(readOpenId);
        //是否有间推
        ShineUpon shineUpon = shineUponMapper.selectOpenId(readOpenId);
        System.out.println(shineUpon.getRead_open_id());
        if (xcuser != null){
            //计算佣金
            Double brokerage = 0.00;
            Double oneBrokerage = 0.00;
            //计算上一级的佣金
            if (orderMoney == 1){
                vipOredermapper.insetVipOrder(openId,readOpenId,out_trade_no,orderMoney,orderMoney,time,payType,orderMoney+readbalance);
                if (!readOpenId.equals("来找客")){
                    xcuserMapper.updataUserBalance(readOpenId,orderMoney);
                }
            }else {
                if (readUser.getStart()==2){//一年尊享会员佣金
                    brokerage = orderMoney*0.14;
                }else if(readUser.getStart()==3){//三年尊享会员佣金
                    brokerage = orderMoney*0.18;
                }else if (readUser.getStart()==4){//品牌尊享会员佣金
                    brokerage = orderMoney*0.22;
                }else if (readUser.getStart() == 5){
                    brokerage = orderMoney*0.26;
                }

                if(shineUpon.getRead_open_id().equals("来找客")){//如果一级为来找客说明没有推荐人
//                System.out.println("1111111111");
                    //将记录写入收益记录表
                    vipOredermapper.insetVipOrder(openId,readOpenId,out_trade_no,orderMoney,brokerage,time,payType,readbalance+brokerage);

                    //将金额跟新到用户余额
                    xcuserMapper.updataUserBalance(readOpenId,brokerage);
                }else if (!shineUpon.getRead_open_id().equals("来找客")){//如果一级推荐人不是来找客
                    Xcuser oneUser = xcuserMapper.selectOpenId(shineUpon.getRead_open_id());

                    if (oneUser != null && oneUser.getStart() > 1){//判断一级推荐人是否平台用户

                        if(oneUser.getStart() == 2){
                            oneBrokerage = orderMoney*0.16;
                        }else if (oneUser.getStart() == 3){
                            oneBrokerage = orderMoney*0.22;
                        }else if (oneUser.getStart() == 4){
                            oneBrokerage = orderMoney*0.28;
                        }else if (oneUser.getStart() == 5){
                            oneBrokerage = orderMoney*0.34;
                        }

                        //将一级推荐人收益记录写入收益记录表
                        vipOredermapper.insetVipOrder(readOpenId,oneUser.getOpen_id(),out_trade_no,orderMoney,oneBrokerage,time,payType,readbalance+brokerage);

                        //将一级推荐人佣金跟新到余额
                        xcuserMapper.updataUserBalance(oneUser.getOpen_id(),oneBrokerage);

                        //将二级推荐人收益记录写入收益记录表
                        vipOredermapper.insetVipOrder(openId,readOpenId,out_trade_no,orderMoney,brokerage,time,payType,readbalance+brokerage);

                        xcuserMapper.updataUserBalance(readOpenId,brokerage);
                    }
                }
            }
        }
    }

    /**
     * 计算vip等级差价
     */
    public Integer priceMoney(Integer getStart ,Integer userStart){
        Integer price = 0;
        if (getStart == 1){
            if (userStart == 2){
                price = 298;
            }else if (userStart == 3){
                price = 687;
            }else if (userStart == 4){
                price = 2998;
            }
        }else if (getStart == 2){
            if (userStart == 3){
                price = 688 - 299;
            }else if (userStart == 4){
                price = 2999 - 299;
            }
        }else if (getStart == 3){
            if (userStart == 4){
                price = 2999 - 688;
            }
        }
        return price*100;
    }


    /**
     * 判断对应关系
     */
    public Map<String,Object> shineUpon(String openId,String readOpenId){
        //支付人的openid
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);
        //分享人的openid
        Xcuser readXcUser = xcuserMapper.selectOpenId(readOpenId);

        //查询是否有分享人为一级的记录
        ShineUpon shineUpons = shineUponMapper.selectOpenId(readOpenId);
        if (shineUpons == null && !readOpenId.equals("来找客")){//如果没有
            //为分享人写入一条上级为来找客的记录
            shineUponMapper.insertShine(readOpenId,"来找客");
        }

        Map<String,Object> datainfo = new HashMap<>();
        //start为0时自己直接支付,start为1是为上下级开通
        //如果分享人openid为来找客时,改用户为自己直接支付
        //查询支付人是否存在对应关系
        ShineUpon shineUpon = shineUponMapper.selectOpenId(openId);
        if (shineUpon != null){
            if(readOpenId.equals("来找客")){
                datainfo.put("start","0");
                return datainfo;
            }
        }

        //当支付人与分销人都不为空时
        if (xcuser != null && readXcUser != null){
            //如果支付人已经存在支付关系
            if (shineUpon != null){
                //当支付人的存在关系为来找客时
                if (shineUpon.getRead_open_id().equals("来找客") && readOpenId.equals(shineUpon.getRead_open_id())){
                    //当前为自己直接支付
                    datainfo.put("start","0");
                }else if (shineUpon.getRead_open_id().equals("来找客") && !readOpenId.equals(shineUpon.getRead_open_id())){//当支付人的上级为来找客,而本次支付传入的openId为分享人是,更新该条记录,将来找客改问分享人
                    Xcuser xcuser1 = xcuserMapper.selectOpenId(shineUpon.getRead_open_id());
                    if(xcuser1 != null){
                        datainfo.put("name",xcuser1.getUser_name());
                        datainfo.put("readOpenId",readOpenId);
                        datainfo.put("start","1");
                        //更新该条记录
                        shineUponMapper.updataShine(shineUpon.getShine_id(),openId,readOpenId);
                    }else {
                        System.out.println("该分享人openId有问题!!!");
                    }

                } else {//支付人存在支付关系
                    datainfo.put("name",xcuserMapper.selectOpenId(shineUpon.getRead_open_id()).getUser_name());
                    datainfo.put("readOpenId",shineUpon.getRead_open_id());
                    datainfo.put("start","1");
                }
            }else {//如果支付在数据没有关系记录是
                if (openId.equals(readOpenId) || readOpenId.equals("来找客")){
                    datainfo.put("start","0");
                    shineUponMapper.insertShine(openId,"来找客");
                }else {
                    datainfo.put("name",xcuserMapper.selectOpenId(readOpenId).getUser_name());
                    datainfo.put("readOpenId",readOpenId);
                    datainfo.put("start","1");
                    shineUponMapper.insertShine(openId,readOpenId);
                }
            }
        }else if (shineUpons == null && shineUpon == null){
            datainfo.put("start","0");
            shineUponMapper.insertShine(openId,"来找客");
        }
//        System.out.println(datainfo.toString());
        return datainfo;
    }
}
