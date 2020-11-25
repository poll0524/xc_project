package com.example.datatoolserver.controller;

import com.example.datatoolserver.common.ReturnVO;
import com.example.datatoolserver.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class searchController {
    @Autowired
    private ISearchService searchService;

    /**
     * 模糊查询文章列表
     */
    @ResponseBody
    @PostMapping("/searchList")
    public ReturnVO<List> searchList(@RequestBody Map<String,Object> data){
        List<Map<String,Object>> a = searchService.searchList(data);
        if (a == null){
            return new ReturnVO().error(40000,"没有查询到文章/视频");
        }
        return new ReturnVO(a);
    }

    /**
     * 分享有礼导航查询
     */
    @ResponseBody
    @PostMapping("/selectShareNav")
    public ReturnVO<List<Map<String,Object>>> selectShareNav(){
        return new ReturnVO(searchService.selectShareNav());
    }


    /**
     * 分享有礼海报
     */
    @ResponseBody
    @PostMapping("/selectContent")
    public ReturnVO<List<Map<String,Object>>> selectPoster(@RequestBody Map<String,Object> data){
        List<Map<String,Object>> a = searchService.selectPoster(data);
        if (a == null){
            return new ReturnVO().error(40000,"内容为空");
        }
        return new ReturnVO(a);
    }

    /**
     * 帮助中心
     */
    @ResponseBody
    @PostMapping("/selectHelp")
    public ReturnVO<List<Map<String,Object>>> selectHelp(@RequestBody Map<String,Object> data){
        List<Map<String,Object>> a = searchService.selectHelp(data);
        if (a == null){
            return new ReturnVO().error(40000,"内容为空");
        }
        return new ReturnVO(a);
    }

    /**
     *  智能推送
     */
//    @ResponseBody
//    @GetMapping("/sendArticle")
//    public re

}
