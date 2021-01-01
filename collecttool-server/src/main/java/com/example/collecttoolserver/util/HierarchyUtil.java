package com.example.collecttoolserver.util;

import com.example.collecttoolserver.common.WeChatUtil;
import com.example.collecttoolserver.mapper.*;
import com.example.collecttoolserver.pojo.ShineUpon;
import com.example.collecttoolserver.pojo.Xcuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
@Component
public class HierarchyUtil {
    @Autowired
    private shineUponMapper shineUponMapper;
    @Autowired
    private vipOrederMapper vipOrederMapper;
    @Autowired
    private xcuserMapper xcuserMapper;

    public void brokerage(String oneOpenId,String twoOpenId,Double orderMoney){
        //查询是否存在关系
        ShineUpon shineUpon = shineUponMapper.selectTowOpenId(twoOpenId);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String orderTime = df.format(new Date());
        //判断是否存在关系
        if (shineUpon != null){//该用户不存在上下级关系时
            if (shineUpon.getOne_open_id().equals(WeChatUtil.NAME)){
                vipOrederMapper.insertOrder(twoOpenId,getOrderId(),orderMoney/100,orderTime,"买会员", WeChatUtil.NAME,0.00);
            }else {
                System.out.println("1111111111111111");
                //判断上级是否是会员
                Xcuser xcuser = xcuserMapper.selectOpenId(shineUpon.getOne_open_id());
                Integer userStart = xcuser.getStart();
                if (userStart == null){
                    userStart = 0;
                }
                if (userStart > 0){//上级为会员时
                    //计算佣金
                    Double brokerageMoney = brokerageMoney(userStart,orderMoney/100);
                    //将佣金写入表单
                    vipOrederMapper.insertOrder(twoOpenId,getOrderId(),orderMoney/100,orderTime,"买会员",shineUpon.getOne_open_id(),brokerageMoney);
                    //将余额更新到上级中
                    xcuserMapper.updataBalance(shineUpon.getOne_open_id(),brokerageMoney);
                    System.out.println("222222222222");
                }else {//上级不是会员时
                    ShineUpon shineUpons = shineUponMapper.selectTowOpenId(shineUpon.getOne_open_id());
                    if (shineUpons == null){
                        //写入表单
                        vipOrederMapper.insertOrder(twoOpenId,getOrderId(),orderMoney/100,orderTime,"买会员", WeChatUtil.NAME,0.00);
                    }else {
                        String topOpenId = shineUpons.getOne_open_id();
                        //向上查找寻找会员
                        while (true){
                            System.out.println("333333333333");
                            Xcuser xcuser1 = xcuserMapper.selectOpenId(topOpenId);
                            Integer userStarts = xcuser1.getStart();
                            if (userStarts == null){
                                userStarts = 0;
                            }
                            if (userStarts > 0){
                                System.out.println("44444444444444444444");
                                //计算佣金
                                Double brokerageMoney = brokerageMoney(userStarts,orderMoney);
                                //将佣金写入表单
                                vipOrederMapper.insertOrder(twoOpenId,getOrderId(),orderMoney/100,orderTime,"买会员",topOpenId,brokerageMoney);
                                xcuserMapper.updataBalance(topOpenId,brokerageMoney);
                                break;
                            }else {
                                ShineUpon shineUpon1 = shineUponMapper.selectTowOpenId(topOpenId);
                                topOpenId = shineUpon1.getOne_open_id();
                            }
                        }
                    }
                }

            }


        }
    }




    public static void main(String[] args){
        String a = "2017-12-01 12:56:12"; // 时间字符串
        String b = "2017-12-31";

        String[] c = a.split("\\s+");
        System.out.println(c[0]);
//        Long between_dayInteger = between_days(a, b);
//
//        System.out.println(between_dayInteger);
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
     * 计算金额
     */
    public Double vipMoney(String openId,Integer start ,Double money){
        //查询当前用户等级
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);


        if (xcuser == null){
            return null;
        }
        Integer userStart = xcuser.getStart();

        if (userStart == null || userStart == 0){//新用户没有开通过会员时的价格
            if (start == 1){//9.9的价格
                return 990.0;
            }else if (start == 2){//24.9的价格
                return 2490.0;
            }else if (start == 3){//99的价格
                return 9900.0;
            }else if (start == 4){//999的价格
                return 99900.0;
            }else if (start == 5){//9999的价格
                return 999900.0;
            }
        }
        //计算已经使用了多少天及价格
        //开始时间
        String[] beginTime = xcuser.getBegin_time().split("\\s+");
        //当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String endTime = df.format(new Date());
        Long between_dayInteger = between_days(beginTime[0], endTime);
        //天数
        int day = between_dayInteger.intValue();
        //结算每一天多少钱
        Double dayMoney = 0.0;
        //一共给了多少钱?
        Double price = 0.0;
        if (userStart == 1){
            price = 990.0;
            dayMoney = 990.0/30;
        }else if (userStart == 2){
            price = 2490.0;
            dayMoney = 2490.0/30;
        }else if (userStart == 3){
            price = 9900.0;
            dayMoney = 9900.0/30;
        }else if (userStart == 4){
            price = 99900.0;
            dayMoney = 99900.0/30;
        }else if (userStart == 5){
            price = 999900.0;
            dayMoney = 999900.0/30;
        }
        //一共消耗多少钱
        Double dayMoneySum = 0.0;
        if (day == 0){
            dayMoneySum = price;
        }else {
            dayMoneySum = price - dayMoney * day;

        }
        //差价金额
        Double supplement = money - dayMoneySum;

        return supplement;
    }

    /**
     * 计算佣金
     */
    public Double brokerageMoney(Integer start,Double orderMoney){
        Double money = 0.0;
        if (start == 1){
            money = orderMoney*0.1;
        }else if (start == 2){
            money = orderMoney*0.2;
        }else if(start == 3){
            money = orderMoney*0.3;
        }else if (start == 4){
            money = orderMoney*0.4;
        }else if (start == 5){
            money = orderMoney*0.45;
        }
        return money;
    }


    public static Long between_days(String a, String b) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// 自定义时间格式

        Calendar calendar_a = Calendar.getInstance();// 获取日历对象
        Calendar calendar_b = Calendar.getInstance();

        Date date_a = null;
        Date date_b = null;

        try {
            date_a = simpleDateFormat.parse(a);//字符串转Date
            date_b = simpleDateFormat.parse(b);
            calendar_a.setTime(date_a);// 设置日历
            calendar_b.setTime(date_b);
        } catch (ParseException e) {
            e.printStackTrace();//格式化异常
        }

        long time_a = calendar_a.getTimeInMillis();
        long time_b = calendar_b.getTimeInMillis();

        long between_days = (time_b - time_a) / (1000 * 3600 * 24);//计算相差天数

        return between_days;
    }
}
