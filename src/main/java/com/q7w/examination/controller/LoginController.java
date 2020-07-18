package com.q7w.examination.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.q7w.examination.Service.RedisService;
import com.q7w.examination.Service.UserService;
import com.q7w.examination.dao.UserDAO;
import com.q7w.examination.entity.OpenIdJson;
import com.q7w.examination.entity.User;
import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import com.q7w.examination.util.HttpUtil;
import com.q7w.examination.util.JWTToken;
import com.q7w.examination.util.JwtUtils;
import com.q7w.examination.util.Pbkdf2Sha256;
import io.buji.pac4j.subject.Pac4jPrincipal;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
/**
 * @author xiaogu
 * @date 2020/7/15 19:29
 **/
@RestController
public class LoginController implements Serializable {
    @Autowired
    UserService userService;
    @Autowired
    private UserDAO userDAO;
    @Resource
    private RedisService redisService;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/api/register")
    @CrossOrigin
    public ResponseData register(@RequestBody User user,@RequestParam("code") String code) {
        if(!userService.checkmailcode(user.getEmail(),code)){
            return new ResponseData(ExceptionMsg.FAILED,"验证码错误，请重新输入");
        }
        int status = userService.register(user);
        switch (status) {
            case 0:
                return new ResponseData(ExceptionMsg.FAILED,"用户名密码不可以为空");
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS,user.getUsername());
            case 2:
                return new ResponseData(ExceptionMsg.FAILED_F,"用户已存在");
        }
        return new ResponseData(ExceptionMsg.FAILED_F,"后端错误");
    }
    @PostMapping("/api/msgregister")
    @CrossOrigin
    public ResponseData msgregister(@RequestBody User user) {
        int status = userService.msgregister(user);
        switch (status) {
            case 0:
                return new ResponseData(ExceptionMsg.FAILED,"用户名密码不可以为空!");
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS,user.getUsername());
            case 2:
                return new ResponseData(ExceptionMsg.FAILED_F,"用户已存在");
            case 3:
                return new ResponseData(ExceptionMsg.FAILED_F,"逻辑错误");
        }
        return new ResponseData(ExceptionMsg.FAILED_F,"后端错误");
    }
    @PostMapping("/api/login")
    @CrossOrigin
    public ResponseData login(@RequestBody User requestUser) {
        String username = requestUser.getUsername();
        username = HtmlUtils.htmlEscape(username);
        Subject subject = SecurityUtils.getSubject();
//        subject.getSession().setTimeout(10000);
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,requestUser.getUser_password());
        usernamePasswordToken.setRememberMe(true);
        try {
            subject.login(usernamePasswordToken);
            User user = userService.findByUsername(username);
            if (user.isEnabled()) {
                return new ResponseData(ExceptionMsg.Login_FAILED_1,"用户状态异常，请联系客服人员处理");
            }
            Map<String,Object> userinfo = new Hashtable<>();
            userinfo.put("token", JwtUtils.sign(username, "s"));
            userinfo.put("username",username );
            return new ResponseData(ExceptionMsg.SUCCESS,userinfo);
        } catch (IncorrectCredentialsException e) {
            return new ResponseData(ExceptionMsg.Login_FAILED_1,"用户名或密码错误");
        } catch (UnknownAccountException e) {
            return new ResponseData(ExceptionMsg.Login_FAILED_1,"用户名或密码错误");
        }
    }
    @PostMapping("/api/jwtlogin")
    @CrossOrigin
    public ResponseData jwtlogin(@RequestBody User requestUser) {
        String username = requestUser.getUsername();
        username = HtmlUtils.htmlEscape(username);
        String password = requestUser.getUser_password();
        User user = userService.findByUsername(username);
        if(user==null){return new ResponseData(ExceptionMsg.Login_FAILED_1,"用户名或密码错误");}
        boolean match = Pbkdf2Sha256.verification(password,user.getUser_password());
        if (match){
            if (user.isEnabled()) {
                return new ResponseData(ExceptionMsg.Login_FAILED_1,"用户状态异常，请联系客服人员处理");
        }
            Map<String,Object> userinfo = new Hashtable<>();
            String token = JwtUtils.sign(username, "s");
            userinfo.put("token",token);
            userinfo.put("username",username);
          //  redisService.hmset(token,userinfo,3600);
            redisService.set(token, user,600);
            return new ResponseData(ExceptionMsg.SUCCESS,userinfo);}
            else {
                return new ResponseData(ExceptionMsg.Login_FAILED_1,"用户名或密码错误");
                }

    }
    @PostMapping(value = "/api/login1")
    @RequiresPermissions("/api/login1")
    @ResponseBody
    @CrossOrigin
    public ResponseData login_1(@RequestBody User requestUser) {
        String username = requestUser.getUsername();
        String user_password = requestUser.getUser_password();
        Subject subject = SecurityUtils.getSubject();
//        subject.getSession().setTimeout(10000);
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,requestUser.getUser_password());
        //System.out.println((usernamePasswordToken.getPassword()).toString()+usernamePasswordToken.getUsername());
        usernamePasswordToken.setRememberMe(true);
        try {
            subject.login(usernamePasswordToken);
            return new ResponseData(ExceptionMsg.SUCCESS,requestUser.getUsername());
        } catch (AuthenticationException e) {
            String message = "账号密码错误";
            return new ResponseData(ExceptionMsg.Login_FAILED_1,"用户名或密码错误");
        }
    }

    @ResponseBody
    @GetMapping("api/logout")
    @CrossOrigin
    public ResponseData logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        String message = "成功登出";
        return new ResponseData(ExceptionMsg.SUCCESS,message);
    }
    @ResponseBody
    @PutMapping("/api/user/resetpwd")
    @CrossOrigin
    public ResponseData resetpwd(String username,String pwd,String newpwd){
        int status = userService.resetpwd(username,pwd,newpwd);
        switch (status) {
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS,"修改成功");
            case 2:
                return new ResponseData(ExceptionMsg.FAILED_F,"原密码错误");
            case 3:
                return new ResponseData(ExceptionMsg.FAILED_F,"后端错误-5");
        }
        return new ResponseData(ExceptionMsg.FAILED_F,"后端错误");
    }
    @PutMapping("/api/admin/user/password")
    public ResponseData resetPassword(@RequestBody User requestUser) {
        if (userService.resetPassword(requestUser)) {
            return new ResponseData(ExceptionMsg.SUCCESS,"密码已初始化为手机号后6位");
        } else {
            return new ResponseData(ExceptionMsg.SUCCESS,"参数错误，重置失败");
        }
    }
    @ResponseBody
    @GetMapping("api/au")
    @CrossOrigin
    public ResponseData au() {
        try {
            Subject subject= SecurityUtils.getSubject();
            String username = SecurityUtils.getSubject().getPrincipal().toString();
        //    String username = userService.getusernamebysu();
           //  User user = userService.findByUsername(username);
            return new ResponseData(ExceptionMsg.SUCCESS,username);
           // return new ResponseData(ExceptionMsg.FAILED,"异常");
        }
        catch (Exception e){
            return new ResponseData(ExceptionMsg.FAILED,"用户未登录");
        }
    }

    @GetMapping("api/authentication")
    @CrossOrigin
    public ResponseData authentication() {
        try {
            String username = SecurityUtils.getSubject().getPrincipal().toString();
            User user = userService.findByUsername(username);
            return new ResponseData(ExceptionMsg.SUCCESS,user.getUsername());
        }
       catch (Exception e){
           return new ResponseData(ExceptionMsg.FAILED,"fail");
       }

    }

    @GetMapping("/wx/getopenid")
    public String userLogin(@RequestParam("code") String code) throws IOException {
         String WXAPPID = "wxc4d82adbcb40d914";
         String WXSECRET = "a789fb8efbbc3aea7729d9c759de2568";
        String result = "";
        try{//请求微信服务器，用code换取openid。HttpUtil是工具类，后面会给出实现，Configure类是小程序配置信息，后面会给出代码
            result = HttpUtil.doGet(
                    "https://api.weixin.qq.com/sns/jscode2session?appid="
                            + WXAPPID + "&secret="
                            + WXSECRET + "&js_code="
                            + code
                            + "&grant_type=authorization_code", null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        OpenIdJson openIdJson = mapper.readValue(result,OpenIdJson.class);
        System.out.println(result.toString());
        System.out.println(openIdJson.getOpenid());
        return result;
    }


    @PostMapping("api/wxbind")
    @CrossOrigin
    public ResponseData wxbind(@RequestBody User requestUser) {
        String username = requestUser.getUsername();
        String password = requestUser.getUser_password();
        String openid = requestUser.getWxuid();
        if(userService.wxisExist(openid)){
            return new ResponseData(ExceptionMsg.Login_FAILED_1,"您已进行过绑定操作请勿重复操作");}
        else{
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,password);
        try {
            subject.login(usernamePasswordToken);
            User userInDB = userDAO.findByUsername(username);
            if(userInDB.getWxuid() == null){userInDB.setWxuid(openid);
                userDAO.save(userInDB);
                return new ResponseData(ExceptionMsg.SUCCESS_WXBIND,username);}
            else {
                return new ResponseData(ExceptionMsg.Login_FAILED_1,"该用户已绑定微信，重置请联系管理员");}
            }
         catch (IncorrectCredentialsException e) {
            return new ResponseData(ExceptionMsg.Login_FAILED_1,"密码错误");
        } catch (UnknownAccountException e) {
            return new ResponseData(ExceptionMsg.Login_FAILED_2,"用户不存在");
        }

    }}
    @GetMapping("api/wxlogin")
    @CrossOrigin
    public ResponseData wxlogin(String openid) {
        try {
            if(redisService.hasKey(openid)){ return new ResponseData(ExceptionMsg.SUCCESS_GETR,"登录状态正常");
            }else if (userService.wxisExist(openid)&&userService.wxisable(openid)){
                redisService.set(openid, 1, 43200);
                return new ResponseData(ExceptionMsg.SUCCESS_GET,"登录状态正常");
            } else{
                return new ResponseData(ExceptionMsg.FAILED,"登录验证失败，请先绑定用户,或检查账户状态");
            }
        } catch (Exception e){
            return new ResponseData(ExceptionMsg.FAILED,"后端错误");
        }

    }
    @RequestMapping(value = "/api/carefresh", method = RequestMethod.GET)
    @CrossOrigin
    public ResponseData refresh(String cache) throws Exception {
        redisService.del(cache);
        return new ResponseData(ExceptionMsg.SUCCESS,"缓存已清除");
    }
    @RequestMapping(value = "/api/getcach", method = RequestMethod.GET)
    @CrossOrigin
    public ResponseData getcache1(String cache) throws Exception {
        Long ucache = redisService.getExpire(cache);
        return new ResponseData(ExceptionMsg.SUCCESS,ucache);
    }

}
