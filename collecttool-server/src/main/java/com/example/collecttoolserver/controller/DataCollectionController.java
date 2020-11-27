package com.example.collecttoolserver.controller;

import com.example.collecttoolserver.common.ReturnVO;
import org.springframework.web.bind.annotation.*;
import com.example.collecttoolserver.service.IDataCollectionService;

import java.util.Map;

@RestController
@RequestMapping("/dataCollection")
public class DataCollectionController {
    private IDataCollectionService dataCollectionService;
    /**
     * 通过链接爬取数据
     */
    @ResponseBody
    @PostMapping("/spiderContent")
    public ReturnVO<Map<String,Object>> spiderContent(@RequestBody Map<String,Object> data){
        Map<String,Object> content = dataCollectionService.spiderContent(data);
        if (content == null){
            return new ReturnVO().error(40000,"该链接没有采集到内容!!!");
        }
        return new ReturnVO(content);
    }
}
