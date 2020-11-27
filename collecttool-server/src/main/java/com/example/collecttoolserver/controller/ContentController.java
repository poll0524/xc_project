package com.example.collecttoolserver.controller;

import com.example.collecttoolserver.common.ReturnVO;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.collecttoolserver.service.IContentService;

import java.util.Map;

@RestController
@RequestMapping("/content")
public class ContentController {
    @Autowired
    private IContentService contentService;
    /**
     * 写入编辑后的内容
     */
    @PostMapping("/insertContent")
    @ResponseBody
    public ReturnVO<Map<String,Object>> insertContent(@RequestBody Map<String,Object> data){
        return new ReturnVO(contentService.insertContent(data));
    }

    /**
     * 根据素材id查询标题内容
     */
    @PostMapping("/selectContentTitle")
    @ResponseBody
    public ReturnVO<Map<String,Object>> selectContentTitle(@RequestBody Map<String,Object> data){
        return new ReturnVO(contentService.selectContentTitle(data));
    }

    /**
     * 根据id查询素材详情
     */
    @PostMapping("/selectContentInfo")
    @ResponseBody
    public ReturnVO<Map<String,Object>> selectContentInfo(@RequestBody Map<String,Object> data){
        return new ReturnVO(contentService.selectContentInfo(data));
    }
}
