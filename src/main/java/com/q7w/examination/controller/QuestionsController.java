package com.q7w.examination.controller;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.q7w.examination.Service.QuestionsService;
import com.q7w.examination.entity.Questions;
import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import com.q7w.examination.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "问题服务接口")
@RequestMapping("/api/question")
@Slf4j
public class QuestionsController {
    @Autowired
    QuestionsService questionsService;

    @GetMapping("/alllist")
    @ApiOperation("获取所有问题")
    public ResponseData getAllList(){
        return new ResponseData(ExceptionMsg.SUCCESS,questionsService.list());
    }
    @GetMapping("/getlistbycid")
    @ApiOperation("根据课程代码获取问题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "课程代码", value = "cid", defaultValue = "1", required = true)
    }
    )
    public ResponseData getListBycid(int cid){
        return new ResponseData(ExceptionMsg.SUCCESS,questionsService.listbycourse(cid));
    }
    @PostMapping("/addquestion")
    @ApiOperation("新增问题问题")
    public ResponseData addquestion(Questions questions){
        int ans= questionsService.addquestion(questions);
        switch (ans){
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS,"题目添加成功，请勿重复操作");
            case 2:
                return new ResponseData(ExceptionMsg.FAILED,"添加失败");
            case 3:
                return new ResponseData(ExceptionMsg.FAILED_F,"参数非法，请从正规接口操作或联系管理员");
        }
        return new ResponseData(ExceptionMsg.FAILED_F,"后端错误");

    }
    @RequestMapping(value="/uploadquestion",method = RequestMethod.POST)
    @CrossOrigin
    @ApiOperation("上传问题excel")
    public ResponseData uploadquestion(int exid,MultipartFile multipartFile){
        return new ResponseData(ExceptionMsg.SUCCESS,"ok");

    }

}
