package com.q7w.examination.util;

import com.q7w.examination.Service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TokenUtil {
    @Autowired
    RedisService redisService;
    public boolean exist(String token){
        Object ans = redisService.hmget(token);
        if (token==null){return false;}
        return true;
    }
    public Object getvalue(String token,String key){
        Object ans = redisService.hget(token,key);
        return ans;
    }
    public Map<Object, Object> getallvalue(String token){
        return redisService.hmget(token);
    }
    public boolean settoken(String token, Map<String, Object> map, long time){
        return redisService.hmset(token, map, time);
    }
}
