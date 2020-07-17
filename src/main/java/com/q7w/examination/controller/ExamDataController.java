package com.q7w.examination.controller;

import com.q7w.examination.Service.ExamDataService;
import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaogu
 * @date 2020/7/17 18:09
 **/
@RestController
@Api(tags = "考试记录接口")
@RequestMapping("/api/examdata")
public class ExamDataController {
    @Autowired
    ExamDataService examDataService;
    @GetMapping("/exam")
    @ApiOperation("考场考试记录查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "考场id", value = "kid", defaultValue = "1", required = true)
    })
    public ResponseData querybykid(int kid){
        //逻辑
        return new ResponseData(ExceptionMsg.SUCCESS,examDataService.querydatabykid(kid));
    }
    @GetMapping("/uno")
    @ApiOperation("个人考试记录查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "学号", value = "uno", defaultValue = "0121710", required = true)
    })
    public ResponseData querybyuno(String uno){
        //逻辑
        return new ResponseData(ExceptionMsg.SUCCESS,examDataService.querydatabyuno(uno));
    }
}
