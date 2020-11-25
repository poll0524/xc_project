package com.example.datatoolserver.controller;

import com.example.datatoolserver.common.ReturnVO;
import com.example.datatoolserver.pojo.Xcuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.datatoolserver.mapper.xcuserMapper;

@RestController
@RequestMapping("/a")
public class testController {
    @Autowired
    private xcuserMapper xcuserMapper;
    @GetMapping("/b")
    @ResponseBody
    public ReturnVO<Xcuser> ccc(){
        return new ReturnVO(xcuserMapper.selectOpenId("oo7wnwqbaAmatYmNkIzGmuZFI548"));
    }

    @GetMapping("/c")
    @ResponseBody
    public Xcuser cccc(){
        return xcuserMapper.selectOpenId("oo7wnwqbaAmatYmNkIzGmuZFI548");
    }

}
