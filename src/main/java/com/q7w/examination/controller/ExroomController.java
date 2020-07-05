package com.q7w.examination.controller;

import com.q7w.examination.Service.ExroomService;
import com.q7w.examination.Service.RedisService;
import com.q7w.examination.entity.Exroom;
import com.q7w.examination.entity.User;
import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ExroomController implements Serializable {
    @Resource
    private RedisService redisService;
    @Autowired
    ExroomService exroomService;
    @GetMapping("/api/exroom/list")
    public ResponseData listroom(){
        //逻辑
        return new ResponseData(ExceptionMsg.SUCCESS,"0");
    }
    @PostMapping("/api/exroom/enter")
    @CrossOrigin
    public ResponseData enterexroom(@RequestBody Exroom exroom) {
        int status = exroomService.enterExroom(exroom.getKid());
        switch (status) {
            case 0:
                return new ResponseData(ExceptionMsg.FAILED,"考场不存在请重试");
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS_ER,"您已进入考场，请等待系统抽取试卷");
            case 2:
                return new ResponseData(ExceptionMsg.FAILED,"现在不在考场允许进入的时间范围");
            case 3:
                return new ResponseData(ExceptionMsg.FAILED,"您不在此考场的考生列表请联系管理员");
        }
        return new ResponseData(ExceptionMsg.FAILED_F,"后端错误");
    }
}
