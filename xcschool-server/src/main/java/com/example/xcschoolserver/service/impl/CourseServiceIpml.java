package com.example.xcschoolserver.service.impl;

import com.example.xcschoolserver.mapper.*;
import com.example.xcschoolserver.pojo.*;
import com.example.xcschoolserver.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CourseServiceIpml implements ICourseService {
    @Autowired
    private courseMapper courseMapper;
    @Autowired
    private courseHourMapper courseHourMapper;
    @Autowired
    private xcuserMapper xcuserMapper;
    @Autowired
    private articleClassifyMapper articleClassifyMapper;
    @Autowired
    private courseCollectMapper courseCollectMapper;
    @Autowired
    private courseRecordMapper courseRecordMapper;
    @Autowired
    private brandCourseMapper brandCourseMapper;
    @Autowired
    private brandCourseHourMapper brandCourseHourMapper;
    @Autowired
    private courseBuyMapper courseBuyMapper;
    @Autowired
    private shareCourseMapper shareCourseMapper;
    @Autowired
    private brandNavMapper brandNavMapper;




    /**
     * 查询课程详情
     */
    public Map<String,Object> courseInfo(Map<String,Object> data){
        String openId = (String) data.get("openId");
        Integer id = Integer.parseInt((String) data.get("id"));
//        Integer start = Integer.parseInt((String) data.get("start"));
        Integer sign = Integer.parseInt((String) data.get("start"));
        Integer tell = Integer.parseInt((String) data.get("tell"));


        //查询用户信息
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);

        //创建map用户返回数据
        Map<String,Object> datas = new HashMap<>();

        if (sign == 0 || sign == null){//平台课程
            //阅读量+1
            courseMapper.updataQuantity(id);
            //查询课程及课时
            //课程
            Course course = courseMapper.selectCourseId(id);
            //根据课程查询课时
            List<CourseHour> courseHour = courseHourMapper.selectCourseId(id);

            //查询是否收藏
            CourseCollect courseCollect = courseCollectMapper.selectCollect(openId,id,0,tell);

            //整理返回数据
            //整理课程数据
            Map<String,Object> courseInfo = new HashMap<>();
            courseInfo.put("id",course.getCourse_id());
            courseInfo.put("title",course.getTitle());
            courseInfo.put("author",course.getAuthor());
            courseInfo.put("digest",course.getDigest());
            courseInfo.put("coverImg",course.getCover_img());
            courseInfo.put("quantity",course.getQuantity());
            courseInfo.put("classify",course.getClassify());
            courseInfo.put("publishTime",course.getPublish_time());
            courseInfo.put("tell",course.getTell());

            if (courseCollect == null){
                courseInfo.put("collectId",null);
            }else {
                courseInfo.put("collectId",courseCollect.getCollect_id());
            }
            if (courseCollect == null){
                courseInfo.put("collect",false);
            }else {
                courseInfo.put("collect",true);
            }
            Double a = 0.00;
            List<Integer> b = new ArrayList<>();
            //整理课时数据
            List<Map<String,Object>> courseHourList = new ArrayList<>();
            for (CourseHour hour : courseHour) {
                Map<String,Object> hourInfo = new HashMap<>();
                hourInfo.put("hourId",hour.getHour_id());
                hourInfo.put("title",hour.getTitle());
                hourInfo.put("downUrl",hour.getDown_url());
                hourInfo.put("hourSort",hour.getHour_sort());
                hourInfo.put("hourQuantity",hour.getHour_quantity());
                //判断是否购买
                //查询购买表是否有该课时数据
                CourseBuy courseBuy = courseBuyMapper.selectCourseBuys(openId,course.getCourse_id(),hour.getHour_id(),0,tell);
                if (courseBuy == null){
                    if (hour.getPrice() == 0.00){
                        hourInfo.put("buy",false);
                        b.add(1);
                        hourInfo.put("price",0.00);
                    }else {
                        hourInfo.put("buy",false);
                        b.add(1);
                        hourInfo.put("price",hour.getPrice());
                    }
                }else {
                    hourInfo.put("buy",true);
                    b.add(0);
                    hourInfo.put("price",hour.getPrice());
                    a = a + hour.getPrice();
                }
                courseHourList.add(hourInfo);
            }

            if (course.getPrice() == 0){
                courseInfo.put("price",course.getPrice() - a);
            }else if (course.getPrice() - a == 0){
                courseInfo.put("price",0.00);
            }else if (course.getPrice() - a != 0){
                courseInfo.put("price",course.getPrice() - a);
            }
            HashSet h = new HashSet(b);
            b.clear();
            b.addAll(h);
            if (b.contains(0) && b.contains(1)){
                courseInfo.put("course",false);
            }else if (b.contains(0)){
                courseInfo.put("course",true);
            }else if (b.contains(1)){
                courseInfo.put("course",false);
            }


            courseInfo.put("courseHour",courseHourList);
            datas.put("dataInfo",courseInfo);
            datas.put("userInfo",xcuser);
            datas.put("start",0);
        }else if (sign == 1){
            //阅读量+1
            brandCourseMapper.updataQuantity(id);

            //查询品牌课程
            BrandCourse course = brandCourseMapper.selectCourseId(id);

            //根据课程查询课时
            List<BrandCourseHour> courseHour = brandCourseHourMapper.selectHour(id);

            //查询是否收藏
            CourseCollect courseCollect = courseCollectMapper.selectCollect(openId,id,1,tell);

            //整理返回数据
            //整理课程数据
            Map<String,Object> courseInfo = new HashMap<>();
            courseInfo.put("id",course.getCourse_id());
            courseInfo.put("title",course.getTitle());
            courseInfo.put("author",course.getAuthor());
            courseInfo.put("digest",course.getDigest());
            courseInfo.put("coverImg",course.getCover_img());
            courseInfo.put("quantity",course.getQuantity());
            courseInfo.put("classify",course.getClassify());
            courseInfo.put("publishTime",course.getPublish_time());
            courseInfo.put("tell",course.getTell());
            if (courseCollect == null){
                courseInfo.put("collectId",null);
            }else {
                courseInfo.put("collectId",courseCollect.getCollect_id());
            }
            if (courseCollect == null){
                courseInfo.put("collect",false);
            }else {
                courseInfo.put("collect",true);
            }
            Double a = 0.00;
            List<Integer> b = new ArrayList<>();
            //整理课时数据
            List<Map<String,Object>> courseHourList = new ArrayList<>();
            for (BrandCourseHour hour : courseHour) {
                Map<String,Object> hourInfo = new HashMap<>();
                hourInfo.put("hourId",hour.getHour_id());
                hourInfo.put("title",hour.getTitle());
                hourInfo.put("downUrl",hour.getDown_url());
                hourInfo.put("hourSort",hour.getHour_sort());
                hourInfo.put("hourQuantity",hour.getHour_quantity());
                //判断是否购买
                //查询购买表是否有该课时数据
                CourseBuy courseBuy = courseBuyMapper.selectCourseBuys(openId,course.getCourse_id(),hour.getHour_id(),1,tell);
                if (courseBuy == null){
                    if (hour.getPrice() == 0.00){
                        hourInfo.put("buy",false);
                        b.add(1);
                        hourInfo.put("price",0.00);
                    }else {
                        hourInfo.put("buy",false);
                        b.add(1);
                        hourInfo.put("price",hour.getPrice());
                    }
                }else {
                    hourInfo.put("buy",true);
                    b.add(0);
                    a = a + hour.getPrice();
                    hourInfo.put("price",hour.getPrice());
                }
                courseHourList.add(hourInfo);
            }

            if (course.getPrice() == 0.00){
                courseInfo.put("price",course.getPrice() - a);
            }else if (course.getPrice() - a == 0){
                courseInfo.put("price",0.00);
            }else if (course.getPrice() - a != 0){
                courseInfo.put("price",course.getPrice() - a);
            }


            HashSet h = new HashSet(b);
            b.clear();
            b.addAll(h);
            if (b.contains(0) && b.contains(1)){
                courseInfo.put("course",false);
            }else if (b.contains(0)){
                courseInfo.put("course",true);
            }else if (b.contains(1)){
                courseInfo.put("course",false);
            }


            courseInfo.put("courseHour",courseHourList);
            datas.put("dataInfo",courseInfo);
            datas.put("userInfo",xcuser);
            datas.put("start",1);
        }

        return datas;
    }




    /**
     * 课时增加阅读数量
     */
    public String updataHour(Map<String,Object> data){
        Integer hourId = (Integer) data.get("hourId");
        courseHourMapper.updataCourseHour(hourId);
        return "成功";
    }

    /**
     * 收藏课程
     */
    public Map<String,Object> insertCollect(Map<String,Object> data){
        //获取用户唯一标识
        String openId = (String) data.get("openId");
        //获取课程主键id
        Integer courseId = (Integer) data.get("courseId");
        //区分是课程还是音频
        Integer tell = Integer.parseInt((String) data.get("tell"));
        //区分平台课程还是品牌课程:0为平台课程,1为品牌课程
        Integer start = Integer.parseInt((String) data.get("start"));
        Integer sign = Integer.parseInt((String) data.get("sign"));
        Integer collectId = (Integer) data.get("collectId");
        Map<String,Object> datas = new HashMap<>();
        if (sign == 0){// 收藏
            //将课程写入课程收藏表中
            courseCollectMapper.insertCourseCollect(openId,courseId,start,tell);
            CourseCollect courseCollect = courseCollectMapper.selectCollect(openId,courseId,start,tell);
            datas.put("collectId",courseCollect.getCollect_id());
        }else if (sign == 1){
            courseCollectMapper.deleteCollect(collectId);
            datas.put("collectId",null);
        }
        return datas;
    }

    /**
     * 查询课程收藏列表
     */
    public List<Map<String,Object>> selectCollectList(Map<String,Object> data){
        //获取用户唯一标识
        String openId = (String) data.get("openId");
        Integer pag = Integer.parseInt((String) data.get("pag"))*10;
        //根据用户唯一标识查询用户收藏的课程
        List<CourseCollect> courseCollects = courseCollectMapper.selectCourseCollectList(openId,pag);
        if (courseCollects.size() == 0){
            return null;
        }
        Xcuser xcuser = xcuserMapper.selectOpenId(openId);
        //整理数据
        List<Map<String,Object>> dataList = new ArrayList<>();
        for (CourseCollect courseCollect : courseCollects) {
            Map<String,Object> datas = new HashMap<>();
            //判断当前课程属于平台还是品牌
            if (courseCollect.getCourse_id() == 0){//当前课程数据平台时
                //根据课程主键id到平台课程表进行查询
                Course course = courseMapper.selectCourseId(courseCollect.getCourse_id());
                datas.put("id",course.getCourse_id());
                datas.put("title",course.getTitle());
                datas.put("coverImg",course.getCover_img());

                //查询观看记录
                CourseRecord courseRecord = courseRecordMapper.selectCourseRecord(openId,course.getCourse_id(),courseCollect.getStart(),courseCollect.getTell());
                if (courseRecord == null){
                    datas.put("record",0);
                }else {
                    datas.put("record",courseRecord.getRecord());
                }
                //查询课程价格
                datas.put("price",course.getPrice());
                datas.put("start",0);
            }else {
                BrandCourse course = brandCourseMapper.selectCourseId(courseCollect.getCourse_id());
                datas.put("id",course.getCourse_id());
                datas.put("title",course.getTitle());
                datas.put("coverImg",course.getCover_img());
                //查询观看记录
                CourseRecord courseRecord = courseRecordMapper.selectCourseRecord(openId,course.getCourse_id(),courseCollect.getStart(),courseCollect.getTell());
                if (courseRecord == null){
                    datas.put("record",0);
                }else {
                    datas.put("record",courseRecord.getRecord());
                }

                //查询课程价格
                datas.put("price",course.getPrice());
                datas.put("start",1);
            }
            datas.put("tell",courseCollect.getTell());
            dataList.add(datas);
        }
        return dataList;
    }

    /**
     * 取消课程收藏
     */
    public Map<String,Object> deleteCollect(Map<String,Object> data){
        Integer collectId = (Integer) data.get("collectId");
        courseCollectMapper.deleteCollect(collectId);

        return courseInfo(data);
    }

    /**
     * 写入课程观看历史
     */
    public String insertRecord(Map<String,Object> data){
        //获取用户唯一标识
        String openId = (String) data.get("openId");
        //获取课程主键id
        Integer id = (Integer) data.get("id");
        //获取课时id
        Integer hourId = (Integer) data.get("hourId");
        //获取课时排序
        Integer hourSort = (Integer) data.get("hourSort");
        //获取平台课程还是品牌课程标识
        Integer start = Integer.parseInt((String) data.get("start"));

        //0为课程1为音频
        Integer tell = Integer.parseInt((String) data.get("tell"));

        //根据openid,courseid,start到数据库查询是否购买该课程
        CourseBuy courseBuy = courseBuyMapper.selectCourseBuys(openId,id,hourId,start,tell);
        //判断该用户是否购买了该课程
        if (courseBuy != null){//该用户购买了该课程
            //根据openid,courseid,start到数据查询是否有该观看记录
            CourseRecord courseRecord = courseRecordMapper.selectCourseRecord(openId,id,start,tell);
            if (courseRecord == null){//没有该观看记录
                //将观看记录写入数据库
                courseRecordMapper.insertcourseRecord(openId,id,start,hourSort,tell);
            }else {//已经有该观看记录
                //将该观看记录更新到数据库
                courseRecordMapper.updatacourseRecord(openId,id,start,hourSort);
            }
        }else {//用户没有购买该课程
            //查询该课程是否免费
            Double price = 0.00;
            if (start == 0){
                price = courseHourMapper.selectCourse(hourId).getPrice();
            }else {
                price = brandCourseHourMapper.selectHourInfo(hourId).getPrice();
            }

            if (price == 0.00){//免费课程
                //根据openid,courseid,start到数据查询是否有该观看记录
                CourseRecord courseRecord = courseRecordMapper.selectCourseRecord(openId,id,start,tell);
                if (courseRecord == null){//没有该观看记录
                    //将观看记录写入数据库
                    courseRecordMapper.insertcourseRecord(openId,id,start,hourSort,tell);
                }else {//已经有该观看记录
                    //将该观看记录更新到数据库
                    courseRecordMapper.updatacourseRecord(openId,id,start,hourSort);
                }
            }else {//收费课程
                return null;
            }
        }
        return "观看记录成功";

    }

    /**
     * 查询历史观看记录
     */
    public List<Map<String,Object>> selectRecord(Map<String,Object> data){
        //获取openId
        String openId = (String) data.get("openId");
        Integer pag = Integer.parseInt((String) data.get("pag"))*10;
        //根据openid到数据库查询所有的历史记录
        List<CourseRecord> courseRecords = courseRecordMapper.selectRecordOpenId(openId,pag);
        if (courseRecords.size() == 0){
            return null;
        }

        //整理数据
        List<Map<String,Object>> dataList = new ArrayList<>();
        for (CourseRecord courseRecord : courseRecords) {
            Map<String,Object> map = new HashMap<>();
            //根据主键id查询课程
            if (courseRecord.getStart() == 0){
                Course course = courseMapper.selectCourseId(courseRecord.getCourse_id());
                map.put("start",0);
                map.put("id",course.getCourse_id());
                map.put("title",course.getTitle());
                map.put("coverImg",course.getCover_img());
                map.put("price",course.getPrice());
                //是否已经收藏
                CourseCollect courseCollect = courseCollectMapper.selectCollect(openId,course.getCourse_id(),0,courseRecord.getTell());
                if (courseCollect == null){
                    map.put("collect",false);
                }else {
                    map.put("collect",true);
                }
                //观看记录
                map.put("record",courseRecord.getRecord());
            }else {
                BrandCourse course = brandCourseMapper.selectCourseId(courseRecord.getCourse_id());
                map.put("start",1);
                map.put("id",course.getCourse_id());
                map.put("title",course.getTitle());
                map.put("coverImg",course.getCover_img());
                map.put("price",course.getPrice());
                //是否已经收藏
                CourseCollect courseCollect = courseCollectMapper.selectCollect(openId,course.getCourse_id(),1,courseRecord.getTell());
                if (courseCollect == null){
                    map.put("collect",false);
                }else {
                    map.put("collect",true);
                }
                //观看记录
                map.put("record",courseRecord.getRecord());
            }
            map.put("tell",courseRecord.getTell());
            dataList.add(map);
        }
        return dataList;
    }

    /**
     * 查询购买课程记录
     */
    public List<Map<String,Object>> selectCourseBuy(Map<String,Object> data){
        //获取openId
        String openId = (String) data.get("openId");
        Integer pag = Integer.parseInt((String) data.get("pag"))*10;

        //根据openid查询购买课程记录
        List<CourseBuy> courseBuys = courseBuyMapper.selectBuyOpenId(openId,pag);
        if (courseBuys.size() == 0){
            return null;
        }

        //整理数据
        List<Map<String,Object>> dataList = new ArrayList<>();
        for (CourseBuy courseBuy : courseBuys) {
            Map<String,Object> map = new HashMap<>();
            if (courseBuy.getStart() == 0){
                Course course = courseMapper.selectCourseId(courseBuy.getCourse_id());
                CourseHour courseHour = courseHourMapper.selectCourse(courseBuy.getCourse_hour_id());
                map.put("start",0);
                map.put("id",course.getCourse_id());
                map.put("hour_id",courseHour.getHour_id());
                map.put("title",courseHour.getTitle());
                map.put("coverImg",course.getCover_img());
                map.put("price",course.getPrice());
                map.put("buyTime",courseBuy.getBuy_time().split("\\s+")[0]);

            }else {
                BrandCourse course = brandCourseMapper.selectCourseId(courseBuy.getCourse_id());
                BrandCourseHour courseHour = brandCourseHourMapper.selectHourInfo(courseBuy.getCourse_hour_id());
                map.put("start",1);
                map.put("id",course.getCourse_id());
                map.put("hour_id",courseHour.getHour_id());
                map.put("title",courseHour.getTitle());
                map.put("coverImg",course.getCover_img());
                map.put("price",course.getPrice());

                map.put("buyTime",courseBuy.getBuy_time().split("\\s+")[0]);
            }
            map.put("tell",courseBuy.getTell());
            dataList.add(map);
        }
        return dataList;
    }

    /**
     * 写入分享课程
     */
    public String insertShareCourse(Map<String,Object> data){
        //判断该课程属于品牌还是属于品牌:0为平台,1为品牌
        Integer start = Integer.parseInt((String) data.get("start"));
        //课程主键id
        Integer id = (Integer) data.get("id");
        //用户唯一标识
        String openId = (String) data.get("openId");

        ShareCourse shareCourse = shareCourseMapper.selectShareCourse(openId,id,start);
        if (shareCourse == null){
            //将分享课程写入数据库
            shareCourseMapper.insertShareCourse(openId,id,start);
        }


        return "分享成功";
    }


    /**
     * 查询上传记录
     */
    public List<Map<String, Object>> uploadingCourser(Map<String,Object> data){
        String openId = (String) data.get("openId");
        Integer pag = Integer.parseInt((String) data.get("pag"))*10;
        //查询课程
        List<BrandCourse> brandCourses = brandCourseMapper.selectOpenId(openId,pag);
        if (brandCourses.size() == 0){
            return null;
        }
        List<Map<String,Object>> brandCourseList = new ArrayList<>();
        for (BrandCourse brandCours : brandCourses) {
            Map<String,Object> course = new HashMap<>();
            course.put("id",brandCours.getCourse_id());
            course.put("title",brandCours.getTitle());
            course.put("price",brandCours.getPrice());
            BrandNav brandNav = brandNavMapper.selectBrandId(brandCours.getBrand_id());
            if (brandNav == null){
                course.put("brandName","暂无品牌");
            }else {
                course.put("brandName",brandNav.getBrand_name());
            }
            course.put("classify",brandCours.getClassify());
            List<BrandCourseHour> brandCourseHour = brandCourseHourMapper.selectHour(brandCours.getCourse_id());
            course.put("hourQuantity",brandCourseHour.size());
            course.put("time",brandCours.getPublish_time().split("\\s+")[0]);
            course.put("start",1);
            course.put("tell",brandCours.getTell());
            brandCourseList.add(course);
        }
        return brandCourseList;
    }
}
