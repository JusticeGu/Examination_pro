package com.q7w.examination.util;

import com.q7w.examination.Service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidUtil {
    @Autowired
    RedisService redisService;
    public boolean validphone(String phone,String code){
        String ans = redisService.get(phone+"pvalid").toString();
        if (ans.equals(code)){
            redisService.del(phone+"pvalid");
            return true;}
        return false;
    }
    public boolean setValidCode(String phone){
        boolean ans = redisService.set(phone+"pvalid", RandomUtil.generateDigitalString(6), 300);
        return ans;
    }
}
