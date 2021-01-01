package com.example.collecttoolserver.controller;

import com.example.collecttoolserver.common.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.collecttoolserver.service.IEarningsService;

import javax.ws.rs.POST;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/earnings")
public class EarningsController {
    @Autowired
    private IEarningsService earningsService;

    /**
     * 写入分享
     */
    @PostMapping("/insertShineUpon")
    @ResponseBody
    public ReturnVO<String> insertShineUpon(@RequestBody Map<String,Object> data){
        String a = earningsService.insertShineUpon(data);
        if (a.equals("本人查看")){
            return new ReturnVO().error(40000,a);
        }else if (a.equals("已存在上级")){
            return new ReturnVO().error(40001,a);
        }
        return new ReturnVO(a);
    }

    /**
     * 上下级关系展示
     */
    @PostMapping("/selectShineUpon")
    @ResponseBody
    public ReturnVO<List<Map<String,Object>>> selectShineUpon(@RequestBody Map<String,Object> data){
        return new ReturnVO(earningsService.selectShineUpon(data));
    }

    /**
     * 下级用户
     */
    @PostMapping("/selectShineUponBelow")
    @ResponseBody
    public ReturnVO<List<Map<String,Object>>> twoUsers(@RequestBody Map<String,Object> data){
        return new ReturnVO(earningsService.twoUsers(data));
    }

    /**
     * 提现记录
     */
    @PostMapping("/selectCash")
    @ResponseBody
    public ReturnVO<List<Map<String,Object>>> selectCash(@RequestBody Map<String,Object> data){
        return new ReturnVO(earningsService.selectCash(data));
    }
}
