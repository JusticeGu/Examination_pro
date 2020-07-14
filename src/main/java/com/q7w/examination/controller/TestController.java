package com.q7w.examination.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

import com.q7w.examination.Service.RedisService;
import com.q7w.examination.Service.UserService;
import com.q7w.examination.entity.Mail;
import com.q7w.examination.rabbit.SenderA;
import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import com.q7w.examination.util.FileUtil;
import com.q7w.examination.util.JwtUtils;
import com.q7w.examination.util.TokenUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "新增功能测试专用接口")
public class TestController implements Serializable {
    @Resource
    RedisService redisService;
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    UserService userService;
    @Autowired
    private SenderA queueSender;
    private static final long serialVersionUID = 3033545151355633240L;
    @RequestMapping(value="/test/excel",method = RequestMethod.POST)
    @CrossOrigin
    public ResponseData exceltest(MultipartFile multipartFile){
        File file = FileUtil.toFile(multipartFile);
        // 读取 Excel 中的数据
        ExcelReader reader = ExcelUtil.getReader(file);
        List<Map<String,Object>>readAll =reader.readAll();
        List<Object> unolist = new ArrayList<>();
        for(int i=0;i<=reader.getRowCount();i++){
            unolist.add(readAll.get(i).get("学号"));
        }
        Object name =reader.readCellValue(1, 4);
        return new ResponseData(ExceptionMsg.SUCCESS,readAll);
    }
    @RequestMapping(value="/test/token",method = RequestMethod.GET)
    @CrossOrigin
    public ResponseData checktoken(String token,String username){
        boolean ans = JwtUtils.verify(token,username,"s");
        String getusername = JwtUtils.getUsername(token);
        boolean timetoend = JwtUtils.isTokenExpired(token);
        String newtoken = JwtUtils.refreshTokenExpired(token,"s");
        return new ResponseData(ExceptionMsg.SUCCESS,newtoken);
    }
    @RequestMapping(value="/test/gethash",method = RequestMethod.GET)
    @CrossOrigin
    public ResponseData gethashbyredis(String key,String item){
        Object ans = redisService.hget(key, item);
        String userinfo = tokenUtil.getvalue(key, item).toString();
        return new ResponseData(ExceptionMsg.SUCCESS,ans);
    }
    @RequestMapping(value="/test/sendmail",method = RequestMethod.POST)
    @CrossOrigin
    public ResponseData sendemail(String to,String title,String content){
        if (to.equals(null)||title.equals(null)||content.equals(null)){
            return new ResponseData(ExceptionMsg.FAILED_F,"字段不完整，请完整填写所有字段");}
        Map map = new HashMap();
        map.put("to", to);
        map.put("text", content);
        map.put("subject", title);
        queueSender.send(map);
        return new ResponseData(ExceptionMsg.SUCCESS,"发送成功");
    }


}
