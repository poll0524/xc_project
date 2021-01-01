package com.example.collecttoolserver.util;

import com.example.collecttoolserver.common.WeChatUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

public class FileUtil {
    //文件路径+名称
    private static String fileNameTemp;
    /**
     * 创建文件
     * @param fileName 文件名字
     * @param content 文件内容
     * @return
     */
    public static boolean createFile(String fileName,String content){
        Boolean bool = false;
        //文件路径+名称+文件类型
        fileNameTemp = WeChatUtil.CONTENT_PATH+fileName+".txt";
        File file = new File(fileNameTemp);
        try {
            //如果文件不存在,则创建新的文件
            if (!file.exists()){
                file.createNewFile();
                bool = true;
                //创建文件成功后，写入内容到文件里
                writeFileContent(fileNameTemp, content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bool;
    }


    public static boolean writeFileContent(String filepath,String newstr) throws IOException{
        Boolean bool = false;
        String filein = newstr + "\r\n";
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            //文件路径(包括文件名称)
            File file = new File(filepath);
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            //文件原有内容
            for(int i=0;(temp =br.readLine())!=null;i++){
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }


    public static String readTxt(String filePath) {
        try {
            File file = new File(filePath);
            if(file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = br.readLine();
                br.close();
                return lineTxt;
            } else {
                System.out.println("文件不存在!");
            }
        } catch (Exception e) {
            System.out.println("文件读取错误!");
        }
        return null;
    }


}
