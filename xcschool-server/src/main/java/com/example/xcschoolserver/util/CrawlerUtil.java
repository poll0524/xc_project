package com.example.xcschoolserver.util;

import com.example.xcschoolserver.common.HttpUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class CrawlerUtil {


//    private static String API_URL;

    /**
     * 获取抖音视频时效链接
     */
    public static Map demo2(String url) throws IOException, JSONException {
        //发起爬取请求
        String content = HttpUtil.postFrom(url);
        //将爬取内容转换为json格式
        JSONObject jsonContent = new JSONObject(content);
        //爬取类型
        String type = jsonContent.getString("source");
        //获取爬取标题
        String title = jsonContent.getString("title");
        //获取爬取封面图片
        String coverImg = jsonContent.getString("cover_url");
        //视频时效链接
        String videoUrl =  jsonContent.getString("video_url");
        //整理返回路径
        Map<String,Object> data = new HashMap<>();
        data.put("type",type);
        data.put("title",title);
        data.put("coverImg",coverImg);
        data.put("videoUrl",videoUrl);
        return data;
    }




    /**
     * 将视频下载并存入oss
     * @param url
     * @return
     * @throws Exception
     */
    public static Map video(String url,String coverImg,String title) throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Connection", "keep-alive");
        headers.put("Host", "aweme.snssdk.com");
        headers.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 12_1_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16D57 Version/12.0 Safari/604.1");

        BufferedInputStream in = Jsoup.connect(url).headers(headers).timeout(10000).ignoreContentType(true).execute().bodyStream();

        String timetmp = getOrderId();

        //线下路径
//        String fileAddress = "E:/开发软件/tomcat/apache-tomcat-8.5.53/webapps/xcvideo/"+timetmp+".mp4";

        //线上路径
        String fileAddress = "/poster/userVideo/"+timetmp+".mp4";

        //7.封装一个保存文件的路径对象
        File fileSavePath = new File(fileAddress);

        //注:如果保存文件夹不存在,那么则创建该文件夹
        File fileParent = fileSavePath.getParentFile();
        if(!fileParent.exists()){
            fileParent.mkdirs();
        }
        //8.新建一个输出流对象
        OutputStream out =
                new BufferedOutputStream(
                        new FileOutputStream(fileSavePath));


        //9.遍历输出文件
        int b ;
        while((b = in.read()) != -1) {
            out.write(b);
        }

        out.close();//关闭输出流
        in.close(); //关闭输入流

        Map data = new HashMap();
        data.put("title",title);
        data.put("coverImg",coverImg);
        //线上
        data.put("videoUrl","http://xiaocisw.site/poster/userVideo/"+timetmp+".mp4");
        //线下
//        data.put("videoUrl","http://192.168.0.157:8083/xcvideo/"+timetmp+".mp4");
        return data;
    }

    /**
     * 微信聊天框采集视频
     * @return
     */
    public static Map videos(String url) throws JSONException, IOException {
        //发起爬取请求
        String content = HttpUtil.postFrom(url);
        //将爬取内容转换为json格式
        JSONObject jsonContent = new JSONObject(content);
        //获取爬取标题
        String title = jsonContent.getString("title");
        //获取爬取封面图片
        String coverImg = jsonContent.getString("cover_url");
        //整理返回路径
        Map<String,Object> data = new HashMap<>();
        data.put("title",title);
        data.put("coverImg",coverImg);
        return data;
    }




    public static String getOrderId() {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar calendar = Calendar.getInstance();
        String dateName = df.format(calendar.getTime());

        Random ne=new Random();//实例化一个random的对象ne
        int x = ne.nextInt(999-100+1)+100;//为变量赋随机值100-999
        String random_order = String.valueOf(x);
        String orderId = dateName+random_order;
        return orderId;
    }
}
