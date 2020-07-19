package com.q7w.examination.filter;

import com.q7w.examination.Service.AdminPermissionService;
import com.q7w.examination.Service.RedisService;
import com.q7w.examination.util.JWTToken;
import com.q7w.examination.util.JwtUtils;
import com.q7w.examination.util.SpringContextUtils;
import com.q7w.examination.util.TokenUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;


import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@Log4j2
public class URLPathMatchingFilter extends PathMatchingFilter {
    @Autowired
    AdminPermissionService adminPermissionService;


    // 登录标识
    private static String LOGIN_SIGN = "Authorization";
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (HttpMethod.OPTIONS.toString().equals((httpServletRequest).getMethod())) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            return true;
        }

        if (null == adminPermissionService) {
            adminPermissionService = SpringContextUtils.getContext().getBean(AdminPermissionService.class);
        }

        String requestAPI = getPathWithinApplication(request);

   //     Subject subject = SecurityUtils.getSubject();
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader(LOGIN_SIGN);
        if (authorization==null) {
            log.info("未登录用户尝试访问需要登录接口："+requestAPI);
            return false;
        }
        JWTToken token = new JWTToken(authorization);
        String usertoken = token.getCredentials().toString();
        String userinfo = JwtUtils.getUsername(usertoken);
        if (!JwtUtils.verify(usertoken, userinfo, "s")) {
            log.info("Token已失效");
            return false;
        }
        // 判断访问接口是否需要过滤（数据库中是否有对应信息）
        boolean needFilter = adminPermissionService.needFilter(requestAPI);
        if (!needFilter) {
            return true;
        } else {
            // 判断当前用户是否有相应权限
            boolean hasPermission = false;
       //     String username = subject.getPrincipal().toString();
            String username = userinfo;
            Set<String> permissionAPIs = adminPermissionService.listPermissionURLsByUser(username);
            for (String api : permissionAPIs) {
                // 匹配前缀
                if (requestAPI.startsWith(api)) {
                    hasPermission = true;
                    break;
                }
            }

            if (hasPermission) {
                log.trace("用户：" + username + "访问了：" + requestAPI + "接口");
                return true;
            } else {
                log.warn( "用户：" + username + "访问了没有权限的接口：" + requestAPI);
                return false;
            }
        }
    }
}
