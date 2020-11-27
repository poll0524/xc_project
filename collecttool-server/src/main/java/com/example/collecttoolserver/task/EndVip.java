package com.example.collecttoolserver.task;

import com.example.collecttoolserver.pojo.Xcuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.example.collecttoolserver.mapper.xcuserMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class EndVip {
    @Autowired
    private xcuserMapper xcuserMapper;

    //    @Scheduled(cron = "0/10 * * * * *")//20,秒一次
    @Scheduled(cron = "0 0 22 * * ?")//每天8:0:0的时候执行一次
    @Transactional(rollbackFor = Exception.class)
    public void endVipTime() throws ParseException {
        List<Xcuser> xcusers = xcuserMapper.selectXcuser();
        for (Xcuser xcuser : xcusers) {
            //提取会员时间检测会员是否到期
            if (xcuser.getEnd_time() != null && !xcuser.getEnd_time().equals("0")){//判断会员到期时间是否为null
                //获取当前时间戳
                Date date = new Date();
                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
                String nowadays = dateFormat.format(date);
                Date now = dateFormat.parse(nowadays);
                long egm = now.getTime();

                //会员到期时间戳
                String[] times = xcuser.getEnd_time().split("\\s+");
                String time = times[0];
                Date end_time = dateFormat.parse(time);
                long end = end_time.getTime();

                if (end < egm){//如果结束时间大于当前时间,结束会员
                    //到数据库更改用户信息
                    xcuserMapper.updataVip(xcuser.getOpen_id(),null,null,null);
                }
            }
        }

    }
}
