package com.example.collecttoolserver.controller;

import com.example.collecttoolserver.common.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.collecttoolserver.service.IOrderService;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class orderController {
    @Autowired
    private IOrderService orderService;
    /**
     * 调起支付
     */
    @PostMapping("/wx")
    public ReturnVO<Map> wxAdd(@RequestBody Map<String,Object> dataplay) throws Exception {
        return new ReturnVO(orderService.doUnifiedOrder(dataplay));
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
            String result = orderService.payBack(resXml);
            return result;
        } catch (Exception e) {
            System.out.println("微信手机支付失败:" + e.getMessage());
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            return result;
        }
    }

    /**
     * 发起提现
     */
    @ResponseBody
    @PostMapping("/outMoney")
    public ReturnVO<String> outMoney(@RequestBody Map<String,Object> data) throws Exception {
        String a = orderService.outMoney(data);
        return new ReturnVO(a);
    }

    /**
     * 余额查询
     */
    @ResponseBody
    @PostMapping("/selectBalance")
    public ReturnVO<Double> selectBalance(@RequestBody Map<String,Object> data){
        return new ReturnVO(orderService.selectBalance(data));
    }



}
