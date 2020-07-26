package com.q7w.examination.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.q7w.examination.Service.ExroomService;
import com.q7w.examination.Service.PaperService;
import com.q7w.examination.Service.RedisService;
import com.q7w.examination.entity.Exroom;
import com.q7w.examination.entity.User;
import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import com.q7w.examination.util.FileUtil;
import com.q7w.examination.util.TokenUtil;
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
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.util.*;
/**
 * @author xiaogu
 * @date 2020/7/15 19:29
 **/
@RestController
@Api(tags = "考试服务接口")
@RequestMapping("/api/exroom")
public class ExroomController implements Serializable {
    @Resource
    private RedisService redisService;
    @Autowired
    ExroomService exroomService;
    @Autowired
    PaperService paperService;
    @Autowired
    TokenUtil tokenUtil;
    @GetMapping("/list")
    @ApiOperation("全部考试(场)列表")
    @CrossOrigin
    public ResponseData listroom(){
        //逻辑
        return new ResponseData(ExceptionMsg.SUCCESS,exroomService.listExroom());
    }
    @GetMapping("/listnum")
    @ApiOperation("全部考试(场)列表(分页)")
    @CrossOrigin
    public ResponseData listroombunum(@RequestParam(value = "start",defaultValue = "0")Integer start,
                                      @RequestParam(value = "num",defaultValue = "10")Integer num)
    {
        start = start<0?0:start;
        Sort sort = Sort.by(Sort.Direction.DESC, "kid");
        Pageable pageable = PageRequest.of(start, num, sort);
        Page<Exroom> page = exroomService.listexroombynum(pageable);
        return new ResponseData(ExceptionMsg.SUCCESS,page);
    }
    @GetMapping("/notstartlist")
    @ApiOperation("待开考考试(场)列表")
    @CrossOrigin
    public ResponseData nowlistroom(){
        //逻辑
        return new ResponseData(ExceptionMsg.SUCCESS,exroomService.getNotStartList());
    }

    @GetMapping("/startedlist")
    @ApiOperation("已开始/结束考试(场)列表")
    @CrossOrigin
    public ResponseData pastlistroom(){
        //逻辑
        return new ResponseData(ExceptionMsg.SUCCESS,exroomService.getStartedList());
    }

    @PostMapping("/enter")
    @CrossOrigin
    @ApiOperation("进入考试(场)")
    public ResponseData enterexroom(@RequestBody Exroom exroom,HttpServletRequest request) {
        String username= tokenUtil.getusername(request);
        if (username==null){return new ResponseData(ExceptionMsg.FAILED,"请登陆后再进入考场"); }
        Map status = exroomService.enterExroom(exroom.getKid(),username);
        switch (status.get("code").toString()) {
            case "0":
                return new ResponseData(ExceptionMsg.FAILED,"考场不存在请重试");
            case "1":
                return new ResponseData(ExceptionMsg.SUCCESS_ER,status.get("expiretime"));
            case "2":
                return new ResponseData(ExceptionMsg.FAILED,"现在不在考场允许进入的时间范围");
            case "3":
                return new ResponseData(ExceptionMsg.FAILED,"此考场非公开考场，且您不在此考场的考生范围内，请联系老师处理");
            case "4":
                return new ResponseData(ExceptionMsg.FAILED,"您已参加过次考试且次数超过允许考试次数上限");
            case "5":
                return new ResponseData(ExceptionMsg.FAILED,"为获取到您的学号信息，请先绑定学号后再参加考试");
        }
        return new ResponseData(ExceptionMsg.FAILED_F,"后端错误");
    }
    @PostMapping("/{kid}/{pid}/submit")
    @CrossOrigin
    @ApiOperation("提交试卷")

