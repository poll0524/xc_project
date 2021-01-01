package com.example.collecttoolserver.service.Impl;

import com.example.collecttoolserver.common.ReturnVO;
import com.example.collecttoolserver.common.WeChatUtil;
import com.example.collecttoolserver.pojo.Cash;
import com.example.collecttoolserver.pojo.ShineUpon;
import com.example.collecttoolserver.pojo.VipOreder;
import com.example.collecttoolserver.pojo.Xcuser;
import com.example.collecttoolserver.service.IEarningsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.collecttoolserver.mapper.*;

import java.text.DecimalFormat;
import java.util.*;

@Service
public class EarningsServiceImpl implements IEarningsService {
    @Autowired
    private shineUponMapper shineUponMapper;
    @Autowired
    private xcuserMapper xcuserMapper;
    @Autowired
    private vipOrederMapper vipOrederMapper;
    @Autowired
    private cashMapper cashMapper;
    /**
     * 写入分享
     */
    public String insertShineUpon(Map<String,Object> data){
        String oneOpenId = (String) data.get("oneOpenId");
        String twoOpenId = (String) data.get("twoOpenId");
        System.out.println("一级:"+oneOpenId+",二级:"+twoOpenId);
        if (twoOpenId == null){
            return "本人查看";
        }
        if (oneOpenId == null && twoOpenId == null){
            return "本人查看";
        }
        //判断one是否存在上级
        ShineUpon shineUpons = shineUponMapper.selectTowOpenId(oneOpenId);
        if(shineUpons == null){
            shineUponMapper.insertShineUpon(WeChatUtil.NAME,oneOpenId);
        }
        //判断是否本人查看
        if (oneOpenId.equals(twoOpenId)){
            return "本人查看";
        }
        //判断下级用户是否已经有上级了
        ShineUpon shineUponTwo = shineUponMapper.selectTowOpenId(twoOpenId);
        if (shineUponTwo != null){
            return "已存在上级";
        }

        //判断是否相反存在
        ShineUpon shineUpon = shineUponMapper.selectOneTowOpenId(oneOpenId,twoOpenId);
        if (shineUpon != null){
            return "已存在上级";
        }

        //写入上下级关系
        shineUponMapper.insertShineUpon(oneOpenId,twoOpenId);

        return "关系写入成功";
    }

    /**
     * 上下级关系展示
     */

    public Map<String,Object> selectShineUpon(Map<String,Object> data){
        String openId = (String) data.get("openId");

        Map<String,Object> datas = new HashMap<>();

        //通过openId获取上级
        ShineUpon shineUpon = shineUponMapper.selectTowOpenId(openId);
        if (shineUpon.getOne_open_id().equals("扩客")){
            datas.put("oneUserName",WeChatUtil.NAME);
        }else {
            datas.put("oneUserName",oneUser(shineUpon.getOne_open_id()));
        }

        //获取用户信息
        datas.put("userInfo",oneUser(openId));

        //获取下级用户
        List<ShineUpon> shineUpons = shineUponMapper.selectOneOpenId(openId);
        datas.put("group",shineUpons.size());
        DecimalFormat df = new DecimalFormat("#.00");
        Double d = moneySum(openId);
        if (d == 0.00){
            datas.put("moneySum",d);
        }else {
            datas.put("moneySum",df.format(d));
        }

        return datas;
    }


    public Map<String,Object> oneUser(String openId){
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);
        Map<String,Object> data = new HashMap<>();
        data.put("userName",xcuser.getUser_name());
        data.put("userHeadimgurl",xcuser.getUser_headimgurl());
        return data;
    }

    public Double moneySum(String openId){
        List<VipOreder> vipOreders = vipOrederMapper.selectReadOpenId(openId);
        Double moneySum = 0.00;
        if (vipOreders.size() != 0){
            for (VipOreder vipOreder : vipOreders) {
                moneySum = moneySum + vipOreder.getBrokerage();
            }
        }
        return moneySum;
    }



    public List<Map<String,Object>> twoUsers(Map<String,Object> data){
        String openId = (String) data.get("openId");
        Integer pag = Integer.parseInt((String) data.get("pag"))*10;
        List<ShineUpon> shineUpons = shineUponMapper.selectOneOpenIds(openId,pag);
        if (shineUpons.size() == 0){
            List a = new ArrayList();
            return a;
        }
        List<Map<String,Object>> dataList = new ArrayList<>();
        for (ShineUpon shineUpon : shineUpons) {
            Xcuser xcuser = xcuserMapper.selectOpenId(shineUpon.getTwo_open_id());
            Map<String,Object> datas = new HashMap<>();
            datas.put("userName",xcuser.getUser_name());
            datas.put("userHeadimgurl",xcuser.getUser_headimgurl());
            //查询金额和时间
            List<VipOreder> vipOreders = vipOrederMapper.selectVipOrder(xcuser.getOpen_id(),openId);
            Double money = 0.0;
            if (vipOreders.size() != 0){
                for (VipOreder vipOreder : vipOreders) {
                    money = money + vipOreder.getBrokerage();
                }
            }
            datas.put("brokerage",money);
            dataList.add(datas);
        }
        return dataList;
    }

    /**
     * 提现记录
     */
    public List<Map<String,Object>> selectCash(Map<String,Object> data){
        String openId = (String) data.get("openId");
        Integer pag = Integer.parseInt((String) data.get("pag"))*10;
        //查询提现记录
        List<Cash> cashList = cashMapper.selectCash(openId,pag);
        if (cashList.size() == 0){
            return null;
        }
        List<Map<String,Object>> dataList = new ArrayList<>();

        for (Cash cash : cashList) {
            Map<String,Object> datas = new HashMap<>();
            datas.put("cashName",cash.getCash_name());
            datas.put("cashTime",cash.getCash_time());
            datas.put("cashMoney",cash.getCash_money());
            dataList.add(datas);
        }
        return dataList;
    }
}
