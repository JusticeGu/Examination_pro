package com.q7w.examination.config;

import com.q7w.examination.Service.AdminPermissionService;
import com.q7w.examination.Service.AdminRoleService;
import com.q7w.examination.Service.RedisService;
import com.q7w.examination.Service.UserService;
import com.q7w.examination.entity.User;
import com.q7w.examination.util.JWTToken;
import com.q7w.examination.util.JwtUtils;
import org.apache.shiro.authc.*;
//import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Set;

public class Q7wRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private AdminPermissionService adminPermissionService;
    @Autowired
    private AdminRoleService adminRoleService;
    @Resource
    RedisService redisService;
    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    // 重写获取授权信息方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取当前用户的所有权限
        String username = principalCollection.getPrimaryPrincipal().toString();
        Set<String> permissions = adminPermissionService.listPermissionURLsByUser(username);

        // 将权限放入授权信息中
        SimpleAuthorizationInfo s = new SimpleAuthorizationInfo();
        s.setStringPermissions(permissions);
        return s;
    }

    // 获取认证信息，即根据 token 中的用户名从数据库中获取密码、盐等并返回
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = auth.getCredentials().toString();
        // 解密获得username
        String username = JwtUtils.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("令牌无效");
        }
        User userBean = (User) redisService.get(token);
        if (userBean == null) {
            throw new AuthenticationException("令牌已过期");
        } else {
            redisService.expire(token, 60);
            return new SimpleAuthenticationInfo(token, token, "MyRealm");
        }

    }



}
