package com.example.datatoolserver.controller;

import com.example.datatoolserver.common.ReturnVO;
import com.example.datatoolserver.common.WeChatUtil;
import com.example.datatoolserver.mapper.xcuserMapper;
import com.example.datatoolserver.pojo.*;
import com.example.datatoolserver.util.CrawlerUtil;
import com.nimbusds.jose.JOSEException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.datatoolserver.mapper.brandNavMapper;
import com.example.datatoolserver.mapper.specialMapper;
import com.example.datatoolserver.service.IArticleService;
import com.example.datatoolserver.util.AccessTokenUtil;

import java.io.UnsupportedEncodingException;
import java.util.*;
@CrossOrigin
@RestController
@RequestMapping("/article")
public class articleController {
    @Autowired
    private brandNavMapper brandNavMapper;
    @Autowired
    private IArticleService articleService;
    @Autowired
    private specialMapper specialMapper;
    @Autowired
    private AccessTokenUtil accessTokenUtil;
    /**
     * 获取jssdk
     */
    @ResponseBody
    @RequestMapping("/jssdk")
    public ReturnVO<Map<String,Object>> jssdk(@RequestBody Map<String,Object> urls) throws JSONException, UnsupportedEncodingException {
        String url = (String) urls.get("url");
        //获取ticket
        String ticket = accessTokenUtil.getTicket();
        //3、时间戳和随机字符串
        String noncestr = UUID.randomUUID().toString().replace("-", "").substring(0, 16);//随机字符串
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳

        String str = "jsapi_ticket="+ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;
//        System.out.println(str);

        //6、将字符串进行sha1加密
        String signature =accessTokenUtil.SHA1(str);



        Map<String,Object> l_data  =new HashMap<>();
        l_data.put("timestamp",timestamp);
        l_data.put("noncestr",noncestr);
        l_data.put("signature",signature);
        l_data.put("url",url);
        l_data.put("appid", WeChatUtil.APPID);
//        l_data.put("appid","wx8ee656699cb2c84d");


        return new ReturnVO(l_data);
    }




    /**
     * 查询所有文章
     * @return
     */

    @ResponseBody
    @GetMapping("/articleList")
    public ReturnVO<List<Article>> selectBillingArticle() throws JSONException {

        return new ReturnVO(articleService.selectArticlelist());
    }

    /**
     * 根据标签/品牌查询素材列表
     */
    @ResponseBody
    @PostMapping("/classifyArticle")
    public ReturnVO<List<Map<String, Object>>> classifyArticle(@RequestBody Map<String,Object> classify){
        List articles = articleService.classifyArticle(classify);

        if (articles == null){
            return new ReturnVO().error(40000,"没有文章");
        }else if (articles.size() == 0){
            return new ReturnVO().error(40000,"没有文章");
        }

        return new ReturnVO(articles);

    }

    /**
     * 修改或分享文章上传
     */
    @ResponseBody
    @PostMapping("/alterArticle")
    public ReturnVO<String> alterArticle(@RequestBody Map<String,Object> article) throws JOSEException {
        return new ReturnVO(articleService.alterArticle(article));
    }

    /**
     * 查询品牌文章
     */
    @ResponseBody
    @PostMapping("/shareArticle")
    public ReturnVO<List<Map<String,Object>>> shareArticle(@RequestBody Map<String,Object> article){
        return new ReturnVO(articleService.selectArticleName(article));
    }

    /**
     * 查询团队文章
     */
    @ResponseBody
    @PostMapping("/GroupList")
    public ReturnVO<Map<String,Object>> GroupList(@RequestBody Map<String,Object> group) {
        Map<String,Object> groupList = articleService.GroupList(group);
        List dataInfo = (List) groupList.get("dataInfo");
        if (dataInfo.size() == 0){

            return new ReturnVO(groupList).error(40000,"xxxxx");
        }
        return new ReturnVO(groupList);
    }



    /**
     * 根据media_id,openId查询文章和用户信息
     */
    @ResponseBody
    @PostMapping("/articleInfo")
    public ReturnVO<Map> articleInfo(@RequestBody Map<String,Object> articleInfo){
        return new ReturnVO(articleService.articleInfo(articleInfo));
    }



