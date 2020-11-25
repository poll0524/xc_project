package com.example.datatoolserver.controller;

import com.example.datatoolserver.common.ReturnVO;
import com.example.datatoolserver.pojo.Reason;
import com.example.datatoolserver.service.IOrderAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderAdmin")
public class orderAdminController {
    @Autowired
    private IOrderAdminService orderAdminService;


    /**
     * 查询提现记录
     */
    @ResponseBody
    @PostMapping("/selectOrder")
    public ReturnVO<Map<String,Object>> selectOrder(@RequestBody Map<String,Object> data){
        return new ReturnVO(orderAdminService.selectOrder(data));
    }

    /**
     * 按照时间或用户名查询提现记录
     */
    @ResponseBody
    @PostMapping("/selectTimeOrder")
    public ReturnVO<List<Map<String,Object>>> selectTimeOrder(@RequestBody Map<String,Object> data, @RequestHeader("userToken") String userToken){
        Map<String,Object> a = orderAdminService.selectTimeOrder(data,userToken);
        if (a == null){
            return new ReturnVO().error(40000,"查询失败");
        }
        return new ReturnVO(a);
    }

    /**
     * 提现驳回理由
     */
    @ResponseBody
    @PostMapping("/selectReason")
    public ReturnVO<List<Reason>> selectReason(){
        return new ReturnVO(orderAdminService.selectReason());
    }

    /**
     * 驳回提现记录
     */
    @ResponseBody
    @PostMapping("/updataCash")
    public ReturnVO<String> updataCash(@RequestBody Map<String,Object> data, @RequestHeader("userToken") String userToken){
        String a = orderAdminService.updataCash(data,userToken);
        if (a.equals("修改失败")){
            return new ReturnVO().error(40000,a);
        }
        return new ReturnVO(a);
    }


    /**
     * 提现记录通过发起转账
     */
    @ResponseBody
    @PostMapping("/outMoney")
    public ReturnVO<String> outMoney(@RequestBody Map<String,Object> data) throws Exception {
        return new ReturnVO(orderAdminService.outMoney(data));
    }



}
