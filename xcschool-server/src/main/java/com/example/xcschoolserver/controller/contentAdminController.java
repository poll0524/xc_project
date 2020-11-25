package com.example.xcschoolserver.controller;

import com.example.xcschoolserver.common.ReturnVO;
import com.example.xcschoolserver.mapper.*;
import com.example.xcschoolserver.pojo.ArticleClassify;
import com.example.xcschoolserver.service.IContentAdminService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/content")
@RestController
public class contentAdminController {
    @Autowired
    private IContentAdminService contentAdminService;

    @Autowired
    private articleMapper articleMapper;

    @Autowired
    private videoMapper videoMapper;

    @Autowired
    private articleClassifyMapper articleClassifyMapper;

    @Autowired
    private brandArticleMapper brandArticleMapper;

    @Autowired
    private brandVideoMapper brandVideoMapper;


    /**
     * 素材分类
     */
    @ResponseBody
    @PostMapping("/classifyContent")
    public ReturnVO<List<ArticleClassify>> classifyContent(){
        return new ReturnVO(articleClassifyMapper.selectClassifyList());
    }
    /**
     * 品牌素材分类
     */
    @ResponseBody
    @PostMapping("/brandClassify")
    public ReturnVO<List<Map<String,Object>>> brandClassify(){
        return new ReturnVO(contentAdminService.brandClassify());
    }

    /**
     * 文章编辑
     */
    @ResponseBody
    @PostMapping("/insertArticle")
    public ReturnVO<String> insertArticle(@RequestBody Map<String,Object> data, @RequestHeader("userToken") String userToken) throws JOSEException {
        String a = contentAdminService.insertArticle(data,userToken);
        if (a.equals("添加失败")){
            return new ReturnVO().error(40000,a);
        }else if(a == null){
            return new ReturnVO().error(40001,"token过期");
        }
        return new ReturnVO(a);
    }


    /**
     * 视频编辑
     */
    @ResponseBody
    @PostMapping("/insertVideo")
    public ReturnVO<String> insertVideo(@RequestBody Map<String,Object> data, @RequestHeader("userToken") String userToken) throws JOSEException {
        String a = contentAdminService.insertVideo(data,userToken);
        if (a.equals("添加失败")){
            return new ReturnVO().error(40000,a);
        }else if(a == null){
            return new ReturnVO().error(40001,"token过期");
        }
        return new ReturnVO(a);
    }

    /**
     * 更新文章
     */
    @ResponseBody
    @PostMapping("/updataArticle")
    public ReturnVO<String> updataArticle(@RequestBody Map<String,Object> data, @RequestHeader("userToken") String userToken){
        String a = contentAdminService.updataArticle(data,userToken);
        if (a == null){
            return new ReturnVO().error(40001,"token过期");
        }else if (a.equals("更新失败")){
            return new ReturnVO().error(40000,a);
        }
        return new ReturnVO(a);
    }

    /**
     * 更新视频
     */
    @ResponseBody
    @PostMapping("/updataVideo")
    public ReturnVO<String> updataVideo(@RequestBody Map<String,Object> data, @RequestHeader("userToken") String userToken){
        String a = contentAdminService.updataVideo(data,userToken);
        if (a.equals("更新失败")){
            return new ReturnVO().error(40000,a);
        }else if (a == null){
            return new ReturnVO().error(40001,"token过期");
        }
        return new ReturnVO(a);
    }

    /**
     * 素材发布
     */
    @ResponseBody
    @PostMapping("/updataContent")
    public ReturnVO<String> updataContent(@RequestBody Map<String,Object> data, @RequestHeader("userToken") String userToken){
        String a = contentAdminService.updataContent(data,userToken);
        if (a == null){
            return new ReturnVO().error(40001,"token过期");
        }
        return new ReturnVO(a);
    }

    /**
     * 查询未发布素材
     */
    @ResponseBody
    @PostMapping("/selectContent")
    public ReturnVO<Map<String,Object>> selectContent(@RequestBody Map<String,Object> data){
        return new ReturnVO(contentAdminService.selectContent(data));
    }

