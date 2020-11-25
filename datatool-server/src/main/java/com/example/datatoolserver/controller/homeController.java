package com.example.datatoolserver.controller;

import com.example.datatoolserver.common.ReturnVO;
import com.example.datatoolserver.mapper.productMapper;
import com.example.datatoolserver.service.IHomeService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class homeController {
    @Autowired
    private IHomeService homeService;
    @Autowired
    private productMapper productMapper;

    /**
     * 查询个人主页
     */
    @ResponseBody
    @PostMapping("/selectHome")
    public ReturnVO<Map<String,Object>> selectHome(@RequestBody Map<String,Object> data){
        return new ReturnVO(homeService.selectHome(data));
    }

    /**
     * 阅读人查询主页
     */
    @ResponseBody
    @PostMapping("/selectReadHome")
    public ReturnVO<Map<String,Object>> selectReadHome(@RequestBody Map<String,Object> data){
        return new ReturnVO(homeService.selectReadHome(data));
    }



    /**
     * 切换主页
     */
    @ResponseBody
    @PostMapping("/cutHome")
    public ReturnVO<Map<String,Object>> cutHome(@RequestBody Map<String,Object> data){
        return new ReturnVO(homeService.cutHome(data));
    }

    /**
     * 修改品牌主页简介
     */
    @ResponseBody
    @PostMapping("/updataInfo")
    public ReturnVO<String> updataInfo(@RequestBody Map<String,Object> data){
        return new ReturnVO(homeService.updataInfo(data));
    }


    /**
     * 添加产品
     */
    @ResponseBody
    @PostMapping("/addProduct")
    public ReturnVO<String> addProduct(@RequestBody Map<String,Object> data){
        String a = homeService.addProduct(data);
        if (a == null){
            return new ReturnVO().error(40000,"添加失败");
        }
        return new ReturnVO(a);
    }


    /**
     * 删除产品
     */
    @ResponseBody
    @PostMapping("/deleteProduct")
    public ReturnVO<String> deleteProduct(@RequestBody Map<String,Object> data){
        Integer productId = (Integer) data.get("productId");
        productMapper.deleteProduct(productId);
        return new ReturnVO("删除成功");
    }

    /**
     * 查询产品列表及详情
     */
    @ResponseBody
    @PostMapping("/selectProduct")
    public ReturnVO<Map<String,Object>> selectProduct(@RequestBody Map<String,Object> data){
        Map<String,Object> a = homeService.selectProduct(data);
        if (a == null){
            return new ReturnVO().error(40000,"还没有产品!!!!");
        }
        return new ReturnVO(a);
    }

    /**
     * 查询打卡海报
     */
    @ResponseBody
    @PostMapping("/selectPoster")
    public ReturnVO<Map<String,Object>> selectPoster(@RequestBody Map<String,Object> data){
        Map<String,Object> a = homeService.selectPoster(data);
        if (a == null){
            return new ReturnVO().error(40000,"查询为空");
        }
        return new ReturnVO(a);
    }


    /**
     * 客服留言
     */
    @ResponseBody
    @PostMapping("/insertLeave")
    public ReturnVO<String> insertLeave(@RequestBody Map<String,Object> data){
        String a = homeService.insertLeave(data);
        if (a == null){
            return new ReturnVO().error(40000,"输入为空");
        }
        return new ReturnVO(a);
    }

    /**
     * 添加素材库文章
     */
    @ResponseBody
    @PostMapping("/addArticle")
    public ReturnVO<String> addArticle (@RequestBody Map<String,Object> data) throws JOSEException {
        String a = homeService.addArticle(data);
        if (a == null){
            return new ReturnVO().error(40000,"视频地址为空");
        }
        return new ReturnVO(a);
    }

    /**
     * 查询素材库文章视频和品牌文章列表
     */
    @ResponseBody
    @PostMapping("/selectArticleList")
    public ReturnVO<Map<String,Object>> selectArticleList(@RequestBody Map<String,Object> data){
        Map<String,Object> a = homeService.selectArticleList(data);
        if (a == null){
            return new ReturnVO().error(40000,"没有啦");
        }
        Integer start = (Integer) a.get("start");
        if (start == 0){
            List<Map<String,Object>> articleList = (List<Map<String, Object>>) a.get("articleList");
            if (articleList.size() == 0){
                return new ReturnVO().error(40000,"没有了");
            }
        }else if (start == 1){
            List<Map<String,Object>> videoList = (List<Map<String, Object>>) a.get("videoList");
            if (videoList.size() == 0){
                return new ReturnVO().error(40000,"没有了");
            }
        }
        return new ReturnVO(a);
    }

    /**
     * 查询素材库文章视频和品牌文章列表
     */
    @ResponseBody
    @PostMapping("/selectArticle")
    public ReturnVO<Map<String,Object>> selectArticle(@RequestBody Map<String,Object> data){
        Map<String,Object> a = homeService.selectArticle(data);
        if (a == null){
            return new ReturnVO().error(40000,"没有啦");
        }
        Integer start = (Integer) a.get("start");
        if (start == 0){
            List<Map<String,Object>> articleList = (List<Map<String, Object>>) a.get("articleList");
            if (articleList.size() == 0){
                return new ReturnVO().error(40000,"没有了");
            }
        }else if (start == 1){
            List<Map<String,Object>> videoList = (List<Map<String, Object>>) a.get("videoList");
            if (videoList.size() == 0){
                return new ReturnVO().error(40000,"没有了");
            }
        }
        return new ReturnVO(a);
    }

    /**
     * 查询素材详情
     */
    @ResponseBody
    @PostMapping("/selectArticleInfo")
    public ReturnVO<Map<String, Object>> selectArticleInfo(@RequestBody Map<String,Object> data){
        return new ReturnVO(homeService.selectArticleInfo(data));
    }

    /**
     * 更新素材库文章视频详情
     */
    @ResponseBody
    @PostMapping("/updataArticleInfo")
    public ReturnVO<String> updataArticleInfo(Map<String,Object> data){
        String a = homeService.updataArticleInfo(data);
        return new ReturnVO(a);
    }

    /**
     * 更新文章视频归属
     */
    @ResponseBody
    @PostMapping("/updataArticle")
    public ReturnVO<String> updataArticle(@RequestBody Map<String,Object> data){
        String a = homeService.updataArticle(data);
        if (a == null){
            return new ReturnVO().error(40003,"权限不够");
        }
        return new ReturnVO(a);
    }


    /**
     * 删除素材库文章和视频
     */
    @ResponseBody
    @PostMapping("/deleteBrand")
    public ReturnVO<String> deleteBrand(@RequestBody Map<String,Object> data){

        return new ReturnVO(homeService.deleteBrand(data));
    }


    /**
     * 写入/更新弹框图片
     */
    @ResponseBody
    @PostMapping("/insertPop")
    public ReturnVO<List<Map<String,Object>>> insertPop(@RequestBody Map<String,Object> data){
        return new ReturnVO(homeService.insertPop(data));
    }

    /**
     * 查询弹框图片列表
     */
    @ResponseBody
    @PostMapping("/selectPop")
    public ReturnVO<List<Map<String,Object>>> selectPop(@RequestBody Map<String,Object> data){
        List<Map<String,Object>> a = homeService.selectPop(data);
        if (a.size() == 0){
            return new ReturnVO().error(40000,"没有图片");
        }
        return new ReturnVO(a);
    }




    /**
     * 删除弹框图片
     */
    @ResponseBody
    @PostMapping("/deletePop")
    public ReturnVO<List<Map<String,Object>>> deletePop(@RequestBody Map<String,Object> data){
        List<Map<String,Object>> a = homeService.deletePop(data);
        if (a.size() == 0){
            return new ReturnVO().error(40000,"没有图片");
        }
        return new ReturnVO(a);
    }


    /**
     * 操作删除oss图片
     */
    @ResponseBody
    @PostMapping("/deleteOss")
    public ReturnVO<String> deleteOss(@RequestBody Map<String,Object> data){
        return new ReturnVO(homeService.deleteOss(data));
    }

    /**
     * H5登录
     */
    @ResponseBody
    @PostMapping("/insertH5")
    public ReturnVO<String> insertH5(@RequestBody Map<String,Object> data){
        String a = homeService.insertH5(data);
        if (a == null){
            return new ReturnVO().error(40000,"写入失败");
        }
        return new ReturnVO(a);
    }
}
