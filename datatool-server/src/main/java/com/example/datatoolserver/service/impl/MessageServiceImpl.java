package com.example.datatoolserver.service.impl;

import com.example.datatoolserver.mapper.countDownMapper;
import com.example.datatoolserver.mapper.shineUponMapper;
import com.example.datatoolserver.mapper.xcuserMapper;
import com.example.datatoolserver.pojo.CountDown;
import com.example.datatoolserver.pojo.ShineUpon;
import com.example.datatoolserver.pojo.Xcuser;
import com.example.datatoolserver.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MessageServiceImpl implements IMessageService {
    @Autowired
    private shineUponMapper shineUponMapper;
    @Autowired
    private xcuserMapper xcuserMapper;
    @Autowired
    private countDownMapper countDownMapper;

    /**
     * 查询邀请人数
     * @param data
     * @return
     */
    public Map<String,Object> selectShineUpon(Map<String,Object> data) throws ParseException {
        String openId = (String) data.get("openId");
        //根据openId查询邀请人数
        List<ShineUpon> shineUpons = shineUponMapper.selectReadOpenId(openId);
        Map<String,Object> datas = new HashMap<>();
        if (shineUpons.size() == 0){
            datas.put("quantity",0);
            List a = new ArrayList();
            datas.put("userInfo",a);
            datas.put("endTime",endTime(data));
            return datas;
        }
        datas.put("quantity",shineUpons.size());
        List<Map<String,Object>> userInfos = new ArrayList<>();
        for (ShineUpon shineUpon : shineUpons) {
            Map<String,Object> datainfo = new HashMap<>();
            //查询用户信息
            Xcuser xcuser = xcuserMapper.selectOpenId(shineUpon.getOpen_id());
            datainfo.put("userName",xcuser.getUser_name());
            datainfo.put("userHeadimgurl",xcuser.getUser_headimgurl());
            userInfos.add(datainfo);
        }
        datas.put("userInfo",userInfos);
        datas.put("endTime",endTime(data));
        return datas;
    }

    public String updataStart(Map<String,Object> data){
        //获取用户openId
        String openId = (String) data.get("openId");
        //根据openId查询用户用户等级
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);
        if (xcuser.getStart()>=2){
            return "no";
        }
        //到数据更新会员等级
        //获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String beginTime = df.format(new Date());
        //结束时间
        Calendar calendar2 = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar2.add(Calendar.YEAR, 1);
        String endTime = sdf2.format(calendar2.getTime());
        xcuserMapper.updataUserStarts(openId,2,beginTime,endTime);
        return "ok";
    }

    /**
     * 活动倒计时
     * @return
     */
    public Map<String,Object> endTime(Map<String,Object> data) throws ParseException {
        //获取openId
        String openId = (String) data.get("openId");
        if (openId == null){
            Map<String,Object> a = new HashMap<>();
            a.put("code",1);
            return a;
        }
        //根据openId到数据库查询时间
        CountDown countDown = countDownMapper.selectCountDown(openId);
        //创建map封装数据
        Map<String,Object> datas = new HashMap<>();
        //如果countdown不为空时
        if (countDown != null){
            //说明该用户正在参加活动或者已经参加过了活动
            //获取当前时间戳
            Long time = new Date().getTime();
            //获取活动结束时间戳
            long ts = timestamp(countDown.getEnd_time());
            //如果结束时间大于当前时间
            if (ts > time){
                long activity = ts - time;
                long days = activity / (1000 * 60 * 60 * 24);

                long hours = (activity-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
                long minutes = (activity-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
                datas.put("day",days);
                datas.put("hour",hours);
                datas.put("minute",minutes);
                datas.put("code",3);
                return datas;
            }else {//如果结束时间小于当前时间
                Map<String,Object> a = new HashMap<>();
                a.put("code",2);
                return a;
            }
        }
        //如果countdown为空时,向数据库写入数据
        //获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String beginTime = df.format(new Date());
        //结束时间
        Calendar calendar2 = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar2.add(Calendar.DATE, 15);
        String endTime = sdf2.format(calendar2.getTime());
        //将数据写入数据库
        countDownMapper.insertCountDown(openId,beginTime,endTime);
        //计算倒计时
        Long beginTamp = timestamp(beginTime);
        Long endTamp = timestamp(endTime);
        long activity = endTamp - beginTamp;
        long days = activity / (1000 * 60 * 60 * 24);

        long hours = (activity-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
        long minutes = (activity-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
        datas.put("day",days);
        datas.put("hour",hours);
        datas.put("minute",minutes);
        datas.put("code",3);
        return datas;
    }

    /**
     * 获取时间戳方法
     * @param time
     * @return
     * @throws ParseException
     */
    private Long timestamp(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(time);
        long ts = date.getTime();
        return ts;
    }
}
