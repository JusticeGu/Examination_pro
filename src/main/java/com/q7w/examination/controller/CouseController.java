package com.q7w.examination.controller;

import com.q7w.examination.Service.CourseService;
import com.q7w.examination.entity.Course;
import com.q7w.examination.entity.Questions;
import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
/**
 * @author xiaogu
 * @date 2020/7/15 19:29
 **/
@RestController
@Api(tags = "课程服务接口")
@RequestMapping("/api/course")
public class CouseController {
    @Autowired
    CourseService courseService;
    @GetMapping("/list")
    @ApiOperation("课程列表")
    public ResponseData listroom(){
        //逻辑
        return new ResponseData(ExceptionMsg.SUCCESS,courseService.list());
    }
    @GetMapping("/queryCourse")
    @ApiOperation("课程模糊查询")
    public ResponseData querycourse(String coursename){
        if (coursename.length()>=10||coursename.length()<=3){return new ResponseData(ExceptionMsg.FAILED_F,"请不要输入过长或过短的内容");}
        //逻辑
        return new ResponseData(ExceptionMsg.SUCCESS,courseService.querycourse("%"+coursename+"%"));
    }
    @PostMapping("/addquestion")
    @ApiOperation("新增课程")
    public ResponseData addcourse(@RequestBody Course course){
        int ans = courseService.addCourse(course);
        switch (ans){
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS,"课程添加成功，请勿重复操作");
            case 2:
                return new ResponseData(ExceptionMsg.FAILED,"添加失败");
            case 3:
                return new ResponseData(ExceptionMsg.FAILED_F,"参数非法，请从正规接口操作或联系管理员");
        }
        return new ResponseData(ExceptionMsg.FAILED,"后端村务");
    }
    @DeleteMapping("/delcourse")
    @CrossOrigin
    @ApiOperation("删除课程")
    public ResponseData addeEroom(@RequestParam int cid){
        int status = courseService.delCourse(cid);
        switch (status) {
            case -1:
                return new ResponseData(ExceptionMsg.FAILED,"课程不存在或已删除请勿重复操作");
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS_ER,"删除成功");
        }
        return new ResponseData(ExceptionMsg.FAILED_F,"后端错误");
    }
}
