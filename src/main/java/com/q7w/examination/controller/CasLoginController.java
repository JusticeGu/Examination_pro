package com.q7w.examination.controller;


import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import com.q7w.examination.util.JWTToken;
import com.q7w.examination.util.JwtUtils;
import io.buji.pac4j.subject.Pac4jPrincipal;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author xiaogu
 * @date 2020/7/15 19:29
 **/
@RestController
public class CasLoginController {
    private static final Logger logger = LoggerFactory.getLogger(CasLoginController.class);


    /**
     * cas登录
     * 前端重定向到此传递登录所需token
     */
    @RequestMapping("/sys/caslogin")
    @ResponseBody
    public Map<String, Object> caslogin(HttpSession session)throws IOException {

        String token = (String)session.getAttribute("token");
        Map r = new HashMap();
        r.put("token", token);
        return r;
    }
    @RequestMapping("/api/caslogin_1")
    @ResponseBody
    @CrossOrigin
    public ResponseData caslogin(@CookieValue("token") String token, @CookieValue("username")String username)throws IOException {
        boolean ans = JwtUtils.verify(token, username, username);

        Map r = new HashMap();
        r.put("token",token);
        return new ResponseData(ExceptionMsg.SUCCESS,r);
    }
    /**
     * 生成前端需要的用户信息
     * @param jwtToken token
     * @param userId userId
     * @return userInfo
     */
    private HashMap<String, Object> generateUserInfo(JWTToken jwtToken, String userId) {

        HashMap<String, Object> userInfo = new HashMap<>();

        userInfo.put("token", jwtToken.getCredentials());
        userInfo.put("expireTime", 3600);

        return userInfo;
    }

    /**
     * 登录认证
     * @return 登录结果
     */
    @GetMapping("/api/sys/caslogin")
    @CrossOrigin
    public void login(HttpServletRequest request, HttpServletResponse response) throws ClassCastException {
        Pac4jPrincipal principal = (Pac4jPrincipal)request.getUserPrincipal();
        String userId = (String)principal.getProfile().getId();
        String cn = (String)principal.getProfile().getAttribute("cn");
        /*
        根据统一认证返回信息确定用户角色，并一同写入数据库
        略
        */
        String token = JwtUtils.sign(userId, userId);
        JWTToken jwtToken = new JWTToken(token);
        HashMap<String, Object> userInfo = this.generateUserInfo(jwtToken, userId);

        try {
            response.sendRedirect("http://localhost:8080/caslogin"+"?userName="+userId+
                    "&token="+userInfo.get("token")+"&expireTime="+userInfo.get("expireTime"));
        } catch (Exception e) {
            System.out.println("LoginController中的异常");
            e.printStackTrace();
        }

    } @GetMapping("/caslogout")
    public String caslogout(){
        SecurityUtils.getSubject().logout();
        return "redirect:https://cas.q7w.cn:8443/logout?service=http://localhost/logouttips";
    }
    @GetMapping("/casindex")
    public String index(HttpServletRequest request, Model model) {
        System.out.println(request.getUserPrincipal().getName());
        System.out.println(SecurityUtils.getSubject().getPrincipal());
        model.addAttribute("userName", "maple");
        return "/index";
    }


    @PostMapping("/caslogin")
    public String loginbycas(String username, String password) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, true);
        try {
            logger.info("对用户[" + username + "]进行登录验证..验证开始");
            subject.login(token);
            logger.info("对用户[" + username + "]进行登录验证..验证通过");
        } catch (UnknownAccountException uae) {
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,未知账户");
        } catch (IncorrectCredentialsException ice) {
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,错误的凭证");
        } catch (LockedAccountException lae) {
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");
        } catch (ExcessiveAttemptsException eae) {
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,错误次数过多");
        } catch (AuthenticationException ae) {
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,堆栈轨迹如下");
            ae.printStackTrace();
        }
        if (subject.isAuthenticated()) {
            logger.info("用户[" + username + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
            return "redirect:/user";
        }
        token.clear();
        return "redirect:/login";

    }


}

