package com.example.collecttoolserver.controller;

import com.example.collecttoolserver.common.ReturnVO;
import org.aspectj.apache.bcel.generic.RET;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.collecttoolserver.service.ILoginService;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private ILoginService loginService;

    @ResponseBody
    @PostMapping("/jsCode")
    public ReturnVO<Map<String, Object>> jsCode(@RequestBody Map<String,Object> data) throws JSONException {
        return new ReturnVO(loginService.jsCode(data));
    }

    @ResponseBody
    @PostMapping("/userLogin")
    public ReturnVO<Map<String,Object>> userLogin(@RequestBody Map<String,Object> data) throws Exception {
        return new ReturnVO(loginService.userLogin(data));
    }
}
