package com.example.datatoolserver.task;

import com.example.datatoolserver.mapper.*;
import com.example.datatoolserver.pojo.*;
import com.example.datatoolserver.service.IWeixinInertfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class Task {
    @Qualifier("xcuser")
    @Autowired
    private IWeixinInertfaceService weixinInertfaceService;
    @Autowired
    private articleMapper articleMapper;
    @Autowired
    private brandArticleMapper brandArticleMapper;
    @Autowired
    private videoMapper videoMapper;
    @Autowired
    private brandVideoMapper brandVideoMapper;
    @Autowired
    private courseMapper courseMapper;
    @Autowired
    private brandCourseMapper brandCourseMapper;
    @Autowired
    private matterDataMapper matterDataMapper;



    /**
     * 签到天数任务执行
     */
//    @Scheduled(cron = "0/10 * * * * *")//20,秒一次
    @Scheduled(cron = "0 0 03 * * ?")//每天3:0:0的时候执行一次
    @Transactional(rollbackFor = Exception.class)
    public void scheduled(){
        System.out.println("运行中");
        List<Xcuser> xcusers = weixinInertfaceService.selectxcuser();
        if (xcusers != null){
            for (Xcuser xcuser : xcusers) {
                if(xcuser.getOpen_id().equals("")){
                    continue;
                }
                //获取签到天数
                int sign = xcuser.getSign()+1;
                weixinInertfaceService.updataxcuser(xcuser.getOpen_id(),sign);
                System.out.println("*****************************************************************");
                System.out.println("更新签到天数执行成功");
                System.out.println("*****************************************************************");
            }
        }
    }


    @Scheduled(cron = "0 0 04 * * ?")//每天3:0:0的时候执行一次
    @Transactional(rollbackFor = Exception.class)
    public void matter(){
        //文章
        //获取平台文章
        List<Article> articles = articleMapper.selectArticleTime();
        //获取品牌文章
        List<BrandArticle> brandArticles = brandArticleMapper.selectBrandArtTime();

        //文章总数
        Integer article = articles.size() + brandArticles.size();

        //视频
        //获取平台视频
        List<Video> videos = videoMapper.selectVideoSum();
        //获取品牌视频
        List<BrandVideo> brandVideos = brandVideoMapper.selectBrandVideoSum();

        //视频总数
        Integer video = videos.size() + brandVideos.size();

        //课程
        //获取平台课程
        List<Course> courses = courseMapper.selectCourseSum();
        //获取品牌课程
        List<BrandCourse> brandCourses = brandCourseMapper.selectBrandCourseSum();

        //课程总数
        Integer course = courses.size() + brandCourses.size();

        //更新到数据库
        matterDataMapper.updataMatterData("article",article);
        matterDataMapper.updataMatterData("video",video);
        matterDataMapper.updataMatterData("course",course);
    }
}
