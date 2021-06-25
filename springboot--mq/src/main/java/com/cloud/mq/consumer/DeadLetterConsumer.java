package com.cloud.mq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author lisw
 * @create 2021/6/25 21:58
 */
@Component
@Slf4j
public class DeadLetterConsumer {

    /**接收消息*/
    @RabbitListener(queues = {"QD"})
    public void receiveD(Message msg, Channel channel) throws Exception{
        String result = new String(msg.getBody());
        log.info("当前时间:{},收到死信队列的消息: {}",new Date(),result);
    }
}
