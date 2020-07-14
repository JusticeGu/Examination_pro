package com.q7w.examination.rabbit;


import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RabbitListener(queues = "Sendwxmsg")//监听QueueHello的消息队列
public class SendwxmsgReceiver {

    @RabbitHandler//@RabbitHandler来实现具体消费
    public void QueueReceiver(String msg) {
       // clubNewUserService.sendsuccessmessage(clubNewUser.getWxopenid(),clubNewUser.getUsername(),"zb");
        System.out.println("Receiver A: " + msg);
    }

}


