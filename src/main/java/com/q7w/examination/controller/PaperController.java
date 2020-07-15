package com.q7w.examination.controller;

import com.q7w.examination.Service.PaperService;
import com.q7w.examination.entity.Exroom;
import com.q7w.examination.entity.Paper;
import com.q7w.examination.entity.Questions;
import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

@RestController
@Api(tags = "试卷服务接口")
@RequestMapping("/api/paper")
public class PaperController {
    @Autowired
    PaperService paperService;

    @GetMapping("/getpaperinfo")
    @CrossOrigin
    @ApiOperation("获取考卷数据")
    public ResponseData getpaperinfo(int pid){
        if (!paperService.isExist(pid)){
            return new ResponseData(ExceptionMsg.FAILED,"试卷不存在，请重新尝试"); }
        Set<Questions> questionsSet = paperService.getPaperList(pid);
        if(!questionsSet.isEmpty()){
        return new ResponseData(ExceptionMsg.SUCCESS,questionsSet);
        }
        else {
            return new ResponseData(ExceptionMsg.FAILED_F,"试卷获取错误请重新获取");
        }
    }
    @PostMapping("/{kid}/{pid}/submit")
    @CrossOrigin
    @ApiOperation("提交答案数据")
    public ResponseData getpaperinfo(@PathVariable("kid") int kid,@PathVariable("pid") int pid
            ,HttpServletRequest request){
        int status = paperService.submitpaper(kid,pid,request);
        switch (status) {
            case 0:
                return new ResponseData(ExceptionMsg.FAILED,"登录信息获取失败，请重试或联系管理员，严禁使用" +
                        "第三方工具进行提交");
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS_ER,"提交成功，" +
                        "成绩会在教师端审核通过后显示，请耐心等候");
            case 2:
                return new ResponseData(ExceptionMsg.FAILED,"提交失败，请检查数据");
        }
        return new ResponseData(ExceptionMsg.FAILED_F,"后端错误");
    }
    @PostMapping("/addpaper")
    @CrossOrigin
    @ApiOperation("提交答案数据")
    public ResponseData addpaper(@RequestBody Paper paper){
        int status = paperService.addqPaper(paper);
        switch (status) {
            case 0:
                return new ResponseData(ExceptionMsg.FAILED,"试卷已存在请勿重复操作");
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS_ER,"提交成功,您可以创建考试绑定此试卷" );
            case 2:
                return new ResponseData(ExceptionMsg.FAILED,"提交失败，请检查数据");
        }
        return new ResponseData(ExceptionMsg.FAILED_F,"后端错误");
    }

}
