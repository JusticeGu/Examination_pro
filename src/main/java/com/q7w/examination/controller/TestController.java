package com.q7w.examination.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.q7w.examination.RabbitMQ.Consumer.EmailService;
import com.q7w.examination.Service.RedisService;
import com.q7w.examination.Service.UserService;
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
    EmailService emailService;
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
        emailService.sendTextEmail(content, to, title);
        return new ResponseData(ExceptionMsg.SUCCESS,"发送成功");
    }
    @RequestMapping(value="/test/sendcheckcode",method = RequestMethod.POST)
    @CrossOrigin
    public ResponseData sendcode(String to){
        String code = userService.sendmailsecode(to);
        if (code!=null){
            String content = "您刚才申请到的邮箱验证码为：【"+code+"】(5分钟有效)如非本人操作请及时修改密码!";
            emailService.sendTextEmail(content, to, "【河马在线考试】您正在进行邮箱验证");
            return new ResponseData(ExceptionMsg.SUCCESS,"操作成功");
        }else {
            return new ResponseData(ExceptionMsg.FAILED,"发送失败");
        }
    }
    @RequestMapping(value="/test/checkcode",method = RequestMethod.POST)
    @CrossOrigin
    public ResponseData checkcode(String mail,String code){
        if (userService.checkmailcode(mail, code)){
        return new ResponseData(ExceptionMsg.SUCCESS,"验证成功");}
        else {
            return new ResponseData(ExceptionMsg.FAILED,"验证失败");
        }
    }

}
