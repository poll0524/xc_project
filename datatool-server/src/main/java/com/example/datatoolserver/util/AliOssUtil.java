package com.example.datatoolserver.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.MultipartConfigElement;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class AliOssUtil {
    static Logger logger = Logger.getLogger(String.valueOf(AliOssUtil.class));

//    private static String endpoint = "xiaocisw-video.oss-accelerate.aliyuncs.com";
    private static String endpoint = "oss-accelerate.aliyuncs.com";

    private static String accessKeyId = "LTAI4GJcedWzZ1XuCijsFhkL";

    private static String accessKeySecret = "7XdOEyTjpMOpA58uOaGWQ55zIpuTCZ";
    private static String bucketName = "xiaocisw-video";

    /**
     * 上传公开文件至公共读写bucket
     * @author LH_Yu
     * @Param uploadFile 上传文件
     * @Param picturePath 上传路径及取出url的key
     */
    public static String uploadOSSFrees(String fileName,InputStream uploadFile, String picturePath) throws Exception {
        /**
         * 创建OSS客户端
         */
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            PutObjectResult a = ossClient.putObject(new PutObjectRequest(bucketName, picturePath+fileName, uploadFile));
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return null;
    }

    public static String uploadOSSFree(MultipartFile uploadFile, String picturePath) throws Exception {
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 上传：picturePath + uploadFile.getOriginalFilename() --> key
        ossClient.putObject(bucketName, picturePath + uploadFile.getOriginalFilename(), new ByteArrayInputStream(uploadFile.getBytes()));
        // 关闭client
        ossClient.shutdown();
        //设置过期时间 -- 十年
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        //取出文件上传路径
        String url = ossClient.generatePresignedUrl(bucketName, picturePath + uploadFile.getOriginalFilename(), expiration).toString();
        return url;
    }

    /**
     * 删除oss文件
     */
    public static String deleteObjects(String bucketName, List<String> bucketUrls) {
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);

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
