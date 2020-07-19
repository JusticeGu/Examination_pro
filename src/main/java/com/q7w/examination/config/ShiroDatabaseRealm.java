package com.q7w.examination.config;

import com.q7w.examination.Service.RedisService;
import com.q7w.examination.Service.UserService;
import com.q7w.examination.dto.UserDTO;
import com.q7w.examination.entity.User;
import com.q7w.examination.util.JWTToken;
import com.q7w.examination.util.JwtUtils;
import com.q7w.examination.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
public class ShiroDatabaseRealm extends AuthorizingRealm {

    @Autowired
    private TokenUtil tokenUtil;
    @Resource
    RedisService redisService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        String username = JwtUtils.getUsername(principals.toString());


        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //List<String> userPermissions = shiroAuthService.getPermissions(member.getId());

        // 基于Permission的权限信息
       // info.addStringPermissions(userPermissions);

        return info;
    }

    /**
     * 使用JWT替代原生Token
     *
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        String token = (String) authcToken.getCredentials();

        String username = JwtUtils.getUsername(token);

        if (username == null) {
            throw new AuthenticationException("令牌无效");
        }
        User userBean = (User) redisService.get(token);
        if (userBean == null) {
            throw new AuthenticationException("令牌已过期");
        } else {
            redisService.expire(token, 60);
            return new SimpleAuthenticationInfo(token, token, "ShiroDatabaseRealm");
        }

    }

}
