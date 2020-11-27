package com.example.collecttoolserver.controller;

import com.example.collecttoolserver.common.ReturnVO;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.collecttoolserver.service.*;

import javax.ws.rs.POST;
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
}