    /**
     * 上传图文素材
     */
    @ResponseBody
    @PostMapping("/addArticle")
    public ReturnVO<Map<String,Object>> addArticle(@RequestBody Article articles) throws JSONException {
        return new ReturnVO(articleService.addArticle(articles));
    }




    /**
     * 展示所有分类标签,及选中的标签
     * @returnhttp://poll.mynatapp.cc/article/classifyList
     */
    @ResponseBody
    @GetMapping("/classifyList")
    public ReturnVO<String> classifyList(String openId){
        return new ReturnVO(articleService.classifyList(openId));
    }

    /**
     * 根据opid查询标签/品牌分类
     */
    @ResponseBody
    @PostMapping("/openidClassify")
    public ReturnVO<Map<String,Object>> openidClassify(@RequestBody Map<String,Object> data){
        return new ReturnVO(articleService.openidClassify(data));
    }

    @ResponseBody
    @PostMapping("/openidClassifys")
    public ReturnVO<Map<String,Object>> openidClassifys(@RequestBody Map<String,Object> data){
        return new ReturnVO(articleService.openidClassifys(data));
    }
    /**
     * 修改我的标签/品牌
     * @param obj
     * @return
     */
    @ResponseBody
    @RequestMapping("/updataUserClassify")
    public ReturnVO<Map<String,Object>> updataUserClassify(@RequestBody Map<String,Object> obj){
        return new ReturnVO(articleService.updataUserClassify(obj));
    }

    /**
     * 设置我的品牌
     */
    @ResponseBody
    @PostMapping("/insertBrand")
    public ReturnVO<String> insertBrand(@RequestBody Map<String,Object> brand){
        String openId = (String) brand.get("openId");
        String a = articleService.insertBrand(brand);
        if(a.equals("创建成功")){
            return new ReturnVO(brandNavMapper.selectBrand(openId));
        }else if(a.equals("更新成功")){
            return new ReturnVO(a);
        }else if(a.equals("0")){
            return new ReturnVO().error(40000,"brandName为空!!!");
        }else if(a.equals("1")){
            return new ReturnVO().error(40001,"该品牌已存在");
        }else if(a.equals("2")){
            return new ReturnVO().error(40002,"二级分类有重复");
        }else if(a.equals("3")){
            return new ReturnVO().error(40003,"创建大于3次");
        }else if (a.equals("4")){
            return new ReturnVO().error(40003,"创建大于3次");
        }
        return new ReturnVO(brandNavMapper.selectBrand(openId));
    }

    /**
     * 品牌名字查重复
     */
    @ResponseBody
    @PostMapping("/selectBrandName")
    public ReturnVO<String> selectBrandName(@RequestBody Map<String,Object> data){
        String a = articleService.selectBrandName(data);
        if (a.equals("2")){
            return new ReturnVO().error(40000,"重复");
        }
        return new ReturnVO(a);
    }



    /**
     * 删除品牌
     */
    @ResponseBody
    @PostMapping("/deleteBrand")
    public ReturnVO<List<BrandNav>> deleteBrand(@RequestBody Map<String,Object> data){
        List<BrandNav> a = articleService.deleteBrand(data);
        if (a == null){
            return new ReturnVO().error(40000,"不能修改");
        }
        return new ReturnVO(a);
    }

    /**
     * 查询品牌详情
     */
    @ResponseBody
    @PostMapping("/selectBrandInfo")
    public ReturnVO<Map<String,Object>> selectBrandInfo(@RequestBody Map<String,Object> data){
        return new ReturnVO(articleService.selectBrandInfo(data));
    }

    /**
     * 查询品牌
     */
    @ResponseBody
    @PostMapping("/selectBrand")
    public ReturnVO<String> selectBrand(@RequestBody Map<String,Object> data){
        return new ReturnVO(articleService.selectBrand(data));
    }

    /**
     * 查询品牌列表
     */
    @ResponseBody
    @PostMapping("/selectBrandList")
    public ReturnVO<List<BrandNav>> selectBrandList(@RequestBody Map<String,Object> data){
        return new ReturnVO(articleService.selectBrandList(data));
    }



