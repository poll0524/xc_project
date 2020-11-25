package com.example.collecttoolserver.util;


import com.example.collecttoolserver.common.HttpUtil;
import com.example.collecttoolserver.common.ReturnVO;
import com.example.collecttoolserver.common.WeChatUtil;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class WeChatFinalUtil {

    /**
     * 编码格式
     */
    private String ENCODING = "UTF-8";
    /**
     * 加密算法
     */
    public String KEY_ALGORITHM = "AES/CBC/PKCS7Padding";

    /**
     * 请求用户openId
     */
    public Map<String, Object> userOpenId(String code) throws JSONException {
        String result = HttpUtil.get(WeChatUtil.GET_OPENID.replace("APPID",WeChatUtil.APPID).replace("SECRET",WeChatUtil.APPSECRET).replace("JSCODE",code));
        JSONObject jsonObject = new JSONObject(new String(result));
        Map<String,Object> map = new HashMap<>();
        map.put("openId",jsonObject.getString("openid"));
        map.put("sessionKey",jsonObject.getString("session_key"));
        return map;
    }

    public String decryptUserInfo(String encryptedData,String iv,String sessionKey) throws Exception {
        byte[] data = Base64.decodeBase64(encryptedData);
        byte[] aseKey = Base64.decodeBase64(sessionKey);
        byte[] ivData = Base64.decodeBase64(iv);
        // 如果密钥不足16位，那么就补足
        int base = 16;
        if (aseKey.length % base != 0) {
            int groups = aseKey.length / base + (aseKey.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(aseKey, 0, temp, 0, aseKey.length);
            aseKey = temp;
        }
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        SecretKeySpec spec = new SecretKeySpec(aseKey, "AES");
        AlgorithmParameters parameters = generateIv(ivData);
        cipher.init(Cipher.DECRYPT_MODE, spec,parameters);
        byte[] result = cipher.doFinal(data);
        return new String(result,ENCODING);
    }

    public static AlgorithmParameters generateIv(byte[] iv) throws Exception{
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }


}
