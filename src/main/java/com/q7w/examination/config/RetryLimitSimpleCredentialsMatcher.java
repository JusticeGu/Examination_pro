package com.q7w.examination.config;

import com.q7w.examination.util.Pbkdf2Sha256;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

class RetryLimitSimpleCredentialsMatcher extends SimpleCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken uptoken = (UsernamePasswordToken) token;
        String userName = uptoken.getUsername();

        String plainPassword = String.valueOf(uptoken.getPassword());
        String password = String.valueOf(getCredentials(info));

        boolean matches = Pbkdf2Sha256.verification(plainPassword, password);
        if (matches) {
            //clear retry count
            //CacheUtils.del(LOGIN_FAIL + userName);
            System.out.println(userName+"登录校验成功");
        }
        return matches;
    }
}
