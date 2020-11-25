package com.example.datatoolserver.controller;


import com.example.datatoolserver.common.ReturnVO;
import com.example.datatoolserver.mapper.xcuserMapper;
import com.example.datatoolserver.pojo.Xcuser;
import com.example.datatoolserver.service.IWxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * @author saodiseng
 * @date 2018/11/5
 */
@RestController
@RequestMapping("/order")
public class orderController {

    @Autowired
    private IWxService wxService;
    @Autowired
    private xcuserMapper xcuserMapper;

    /**
     * 调起支付
     * @param dataplay
     * @return
     * @throws Exception
     */
    @PostMapping("/wx")
    public ReturnVO<Map> wxAdd(@RequestBody Map<String,Object> dataplay) throws Exception {
        return new ReturnVO<>(wxService.doUnifiedOrder(dataplay));
    }

    /**
     * 回调支付结果
     * @param request
     * @return
     */
    @PostMapping("/notify")
    public String wxPayNotify(HttpServletRequest request) {
        String resXml = "";
        try {
            InputStream inputStream = request.getInputStream();
            //将InputStream转换成xmlString
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            String result = wxService.payBack(resXml);
            return result;
        } catch (Exception e) {
            System.out.println("微信手机支付失败:" + e.getMessage());
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            return result;
        }
    }




    /**
     * 开通会员之外的支付接口
     */
    @ResponseBody
    @PostMapping("/payment")
    public ReturnVO<Map> payment(@RequestBody Map<String,Object> data){
        return null;
    }


    /**
     * 收益展示数据
     */
    @ResponseBody
    @PostMapping("/earnings")
    public ReturnVO<Map<String,Object>> earnings(@RequestBody Map<String,Object> data){
        return new ReturnVO(wxService.earnings(data));
    }

    /**
     * 提现记录查询
     */
    @ResponseBody
    @PostMapping("/moneyRecord")
    public ReturnVO<List<Map<String,Object>>> moneyRecord(@RequestBody Map<String,Object> data){
        return new ReturnVO<>(wxService.moneyRecord(data));
    }

    /**
     * 收益记录
     */
    @ResponseBody
    @PostMapping("/earningsRecord")
    public ReturnVO<List<Map<String,Object>>> earningsRecord(@RequestBody Map<String,Object> data){
        return new ReturnVO<>(wxService.earningsRecord(data));
    }

    /**
     * 转账到用户
     */
    @ResponseBody
    @PostMapping("/remittance")
    public ReturnVO<String> remittance(@RequestBody Map<String,Object> data) throws Exception {
        return new ReturnVO<>(wxService.remittance(data));
    }

    /**
     * 发起提现
     */
    @ResponseBody
    @PostMapping("/outMoney")
    public ReturnVO<String> outMoney(@RequestBody Map<String,Object> data){
        String a = wxService.outMoney(data);
        if (a.equals("0")){
            return new ReturnVO().error(40000,"您还没有收益!");
        }else if (a.equals("1")){
            return new ReturnVO().error(40001,"您的余额不足!");
        }
        return new ReturnVO(a);
    }

    /**
     * 查询是否有条件提现
     */
    @PostMapping("/cashStart")
    @ResponseBody
    public ReturnVO<Map<String,Object>> cashStart(@RequestBody Map<String,Object> data){
        String openId = (String) data.get("openId");
        //根据openId查询用户等级
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);

        if (xcuser.getStart()>1) {
            return new ReturnVO("可以提现");
        }

        return new ReturnVO(wxService.cashStart(data)).error(40000,"不能提现");
    }
}


