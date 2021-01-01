package com.example.collecttoolserver.controller;

import com.example.collecttoolserver.common.ReturnVO;
import com.example.collecttoolserver.common.WeChatUtil;
import com.example.collecttoolserver.util.WxQrCodeUtil;
import org.aspectj.apache.bcel.generic.RET;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.collecttoolserver.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/share")
public class shareController {
    @Autowired
    private IShareService shareService;
    /**
     * 写入分享
     */
    @PostMapping("/insertShare")
    @ResponseBody
    public ReturnVO<String> insertShare(@RequestBody Map<String,Object> data){
        return new ReturnVO(shareService.insertShare(data));
    }

    /**
     * 查询分享素材
     */
    @PostMapping("/selectShare")
    @ResponseBody
    public ReturnVO<List<Map<String,Object>>> selectShare(@RequestBody Map<String,Object> data){
        return new ReturnVO(shareService.selectShare(data));
    }

    /**
     * 更改电话状态
     */
    @PostMapping("/updataPhoneTell")
    @ResponseBody
    public ReturnVO<Map<String,Object>> updataPhoneTell(@RequestBody Map<String,Object> data){
        return new ReturnVO(shareService.updataPhoneTell(data));
    }

    /**
     * 查询分享信息
     */
    @PostMapping("/selectShareUser")
    @ResponseBody
    public ReturnVO<Map<String,Object>> selectShareUser(@RequestBody Map<String,Object> data){
        Map<String,Object> map = shareService.selectShareUser(data);
        String report = (String) map.get("report");
        if (report.equals("无分享信息,请调用")){
            return new ReturnVO(map).error(40000,report);
        }else {
            return new ReturnVO(map).error(200,report);
        }
    }

    /**
     * 生成二维码图片
     */
    @PostMapping("/getCode")
    @ResponseBody
    public ReturnVO<Map<String,Object>> getCode(@RequestBody Map<String,Object> datas){
        return new ReturnVO(shareService.getCode(datas));
    }

    /**
     * 存储页面链接
     */

}
