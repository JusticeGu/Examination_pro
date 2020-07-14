
package com.q7w.examination.rabbit;

import com.q7w.examination.Service.EmailService;
import com.q7w.examination.entity.User;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * @author longzhonghua
 * @data 2019/02/03 11:07
 */

@Component
@RabbitListener(queues ="email_queue")//监听QueueHello的消息队列
public class ReceiverB {
    @Autowired
    EmailService emailService;
    @RabbitHandler//@RabbitHandler来实现具体消费
    public void QueueReceiver(Map map) {
        String title = map.get("subject").toString();
        String to = map.get("to").toString();
        String content = map.get("text").toString();
        emailService.sendTextEmail(content, to, title);

        System.out.println("B：已接收到来自队列的邮件:【 "+title+ "】正在处理...");
    }

}
