package com.q7w.examination.rabbit;

import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SenderA {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(Map map) {
        System.out.println("邮件正在上传至队列 : ");
        //使用AmqpTemplate将消息发送到消息队列QueueHello中去
        this.rabbitTemplate.convertAndSend("email_exchange","email_queue",map);
    }
    public boolean sendemail(String to, String title, String content){
        if (to.equals(null)||title.equals(null)||content.equals(null)){
            return false;}
        Map map = new HashMap();
        map.put("to", to);
        map.put("text", content);
        map.put("subject", title);
        send(map);
        return true;
    }
}