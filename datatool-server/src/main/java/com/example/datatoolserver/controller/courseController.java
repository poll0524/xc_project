package com.example.datatoolserver.controller;

import com.example.datatoolserver.common.ReturnVO;
import com.example.datatoolserver.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/course")
@RestController
public class courseController {
    @Autowired
    private ICourseService courseService;
    /**
     * 查询课程详情
     */
    @ResponseBody
    @PostMapping("/courseInfo")
    public ReturnVO<Map<String,Object>> courseInfo(@RequestBody Map<String,Object> data){
        return new ReturnVO(courseService.courseInfo(data));
    }



    /**
     * 课时增加阅读数量
     */
    @ResponseBody
    @PostMapping("/updataHour")
    public ReturnVO<String> updataHour(@RequestBody Map<String,Object> data){
        return new ReturnVO(courseService.updataHour(data));
    }

    /**
     * 收藏课程
     */
    @ResponseBody
    @PostMapping("/insertCollect")
    public ReturnVO<Map<String,Object>> insertCollect(@RequestBody Map<String,Object> data){
        return new ReturnVO(courseService.insertCollect(data));
    }

    /**
     * 查询收藏课程列表
     */
    @ResponseBody
    @PostMapping("/selectCollectList")
    public ReturnVO<List<Map<String,Object>>> selectCollectList(@RequestBody Map<String,Object> data){
        List<Map<String,Object>> a = courseService.selectCollectList(data);
        if (a == null){
            return new ReturnVO().error(40000,"你还没有收藏过任何课程哟!!!!");
        }
        return new ReturnVO(a);
    }

    /**
     * 取消收藏
     */
    @ResponseBody
    @PostMapping("deleteCollect")
    public ReturnVO<Map<String,Object>> deleteCollect(@RequestBody Map<String,Object> data){
        return new ReturnVO(courseService.deleteCollect(data));
    }

    /**
     * 写入课程观看历史
     */
    @ResponseBody
    @PostMapping("/insertRecord")
    public ReturnVO<String> insertRecord(@RequestBody Map<String,Object> data){
        String a = courseService.insertRecord(data);
        if (a == null){
            return new ReturnVO().error(40000,"您还没有购买该课程");
        }
        return new ReturnVO(a);
    }

    /**
     * 查询课程观看历史
     */
    @ResponseBody
    @PostMapping("/selectRecord")
    public ReturnVO<List<Map<String,Object>>> selectRecord(@RequestBody Map<String,Object> data){
        List<Map<String,Object>> a = courseService.selectRecord(data);
        if (a == null){
            return new ReturnVO().error(40000,"没有观看历史记录");
        }
        return new ReturnVO(a);
    }

    /**
     * 查询课程购买记录
     */
    @ResponseBody
    @PostMapping("/selectCourseBuy")
    public ReturnVO<List<Map<String,Object>>> selectCourseBuy(@RequestBody Map<String,Object> data){
        List<Map<String,Object>> a = courseService.selectCourseBuy(data);
        if (a == null){
            return new ReturnVO().error(40000,"没有购买历史记录");
        }
        return new ReturnVO(a);
    }


    /**
     * 查询上传记录
     */
    @ResponseBody
    @PostMapping("/uploadingCourser")
    public ReturnVO<List<Map<String,Object>>> uploadingCourser(@RequestBody Map<String,Object> data){
        List<Map<String,Object>> a = courseService.uploadingCourser(data);
        if (a == null){
            return new ReturnVO().error(40000,"为空");
        }
        return new ReturnVO(a);
    }


    /**
     * 写入分享视频
     */
    @ResponseBody
    @PostMapping("/insertShareCourse")
    public ReturnVO<String> insertShareCourse(@RequestBody Map<String,Object> data){
        return new ReturnVO(courseService.insertShareCourse(data));
    }

}
