package com.example.collecttoolserver.util;

import com.alibaba.fastjson.JSON;
import com.example.collecttoolserver.common.WeChatUtil;
import com.alibaba.fastjson.JSONObject;
import org.mybatis.logging.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

public class WxQrCodeUtil {
    /**
     * 用于获取access_token
     * @return  access_token
     * @throws Exception
     */
    public static String getAccessToken() throws Exception {
        String requestUrl = WeChatUtil.ACCESSTOKEN.replace("APPID",WeChatUtil.APPID).replace("APPSECRET",WeChatUtil.APPSECRET);
        URL url = new URL(requestUrl);
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 得到请求的输出流对象
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes("");
        out.flush();
        out.close();

        // 建立实际的连接
        connection.connect();
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = null;
        if (requestUrl.contains("nlp"))
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "GBK"));
        else
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        String result = "";
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result += getLine;
        }
        in.close();
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
        String accesstoken=jsonObject.getString("access_token");
        return accesstoken;
    }


    /*
     * 获取 二维码图片
     *
     */
    public static String getminiqrQr(String accessToken,String uploadPath,String urls) {
        String ctxPath = uploadPath;
        String fileName=getOrderId()+".png";
        String bizPath = "files";
        String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String ppath =ctxPath + File.separator + bizPath + File.separator + nowday;
        File file = new File(ctxPath);
        if (!file.exists()) {
            file.mkdirs();// 创建文件根目录
        }
        String savePath = file.getPath() + File.separator + fileName;
        String qrCode = bizPath + File.separator + nowday+ File.separator + fileName;
//        if (ppath.contains("\\")) {
//            ppath = ppath.replace("\\", "/");
//        }
        if (qrCode.contains("\\")) {
            qrCode = qrCode.replace("\\", "/");
        }
//        String codeUrl=ppath+"/twoCode.png";
//        System.out.print(qrCode);
//        System.out.print(savePath);
        try
        {
//            URL url = new URL("https://api.weixin.qq.com/wxa/getwxacode?access_token="+accessToken);
            String wxCodeURL = WeChatUtil.WXCODE.replace("ACCESS_TOKEN",accessToken);
            URL url = new URL(wxCodeURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
            // conn.setReadTimeout(2000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            JSONObject paramJson = new JSONObject();
            System.out.println(urls);
//            paramJson.put("scene", urls);
            paramJson.put("path", urls);
//            paramJson.put("page", "pages/index/index"); //小程序暂未发布我没有带page参数
            paramJson.put("width", 430);
//            paramJson.put("is_hyaline", true);
//            paramJson.put("auto_color", true);
            /**
             * line_color生效
             * paramJson.put("auto_color", false);
             * JSONObject lineColor = new JSONObject();
             * lineColor.put("r", 0);
             * lineColor.put("g", 0);
             * lineColor.put("b", 0);
             * paramJson.put("line_color", lineColor);
             * */

            printWriter.write(paramJson.toString());
            // flush输出流的缓冲
            printWriter.flush();
            //开始获取数据
            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            OutputStream os = new FileOutputStream(new File(savePath));
            int len;
            byte[] arr = new byte[1024];
            while ((len = bis.read(arr)) != -1)
            {
                os.write(arr, 0, len);
                os.flush();
            }
            os.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

//        return "http://192.168.1.6:8083/xcvideo/"+fileName;
        return "https://xiaocisw.site/poster/userVideo/"+fileName;
    }

    /**
     * 生成订单号
     */
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



    // 编码方式
    private static final String ENCODE_UTF = "UTF-8";

    public static int getLength(String str) throws UnsupportedEncodingException {
        if (str == null || str.length() == 0) {
            return 0;
        }

        return str.getBytes(ENCODE_UTF).length;

    }


    /**
     * 计算中英文字符串的字节长度
     *
     * @param str
     * @return int
     */
    public static int getStrLength(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        int len = 0;
        for (int i = 0, j = str.length(); i < j; i++) {
            //UTF-8编码格式中文占三个字节，GBK编码格式 中文占两个字节 ;
            len += (str.charAt(i) > 255 ? 3 : 1);
        }
        return len;
    }


    public static void main(String[] args) throws Exception {
        String str = "pages/video/video?id=447&start=1&title=你好&are=0";
        System.out.println(getLength(str));

        System.out.println(getminiqrQr(getAccessToken(),WeChatUtil.CONTENT_PATH,str));
    }

}