    /**
     * 查询已发布素材
     */
    @ResponseBody
    @PostMapping("/selectConfirm")
    public ReturnVO<Map<String,Object>> selectConfirm(@RequestBody Map<String,Object> data){
        return new ReturnVO(contentAdminService.selectConfirm(data));
    }

    /**
     * 根据id查询素材详情
     */
    @ResponseBody
    @PostMapping("/contentInfo")
    public ReturnVO<Object> contentInfo(@RequestBody Map<String,Object> data){
        Integer start = Integer.parseInt((String) data.get("start"));
        Integer id = Integer.parseInt((String) data.get("id"));
        Integer tell = Integer.parseInt((String) data.get("tell"));
        if (tell == 0){
            if (start == 0){
                return new ReturnVO(articleMapper.selectArticleId(id));
            }
            return new ReturnVO(videoMapper.selectVideoId(id));
        }else if (tell == 1){
            if(start == 0){
                return new ReturnVO(brandArticleMapper.selectArticleId(id));
            }
            return new ReturnVO(brandVideoMapper.selectVideoIds(id));
        }
        return null;
    }


    /**
     * 根据id删除素材
     */
    @ResponseBody
    @PostMapping("/deleteContent")
    public ReturnVO<String> deleteContent(@RequestBody Map<String,Object> data, @RequestHeader("userToken") String userToken){
        String a = contentAdminService.deleteContent(data,userToken);
        if (a == null){
            return new ReturnVO().error(40001,"token过期");
        }
        return new ReturnVO(a);
    }

    /**
     * 模糊搜索素材
     */
    @ResponseBody
    @PostMapping("/searchContent")
    public ReturnVO<Map<String,Object>> searchContent(@RequestBody Map<String,Object> data, @RequestHeader("userToken") String userToken){
        Map<String,Object> a = contentAdminService.searchContent(data,userToken);
        if (a == null){
            return new ReturnVO().error(40001,"token过期");
        }
        return new ReturnVO(a);
    }


    /**
     * 管理后台登录
     */
    @ResponseBody
    @PostMapping("/adminLogin")
    public ReturnVO<String> selectLogin(@RequestBody Map<String,Object> data) throws JOSEException {
        Map<String,Object> a = contentAdminService.selectLogin(data);
        if (a == null){
            return new ReturnVO().error(40000,"登录失败");
        }
        return new ReturnVO(a);
    }

    /**
     * 管理后台修改密码
     */
    @ResponseBody
    @PostMapping("/updataPwd")
    public ReturnVO<String> updataPwd(@RequestBody Map<String,Object> data, @RequestHeader("userToken") String userToken){
        String a = contentAdminService.updataPwd(data,userToken);
        if (a == null){
            return new ReturnVO().error(40000,"修改失败");
        }else if(a.equals("token错误")){
            return new ReturnVO().error(40001,a);
        }
        return new ReturnVO(a);
    }

    /**
     * 通过token查询登录状态
     */
    @ResponseBody
    @PostMapping("/selectToken")
    public ReturnVO<String> selectToken(@RequestBody Map<String,Object> data){
        String a = contentAdminService.selectToken(data);
        if (a.equals(null)){
            return new ReturnVO().error(40000,"登录失败");
        }
        return new ReturnVO(a);
    }