    public ResponseData submitdata(@PathVariable("kid") int kid, @PathVariable("pid") int pid
            , HttpServletRequest request, @RequestBody Map map){
        Map status = paperService.submitpaper(kid,pid,request,map);
        if (!redisService.hasKey("exroom-"+kid)){
            return new ResponseData(ExceptionMsg.FAILED,"考试截止时间已过，现在已停止提交"); }
        switch (status.get("code").toString()) {
            case "0":
                return new ResponseData(ExceptionMsg.FAILED,"登录信息获取失败，请重试或联系管理员，严禁使用" +
                        "第三方工具进行提交");
            case "200":
                return new ResponseData(ExceptionMsg.SUCCESS_ER,"提交成功，你的成绩信息为" +
                        status);
            case "2":
                return new ResponseData(ExceptionMsg.FAILED,"提交失败，请检查数据");
        }
        return new ResponseData(ExceptionMsg.FAILED_F,"后端错误");
    }
    @PostMapping("/addexroom")
    @CrossOrigin
    @ApiOperation("创建考试(场)")
    public ResponseData addeEroom(@RequestBody Exroom exroom){
        int status = exroomService.addExroom(exroom);
        switch (status) {
            case 0:
                return new ResponseData(ExceptionMsg.FAILED,"考场已存在请不要重复提交");
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS_ER,"创建成功");
            case 2:
                return new ResponseData(ExceptionMsg.FAILED,"创建失败，请检查数据");
        }
        return new ResponseData(ExceptionMsg.FAILED_F,"后端错误");
    }
    @PutMapping("/putexroom")
    @CrossOrigin
    @ApiOperation("修改考试(场)信息")
    public ResponseData modifyeEroom(@RequestBody Exroom exroom){
        int status = exroomService.modifyExroom(exroom);
        switch (status) {
            case 0:
                return new ResponseData(ExceptionMsg.FAILED,"请不要添加和存量考场相同的考场安排");
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS_ER,"修改成功");
            case 2:
                return new ResponseData(ExceptionMsg.FAILED,"修改失败，请检查数据");
        }
        return new ResponseData(ExceptionMsg.FAILED_F,"后端错误");
    }
    @PostMapping("putsingleper")
    @CrossOrigin
    @ApiOperation("考场添加单个考生许可")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "考场id", value = "exid", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "考生学号", value = "uno", defaultValue = "0121710", required = true)
    }
    )
    public ResponseData pubsinglepermission(String exid,String uno){
        boolean ans = exroomService.putpermission(exid, uno);
        if (ans){
            return new ResponseData(ExceptionMsg.SUCCESS,"添加成功");
        }else {
            return new ResponseData(ExceptionMsg.FAILED,"添加失败，考生可能已在列表");
        }

    }
    @RequestMapping(value="/permissionlist",method = RequestMethod.POST)
    @CrossOrigin
    @ApiOperation("考场添加考生列表From:excel")
    public ResponseData setpermissionlistfile(int exid,MultipartFile multipartFile){
        File file = FileUtil.toFile(multipartFile);
        // 读取 Excel 中的数据
        ExcelReader reader = ExcelUtil.getReader(file);
        List<Map<String,Object>>readAll =reader.readAll();
        List<Object> unolist = new ArrayList<>();
        for(int i=0;i<=reader.getRowCount()-1;i++){
            String uno = readAll.get(i).get("学号").toString();
            unolist.add(uno);
            exroomService.putpermission(String.valueOf(exid), uno);
        }
        Object name =reader.readCellValue(1, 4);
        return new ResponseData(ExceptionMsg.SUCCESS,"添加成功");
    }
    @GetMapping("/getpeset")
    @ApiOperation("根据考场号获取该考场允许进入的考生名单")
    public ResponseData getpermissionset(String exid){
        //逻辑
         Set<Object> list = redisService.setMembers(exid);
        //   Set<String> set_old = new HashSet<String>();
        return new ResponseData(ExceptionMsg.SUCCESS,list);
    }
    @GetMapping("/checkpeset")
    @ApiOperation("外部核验许可")
    public ResponseData checkpermissionset(String exid,String uno){
        //逻辑
       if (exroomService.checkpermission(exid, uno)){
        return new ResponseData(ExceptionMsg.SUCCESS,"验证成功，考生在此考场考试范围");}
       else {
           return new ResponseData(ExceptionMsg.FAILED_V,"考生不允许参与此考试");
       }
    }
    @DeleteMapping("/delexroom")
    @CrossOrigin
    @ApiOperation("删除考试(场)")
    public ResponseData addeEroom(@RequestParam int kid){
        int status = exroomService.delExroom(kid);
        switch (status) {
            case -1:
                return new ResponseData(ExceptionMsg.FAILED,"考场不存在或已删除请勿重复操作");
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS_ER,"删除成功");
        }
        return new ResponseData(ExceptionMsg.FAILED_F,"后端错误");
    }

    @GetMapping("/last3exam")
    @ApiOperation("老师端最新发布的3场考试")
    public ResponseData getLastThreeExam(@RequestParam String name){
        return new ResponseData(ExceptionMsg.SUCCESS, exroomService.getLastThreeExam(name));
    }

    @GetMapping("/Slast3exam")
    @ApiOperation("学生端首页最近3场考试")
    public ResponseData getSLastThreeExam(){
        return new ResponseData(ExceptionMsg.SUCCESS, exroomService.getSLastThreeExam());
    }


}
