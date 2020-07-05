package com.q7w.examination.config.cas;

import com.q7w.examination.Service.UserService;
import com.q7w.examination.entity.User;
import com.q7w.examination.util.JWTToken;
import com.q7w.examination.util.JwtUtils;
import io.buji.pac4j.realm.Pac4jRealm;
import io.buji.pac4j.subject.Pac4jPrincipal;
import io.buji.pac4j.token.Pac4jToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.pac4j.core.profile.CommonProfile;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Tornado Young
 * @version 2019/7/15 17:15
 */
public class CasRealm extends Pac4jRealm {
    private String clientName;
    @Autowired
    private UserService userService;
//    public String getClientName() {
//        return clientName;
//    }
//
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        Pac4jPrincipal pac4jPrincipal = (Pac4jPrincipal) principals.getPrimaryPrincipal();
        return pac4jPrincipal.getProfile().getId();
    }
    /**
     * 验证用户身份
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        if (!(authenticationToken instanceof JWTToken)) {
            final Pac4jToken pac4jToken = (Pac4jToken) authenticationToken;
            final List<CommonProfile> commonProfileList = pac4jToken.getProfiles();
            final CommonProfile commonProfile = commonProfileList.get(0);
            final Pac4jPrincipal principal = new Pac4jPrincipal(commonProfileList, getPrincipalNameAttribute());
            final PrincipalCollection principalCollection = new SimplePrincipalCollection(principal, getName());
            return new SimpleAuthenticationInfo(principalCollection, commonProfileList.hashCode());
        } else {
            // 这里的 token是从 JWTFilter 的 executeLogin 方法传递过来的
            System.out.println(authenticationToken.getCredentials());
            String token = (String) authenticationToken.getCredentials();
            String username = JwtUtils.getUsername(token);
            User user = userService.findByUsername(username);
            if (user == null) {
                throw new AuthenticationException("用户名或密码错误");
            }
            if (!JwtUtils.verify(token, username, JwtUtils.SECRET_KEY)) {
                throw new AuthenticationException("token校验不通过");
            }

            return new SimpleAuthenticationInfo(token, token, getName());
        }
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authInfo = new SimpleAuthorizationInfo();
        //TODO 如何获取roles和perms?
//        info.addStringPermission(String.valueOf(commonProfile.getAttribute("perms")));
//        info.addRole(String.valueOf(commonProfile.getAttribute("roles")));
        authInfo.addStringPermission("user");
        return authInfo;
    }

}
