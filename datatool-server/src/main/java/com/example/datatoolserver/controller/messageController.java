package com.example.datatoolserver.controller;

import com.example.datatoolserver.common.ReturnVO;
import com.example.datatoolserver.mapper.shineUponMapper;
import com.example.datatoolserver.mapper.xcuserMapper;
import com.example.datatoolserver.pojo.ShineUpon;
import com.example.datatoolserver.pojo.Xcuser;
import com.example.datatoolserver.service.IMessageService;
import com.example.datatoolserver.task.VipInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/message")
public class messageController {
    @Autowired
    private VipInfo vipInfo;
    @Autowired
    private IMessageService messageService;
    @Autowired
    private shineUponMapper shineUponMapper;
    @Autowired
    private xcuserMapper xcuserMapper;

    /**
     *一推五活动拉取新人
     */
    @ResponseBody
    @PostMapping("/shineUpon")
    public ReturnVO<Map<String,Object>>  shineUpon(@RequestBody Map<String,Object> data){
        String readOpenId = (String) data.get("readOpenId");
        String openId = (String) data.get("openId");
        //判断两个openId是否相同
        if (readOpenId.equals(openId)){
            return new ReturnVO().error(40000,"自己不能邀请自己");
        }else {//如果不同
            //查询关系是否存在
            ShineUpon shineUpon = shineUponMapper.selectShine(openId,readOpenId);
            //如果关系不存在
            if (shineUpon == null){
                //查询该用户是否存在其他关系
                ShineUpon shineUpon1 = shineUponMapper.selectOpenId(openId);
                //如果存在其他关系
                if (shineUpon1 != null && !shineUpon1.getRead_open_id().equals("来找客")){
                    Xcuser xcuser = xcuserMapper.selectOpenId(readOpenId);
                    return new ReturnVO(xcuser).error(40001,"与其他人,邀请关系已经存在");
                }else if (shineUpon1 != null && shineUpon1.getRead_open_id().equals("来找客")){
                    //判断是否交换邀请
                    List<ShineUpon> shineUpons = shineUponMapper.selectReadOpenId(openId);
                    if (shineUpons.size() != 0){
                        for (ShineUpon upon : shineUpons) {
                            if (upon.getOpen_id().equals(readOpenId)){
                                return new ReturnVO().error(40003,"您邀请过该用户,不能交换加入!!!");
                            }
                        }
                    }
                    return new ReturnVO().error(40004,"您已经不是新用户了,不能参与该活动");
                }
            }else {//如果关系存在
                return new ReturnVO().error(40002,"您已经加入该邀请");
            }
        }

        Map<String,Object> dataInfo = vipInfo.shineUpon(openId,readOpenId);
        return new ReturnVO(dataInfo);
    }


    /**
     * 查询邀请人数
     */
    @ResponseBody
    @PostMapping("/selectShineUpon")
    public ReturnVO<Map<String,Object>> selectShineUpon(@RequestBody Map<String,Object> data) throws ParseException {
        return new ReturnVO(messageService.selectShineUpon(data));
    }

    /**
     * 立即领取接口
     */
    @ResponseBody
    @PostMapping("/updataStart")
    public ReturnVO<Map<String,Object>> updataStart(@RequestBody Map<String,Object> data){
        String a = messageService.updataStart(data);
        if (a.equals("on")){
            return new ReturnVO().error(40000,"您当前已经是充值会员了");
        }
        return new ReturnVO(a);
    }

    /**
     * 活动倒计时
     */
    @ResponseBody
    @PostMapping("/endTime")
    public ReturnVO<String> endTime(@RequestBody Map<String,Object> data) throws ParseException {
        Map<String,Object> a = messageService.endTime(data);
        Integer c = (Integer) a.get("code");
        if (c==2){
            return new ReturnVO().error(40000,"超过该活动倒计时!!!");
        }else if (c==1){
            return new ReturnVO().error(40001,"openId为空");
        }
        return new ReturnVO(a);
    }
}
