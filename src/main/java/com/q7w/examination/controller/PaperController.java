package com.q7w.examination.controller;

import com.q7w.examination.Service.PaperService;
import com.q7w.examination.Service.RedisService;
import com.q7w.examination.entity.Paper;
import com.q7w.examination.entity.Questions;
import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
/**
 * @author xiaogu
 * @date 2020/7/15 19:29
 **/
@RestController
@Api(tags = "试卷服务接口")
@RequestMapping("/api/paper")
public class PaperController {
    @Autowired
    PaperService paperService;
    @Autowired
    RedisService redisService;
    @GetMapping("/queryPaper")
    @ApiOperation("试卷模糊查询")
    @CrossOrigin
    public ResponseData querycourse(String papername){
        if (papername.length()>=10||papername.length()<=3){return new ResponseData(ExceptionMsg.FAILED_F,"请不要输入过长或过短的内容");}
        //逻辑
        return new ResponseData(ExceptionMsg.SUCCESS,paperService.querypaper("%"+papername+"%"));
    }
    @GetMapping("/list")
    @ApiOperation("试卷列表(分页)")
    @CrossOrigin
    public ResponseData listroombunum(@RequestParam(value = "start",defaultValue = "0")Integer start,
                                      @RequestParam(value = "num",defaultValue = "10")Integer num)
    {
        start = start<0?0:start;
        Sort sort = Sort.by(Sort.Direction.DESC, "pid");
        Pageable pageable = PageRequest.of(start, num, sort);
        Page<Paper> page = paperService.listpapersbynum(pageable);
        return new ResponseData(ExceptionMsg.SUCCESS,page);
    }
    @GetMapping("/getpaperquestion")
    @CrossOrigin
    @ApiOperation("获取试卷试题数据教师端")
    public ResponseData getpaperque(int pid){
        if (!paperService.isExist(pid)){
            return new ResponseData(ExceptionMsg.FAILED,"试卷不存在，请重新尝试"); }
        List<Questions> questionsSet = paperService.getPaperQuestionList(pid);
        if(!questionsSet.isEmpty()){
        return new ResponseData(ExceptionMsg.SUCCESS,questionsSet);
        }
        else {
            return new ResponseData(ExceptionMsg.FAILED_F,"试卷获取错误请重新获取");
        }
    }
    @GetMapping("/getpaperinfo")
    @CrossOrigin
    @ApiOperation("获取考卷数据考生端")
    public ResponseData getpaperinfo(int pid){

        if (!paperService.isExist(pid)){
            return new ResponseData(ExceptionMsg.FAILED,"试卷不存在，请重新尝试"); }
        Map questionsSet = paperService.getPaperInfo(pid);
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "考场id", value = "kid", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "试卷id", value = "pid", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "答案列表", value = "map", defaultValue = "[]", required = true),
    })
    public ResponseData postpaper(@PathVariable("kid") int kid,@PathVariable("pid") int pid
           ,HttpServletRequest request,@RequestBody Map map){
        if (!redisService.hasKey("exroom-"+kid)){
            return new ResponseData(ExceptionMsg.FAILED_SUB,"考试提交截止时间已过，现在已停止回收试卷,相关事宜请联系负责老师"); }
        Map status = paperService.submitpaper(kid,pid,request,map);
        switch (status.get("code").toString()) {
            case "0":
                return new ResponseData(ExceptionMsg.FAILED_SUB,"登录信息获取失败，请重试或联系管理员，严禁使用" +
                        "第三方工具进行提交");
            case "200":
                return new ResponseData(ExceptionMsg.SUCCESS_SUBMIT,"提交成功，你的成绩信息为" +
                        status);
            case "2":
                return new ResponseData(ExceptionMsg.FAILED_SUB,"提交失败，请检查数据");
            case "801":
                return new ResponseData(ExceptionMsg.FAILED_SUB,"您的考试信息存在问题、请不要通过非官方渠道参加考试");
        }
        return new ResponseData(ExceptionMsg.FAILED_SUB,"后端错误");
    }
    @PostMapping("/addpaper")
    @CrossOrigin
    @ApiOperation("添加试卷")
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
    @PostMapping("/modipaper")
    @CrossOrigin
    @ApiOperation("修改试卷")
    public ResponseData modipaper(@RequestBody Paper paper){
        int status = paperService.modifyPaper(paper);
        switch (status) {
            case 0:
                return new ResponseData(ExceptionMsg.FAILED,"试卷已存在请勿重复操作");
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS_ER,"修改成功,您可以创建考试绑定此试卷" );
            case 2:
                return new ResponseData(ExceptionMsg.FAILED,"修改失败，请检查数据");
        }
        return new ResponseData(ExceptionMsg.FAILED_F,"后端错误");
    }
    @DeleteMapping("/delpaper")
    @CrossOrigin
    @ApiOperation("删除试卷")
    public ResponseData addeEroom(@RequestParam int pid){
        int status = paperService.delPaper(pid);
        switch (status) {
            case -1:
                return new ResponseData(ExceptionMsg.FAILED,"试卷不存在或已删除请勿重复操作");
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS_ER,"删除成功");
        }
        return new ResponseData(ExceptionMsg.FAILED_F,"后端错误");
    }

}
