package com.q7w.examination.util;

import org.apache.shiro.authc.AuthenticationToken;

import org.apache.shiro.authc.AuthenticationToken;

public class JWTToken implements AuthenticationToken {

    private static final long serialVersionUID = 1L;

    // 加密后的 JWT token串
    private String token;

    private String userName;

    public JWTToken(String token) {
        this.token = token;
        this.userName = JwtUtils.getUsername(token);
    }

    @Override
    public Object getPrincipal() {
        return this.userName;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}