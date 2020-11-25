package com.example.datatoolserver.controller;

import com.example.datatoolserver.common.ReturnVO;
import com.example.datatoolserver.pojo.Xcuser;
import com.example.datatoolserver.service.IXcuserService;
import org.json.JSONException;
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
     * 用户关系绑定
     */
    @ResponseBody
    @PostMapping("/shineUpon")
    public ReturnVO<String> shineUpon(@RequestBody Map<String,Object> data){
        String a = xcuserService.shineUpon(data);
        if (a.equals("1")){
            return new ReturnVO().error(40000,"失败");
        }
        return new ReturnVO();
    }

    /**
     * 上下级关系展示
     */
    @ResponseBody
    @PostMapping("/selectShineUpon")
    public ReturnVO<List<Map<String,Object>>> selectShineUpon(@RequestBody Map<String,Object> data){
        return new ReturnVO(xcuserService.selectShineUpon(data));
    }

    @ResponseBody
    @PostMapping("/twoShineUpon")
    public ReturnVO<Map<String,Object>> twoShineUpon(@RequestBody Map<String,Object> data){
        return new ReturnVO(xcuserService.twoShineUpon(data));
    }



    /**
     * 模拟算法
     */
    @ResponseBody
    @PostMapping("/test")
    public ReturnVO<Map<String,Object>> test(@RequestBody Map<String,Object> data){
        return null;
    }


    /**
     * 信息发送
     */
    @ResponseBody
    @PostMapping("/message")
    public ReturnVO<String> message(@RequestBody Map<String,Object> data) throws JSONException {
        return new ReturnVO(xcuserService.message(data));
    }

}
