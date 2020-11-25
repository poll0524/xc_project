package com.example.xcschoolserver.controller;

import com.example.xcschoolserver.common.ReturnVO;
import com.example.xcschoolserver.service.IVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/video")
public class videoController {
    @Autowired
    private IVideoService videoService;


    /**
     * 修改视频标题,并上传到分享表
     */
    @ResponseBody
    @PostMapping("/alterVideo")
    public ReturnVO<String> alterVideo(@RequestBody Map<String,Object> video){
        String a = videoService.alterVideo(video);
        if (a == null){
            return new ReturnVO().error(40000,"视频地址为空");
        }
        return new ReturnVO(a);
    }

    /**
     * 按品牌查询
     */
    @ResponseBody
    @PostMapping("/selectVideo")
    public ReturnVO<Map<String,Object>> selectVideo(@RequestBody Map<String,Object> video){
//        List<Map<String,Object>> datas = videoService.selectVideo(video);
//        for (Map<String, Object> data : datas) {
//            if (data.get("erorr") != null){
//                if (data.get("erorr").equals("还没有分享过文章")){
//                    return new ReturnVO().error(40000,"还没有分享过文章");
//                }else if(data.get("erorr").equals("您还没有创建品牌")){
//                    return new ReturnVO().error(40001,"您还没有创建品牌");
//                }
//            }else{
//                break;
//            }
//
//        }

        return new ReturnVO().error(40000,"还没发开该功能");
    }

    /**
     * 按照video_id查询视频信息,openid查询用户信息
     */
    @ResponseBody
    @PostMapping("/videoInfo")
    public ReturnVO<Map<String,Object>> videoInfo(@RequestBody Map<String,Object> video){
        return new ReturnVO(videoService.videoInfo(video));
    }

    /**
     * 视频点赞
     */
    @ResponseBody
    @PostMapping("/videoLike")
    public ReturnVO<Integer> videoLike(@RequestBody Map<String,Object> data){
        Integer a = videoService.videoLike(data);
        return new ReturnVO(a);
    }









    /**
     * 视频上传
     */
    @RequestMapping("/addvideo")
    public ReturnVO<Map<String,Object>> addimg(@RequestParam(value = "file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){

        Map<String,Object> map = new HashMap<>();
        File targetFile=null;
        //返回图片路径
        String url="";
        System.out.println(file);
        //获取文件名加后缀
        String fileName=file.getOriginalFilename();

        if(fileName!=null&&fileName!=""){

            String path = "F:\\tupian";

            //文件后缀
            String fileF = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            if(!fileF.equals("mp4")){
                return new ReturnVO().error(40000,"请上传mp4格式的视频文件");
            }

            //新的文件名
            fileName=new Date().getTime()+"_"+new Random().nextInt(1000)+fileF;

            //先判断文件是否存在
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String fileAdd = sdf.format(new Date());

            //获取文件夹路径
            File file1 =new File(path+"/"+fileAdd);

            //如果文件夹不存在则创建
            if(!file1 .exists()  && !file1 .isDirectory()){
                file1 .mkdir();
            }

            targetFile = new File(file1, fileName);
            try {
                //将上传的文件写到服务器上指定的文件。
                file.transferTo(targetFile);
                url=path+fileAdd+"/"+fileName;
                map.put("url", url);
                return new ReturnVO(map);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return new ReturnVO(map);
    }
}
