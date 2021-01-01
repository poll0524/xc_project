package com.example.collecttoolserver.controller;

import com.example.collecttoolserver.common.ReturnVO;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.collecttoolserver.service.IContentService;

import java.util.List;
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

    /**
     * 根据openId查询视频列表
     */
    @PostMapping("/selectContentOpenId")
    @ResponseBody
    public ReturnVO<Map<String,Object>> selectContentOpenId(@RequestBody Map<String,Object> data){
        return new ReturnVO(contentService.selectContentOpenId(data));
    }

    /**
     * 删除素材库
     */
    @PostMapping("/deleteContent")
    @ResponseBody
    public ReturnVO<String> deleteContent(@RequestBody Map<String,Object> data){
        return new ReturnVO(contentService.deleteContent(data));
    }

    /**
     * 更新视频
     */
    @PostMapping("/updateContent")
    @ResponseBody
    public ReturnVO<String> updateContent(@RequestBody Map<String,Object> data){
        return new ReturnVO(contentService.updateContent(data));
    }

    /**
     * 查询视频分类
     */
    @PostMapping("/selectClassify")
    @ResponseBody
    public ReturnVO<List<Map<String,Object>>> selectClassify(){
        return new ReturnVO(contentService.selectClassify());
    }

    /**
     * 删除oss静态资源
     */


}