    /**
     * 查询推荐品牌
     */
    @ResponseBody
    @PostMapping("/selectBrandTop")
    public ReturnVO<List<Map<String,Object>>> selectBrandTop(@RequestBody Map<String,Object> data){
        return new ReturnVO(articleService.selectBrandTop(data));
    }




//
//
//
//
//    /**
//     * 登录
//     * @return
//     */
//    @CrossOrigin
//    @PostMapping("/login")
//    public Map<String,Object> login(@RequestParam("username") String username,@RequestParam("userphone") String userphone){
//        Xcuser userinfo = xcuserMapper.selectPhone(userphone);
////        System.out.println(userinfo);
//        Xcuser data = articleService.login(username, userphone);
//        Map datainfo = new HashMap();
//        datainfo.put("data",data);
//        if(userinfo == null){
//            datainfo.put("state","20000");
//            return datainfo;
//        }else {
//            datainfo.put("state","50000");
//            return datainfo;
//        }
//    }
//    /**
//     * 获取名人名言
//     */
//    @CrossOrigin
//    @PostMapping("/sentence")
//    public String sentence(){
//        return articleService.selectOneSentence();
//    }

    /**
     * 抓取文章
     */
    @ResponseBody
    @PostMapping("/getArticle")
    public ReturnVO<Map> getArticle(@RequestBody Map<String,Object> data){
        String url = (String) data.get("url");
        Integer start = (Integer) data.get("start");
        if(start == 0){
            if (url.indexOf("https://mp.weixin.qq.com") != -1){
                return new ReturnVO(articleService.getArticle(url));
            }
            return new ReturnVO().error(40000,"该文章不属于公众号文章");
        }else if (start == 1){
            if (url.indexOf("抖音") != -1 || url.indexOf("douyin") != -1){
                return new ReturnVO(articleService.getVideo(url));
            }else if (url.indexOf("快手") != -1 || url.indexOf("kuaishou") != -1){
                return new ReturnVO(articleService.getVideo(url));
            }
            return new ReturnVO().error(40001,"该视频不属于视频");
        }

        return null;
    }

    /**
     * 将视频下载存入oss
     * @return
     */
    @ResponseBody
    @PostMapping("/ossVideo")
    public ReturnVO<Map<String,Object>> ossVideo(@RequestBody Map<String,Object> data) throws Exception {
        String title = (String) data.get("title");
        String coverImg = (String) data.get("coverImg");
        String videoUrl = (String) data.get("videoUrl");
        return new ReturnVO(CrawlerUtil.video(videoUrl,coverImg,title));
    }

    @ResponseBody
    @PostMapping("/ossVideos")
    public ReturnVO<Map<String,Object>> ossVideos(@RequestBody Map<String,Object> data) throws Exception {
        String url = (String) data.get("url");
        Map a = articleService.getVideo(url);
        String videoUrl = (String) a.get("videoUrl");
        return new ReturnVO(CrawlerUtil.videos(videoUrl));
    }

    /**
     * 查询专题branner
     * @return
     */
    @ResponseBody
    @PostMapping("/special")
    public ReturnVO<List<Map<String,Object>>> special(){
        //查询branner
        List<Special> specials = specialMapper.selectSpecial();

        if (specials.size() == 0){
            return new ReturnVO().error(40000,"当下没有专题");
        }else {
            List<Map<String,Object>> dataList = new ArrayList<>();
            for (Special special : specials) {
                Map<String,Object> map = new HashMap<>();
                map.put("specialId",special.getSpecial_id());
                map.put("skipUrl",special.getSkip_url());
                map.put("coverImg",special.getCover_img());
                dataList.add(map);
            }
            return new ReturnVO(dataList);
        }
    }


    @ResponseBody
    @PostMapping("/specialList")
    public ReturnVO<Map<String,Object>> specialList(@RequestBody Map<String,Object> data){
        return new ReturnVO(articleService.specialList(data));
    }

    @ResponseBody
    @PostMapping("/a")
    public ReturnVO<Map<String,Object>> a(){
        Double a = 52.00;
        a = a *100;
        System.out.println(a.toString());
        return null;
    }

}
