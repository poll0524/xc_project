package com.example.collecttoolserver.controller;

import com.example.collecttoolserver.common.ReturnVO;
import com.example.collecttoolserver.pojo.Xcuser;
import org.aspectj.apache.bcel.generic.RET;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.collecttoolserver.service.ILoginService;
import com.example.collecttoolserver.mapper.*;

import javax.ws.rs.POST;
import java.lang.annotation.Retention;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private ILoginService loginService;
    @Autowired
    private xcuserMapper xcuserMapper;

    @ResponseBody
    @PostMapping("/jsCode")
    public ReturnVO<Map<String, Object>> jsCode(@RequestBody Map<String,Object> data) throws JSONException {
        return new ReturnVO(loginService.jsCode(data));
    }

    /**
     * 获取用户电话号码
     */
    @ResponseBody
    @PostMapping("/userPhoneLogin")
    public ReturnVO<Map<String,Object>> userPhoneLogin(@RequestBody Map<String,Object> data) throws Exception {
        return new ReturnVO(loginService.userPhoneLogin(data));
    }

    @ResponseBody
    @PostMapping("/userInfoLogin")
    public ReturnVO<Map<String,Object>> userInfoLogin(@RequestBody Map<String,Object> data){
        Map<String,Object> code = loginService.userInfoLogin(data);
        System.out.println(code);
        String describe = (String) code.get("describe");
        if (describe.equals("登录成功,有手机号")){
            return new ReturnVO(code).error(200,"登录成功,有手机号");
        }
        return new ReturnVO(code).error(40000,"登录成功,无手机号");
    }

    /**
     * 查询用户数据
     */
    @ResponseBody
    @PostMapping("/userInfo")
    public ReturnVO<Xcuser> userInfo(@RequestBody Map<String,Object> data){
        String openId = (String) data.get("openId");
        return new ReturnVO(xcuserMapper.selectOpenId(openId));
    }

    /**
     * oss配置
     */
    @ResponseBody
    @PostMapping("/ossInfo")
    public ReturnVO<Map<String,Object>> ossInfo(@RequestBody Map<String,Object> data){
        return new ReturnVO(loginService.ossInfo(data));
    }

    /**
     * 是否失效
     */
    @ResponseBody
    @PostMapping("/isValid")
    public boolean isValid(@RequestBody Map<String,Object> data) {
       URL url;
       try {
          url = new URL((String) data.get("strLink"));
          HttpURLConnection connt = (HttpURLConnection)url.openConnection();
          connt.setRequestMethod("HEAD");

          String strMessage = connt.getResponseMessage();

          if (strMessage.compareTo("Not Found") == 0) {
                 return false;
          }
          connt.disconnect();
       } catch (Exception e) {
            return false;
       }
       return true;
    }


    /**
     * 数据红点
     */
    @ResponseBody
    @PostMapping("/dataUpdate")
    public ReturnVO<String> dataUpdate(@RequestBody Map<String,Object> data){
        String a = loginService.dataUpdate(data);
        if (a.equals("有红点")){
            return new ReturnVO().error(40000,"有红点");
        }
        return new ReturnVO(a);
    }

    @ResponseBody
    @PostMapping("/updatsKuoke")
    public ReturnVO<Map<String,Object>> updatsKuoke(){
        Map<String,Object> map = new HashMap<>();
        map.put("key",true);
        return new ReturnVO(map);
    }

}
