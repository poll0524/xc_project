package com.example.xcschoolserver.controller;

import com.example.xcschoolserver.common.ReturnVO;
import com.example.xcschoolserver.pojo.Xcuser;
import com.example.xcschoolserver.service.IXcuserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/xcuser")
public class xcuserController {
    @Autowired
    private IXcuserService xcuserService;



    /**
     * 显示积分商城二维码
     */
    @ResponseBody
    @PostMapping("/shopping")
    public ReturnVO<Map<String,Object>> shopping(@RequestBody Map<String,Object> data){
        return new ReturnVO(xcuserService.shopping(data));
    }



    /**
     * 修改客服二维码图片
     */
    @ResponseBody
    @PostMapping("/updataCode")
    public ReturnVO<Map<String,Object>> updataCode(@RequestBody Map<String,Object> data){
        return new ReturnVO(xcuserService.updataCode(data));
    }

    /**
     * 查询所有用户
     */
    @ResponseBody
    @PostMapping("/selectUser")
    public ReturnVO<Map<String,Object>> selectUser(@RequestBody Map<String,Object> data, @RequestHeader("userToken") String userToken){
        return new ReturnVO(xcuserService.selectUser(data,userToken));
    }

    /**
     * 查询用户详情
     */
    @ResponseBody
    @PostMapping("/userInfo")
    public ReturnVO<Xcuser> userInfo(@RequestBody Map<String,Object> data, @RequestHeader("userToken") String userToken){
        return new ReturnVO(xcuserService.userInfo(data,userToken));
    }

    /**
     * 根据名称模糊搜索
     */
    @ResponseBody
    @PostMapping("/userSearch")
    public ReturnVO<Map<String,Object>> userSearch(@RequestBody Map<String,Object> data, @RequestHeader("userToken") String userToken){
        return new ReturnVO(xcuserService.userSearch(data,userToken));
    }

    /**
     * 更新用户信息接口
     */
    @ResponseBody
    @PostMapping("/updataUser")
    public ReturnVO<String> updataUser(@RequestBody Map<String,Object> data, @RequestHeader("userToken") String userToken){
        return new ReturnVO(xcuserService.updataUser(data,userToken));
    }

    /**
     * 模拟算法
     */
    @ResponseBody
    @PostMapping("/test")
    public ReturnVO<Map<String,Object>> test(@RequestBody Map<String,Object> data){
        return null;
    }




}
