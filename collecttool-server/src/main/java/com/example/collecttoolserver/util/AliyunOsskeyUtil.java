package com.example.collecttoolserver.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.example.collecttoolserver.common.WeChatUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class AliyunOsskeyUtil {
    private static String endpoint = WeChatUtil.ENDPOINT;
    private static String accessId = WeChatUtil.KEYID;
    private static String accessKey = WeChatUtil.KEYSECRET;

    public Map getSignature(String dir){
        OSSClient ossClient = new OSSClient(endpoint,accessId,accessKey);
        long expireTime = 30;
        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        Date expiration = new Date(expireEndTime);
        PolicyConditions policyConds = new PolicyConditions();
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);//根据参数dir计算的policy，如果和前端uploadfile中参数key的相应字段不一致的话是会报错的

        String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
        byte[] binaryData = postPolicy.getBytes();
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossClient.calculatePostSignature(postPolicy);
        ossClient.shutdown();//业务完成一定要调用shutdown

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("accessId",accessId);
        map.put("policy", encodedPolicy);
        map.put("signature", postSignature);

        return map;
    }


    /**
     * 删除oss文件
     */
    public static String deleteObjects(String bucketName, List<String> bucketUrls) {
        OSSClient client = new OSSClient(endpoint, accessId, accessKey);
        try {
            // 删除Object.
            DeleteObjectsResult deleteObjectsResult = client.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(bucketUrls));
            List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
        } catch (Exception e) {
            e.printStackTrace();
            return "删除失败";
        } finally {
            client.shutdown();
        }
        return "删除成功";
    }
}
