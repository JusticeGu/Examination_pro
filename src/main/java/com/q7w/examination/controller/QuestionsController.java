package com.q7w.examination.controller;

import com.q7w.examination.Service.QuestionsService;
import com.q7w.examination.entity.Questions;
import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
/**
 * @author xiaogu
 * @date 2020/7/15 19:29
 **/
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
    @GetMapping("/typelist")
    @ApiOperation("获取指定类型所有问题")
    public ResponseData gettypeList(int type){
        return new ResponseData(ExceptionMsg.SUCCESS,questionsService.listbytype(type));
    }
    @GetMapping("/listnum")
    @ApiOperation("问题列表(分页)")
    public ResponseData listroombunum(@RequestParam(value = "start",defaultValue = "0")Integer start,
                                      @RequestParam(value = "num",defaultValue = "10")Integer num)
    {
        start = start<0?0:start;
        Sort sort = Sort.by(Sort.Direction.DESC, "qid");
        Pageable pageable = PageRequest.of(start, num, sort);
        Page<Questions> page = questionsService.listqusetionbynum(pageable);
        return new ResponseData(ExceptionMsg.SUCCESS,page);
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
    @GetMapping("/qlistbytypecid")
    @ApiOperation("根据课程代码以及试题类型获取问题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "课程代码", value = "cid", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "题目类型", value = "type", defaultValue = "1", required = true),
    }
    )
    public ResponseData qlistbytypecid(int type,int cid){
        return new ResponseData(ExceptionMsg.SUCCESS,questionsService.listbytypeandcid(type,cid));
    }
    @PostMapping("/addquestion")
    @ApiOperation("新增问题问题")
    public ResponseData addquestion(@RequestBody Questions questions){
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
    public ResponseData uploadquestion(MultipartFile multipartFile){
        int ans = questionsService.uploadquestion(multipartFile);
        switch (ans){
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS,"上传成功");
            case 2:
                return new ResponseData(ExceptionMsg.FAILED,"上传失败");
            case 3:
                return new ResponseData(ExceptionMsg.FAILED_F,"参数非法，请从正规接口操作或联系管理员");
        }
        return new ResponseData(ExceptionMsg.FAILED_F,"后端错误");

    }

    @GetMapping("/questionsRec")
    @ApiOperation("首页题目推荐")
    public  ResponseData getQuestionsRec(){
        return new ResponseData(ExceptionMsg.SUCCESS, questionsService.getQuestionsRec());
    }

}