    /**
     * 上传图片或视频
     */
//    @PostMapping("/addPrImage")
//    @ResponseBody
//    public ReturnVO<String> addPrImage(@RequestParam(name = "image_data", required = false) MultipartFile file) {
//        //创建空file存放文件
//        File targetFile=null;
//        String url = "";
//        String imgUrl = "";
//        //获取文件名
//        String fileName=file.getOriginalFilename();
//        if (!file.isEmpty()) {
//            try {
//                // 路径
////                String newCompanyImagepath = "E:\\tupian";
//                String newCompanyImagepath = "/item/apache-tomcat-7.0.99/webapps/product";
//                //获取文件后缀
//                String fileF = fileName.substring(fileName.lastIndexOf("."), fileName.length());
//                //新的文件名
//                String newCompanyImageName=new Date().getTime()+"_"+new Random().nextInt(1000)+fileF;
//                //先判断文件是否存在
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//                String fileAdd = sdf.format(new Date());
//                //获取文件夹路径
//                File file1 =new File(newCompanyImagepath+"/"+fileAdd);
//                //如果文件夹不存在则创建
//                if(!file1 .exists()  && !file1 .isDirectory()){
//                    file1 .mkdir();
//                }
//
//                targetFile = new File(file1, newCompanyImageName);
//
//
//                if (!targetFile.exists()) {
//                    targetFile.createNewFile();
//                }
//                BufferedOutputStream out = new BufferedOutputStream(
//                        new FileOutputStream(targetFile));
//                out.write(file.getBytes());
//                //文件路径url
////                url=newCompanyImagepath+"\\"+fileAdd+"\\"+newCompanyImageName;
//                url="http://"+"xiaocisw.site:8044/product/"+fileAdd+"/"+newCompanyImageName;
////                imgUrl = "http://xiaocisw.site:8044/userQR/20200306/1583453493441_258.jpg";
//
//                out.flush();
//                out.close();
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                return new ReturnVO().error(40000,"图片上传失败");
//            } catch (IOException e) {
//                e.printStackTrace();
//                return new ReturnVO().error(40000,"图片上传失败");
//            }
//        }
////        System.out.println(url);
//        return new ReturnVO(url);
//    }


    /**
     *上传产品图片(本地)
     */
//    @PostMapping("/addPrImage")
//    @ResponseBody
//    public ReturnVO<String> addPrImage(@RequestParam(name = "image_data", required = false) MultipartFile file) {
//
//        //创建空file存放文件
//        File targetFile=null;
//        String url = "";
//        String imgUrl = "";
//        //获取文件名
//        String fileName=file.getOriginalFilename();
//        if (!file.isEmpty()) {
//            try {
//                // 路径
//                String newCompanyImagepath = "E:\\开发软件\\tomcat\\apache-tomcat-8.5.53\\webapps\\images";
//                //获取文件后缀
//                String fileF = fileName.substring(fileName.lastIndexOf("."), fileName.length());
//                //新的文件名
//                String newCompanyImageName=new Date().getTime()+"_"+new Random().nextInt(1000)+fileF;
//                //先判断文件是否存在
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//                String fileAdd = sdf.format(new Date());
//                //获取文件夹路径
//                File file1 =new File(newCompanyImagepath+"/"+fileAdd);
//                //如果文件夹不存在则创建
//                if(!file1 .exists()  && !file1 .isDirectory()){
//                    file1 .mkdir();
//                }
//
//                targetFile = new File(file1, newCompanyImageName);
//
//
//                if (!targetFile.exists()) {
//                    targetFile.createNewFile();
//                }
//                BufferedOutputStream out = new BufferedOutputStream(
//                        new FileOutputStream(targetFile));
//                out.write(file.getBytes());
//                //文件路径url
//                url="192.168.0.157:8083/images/"+fileAdd+"/"+newCompanyImageName;
//
//                out.flush();
//                out.close();
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                return new ReturnVO().error(40000,"图片上传失败");
//            } catch (IOException e) {
//                e.printStackTrace();
//                return new ReturnVO().error(40000,"图片上传失败");
//            }
//        }
////        System.out.println(url);
//        return new ReturnVO(url);
//    }

    /**
     * 删除图片及视频
     */
    @ResponseBody
    @PostMapping("/deleteImage")
    public ReturnVO<String> deleteImage(@RequestBody Map<String,Object> data){
        return new ReturnVO(contentAdminService.deleteImage(data));
    }


    /**
     * 用户信息数据查询
     */
    @ResponseBody
    @PostMapping("/adminData")
    public ReturnVO<Map<String,Object>> adminData(@RequestHeader("userToken") String userToken){
        return new ReturnVO(contentAdminService.adminData(userToken));
    }
}
