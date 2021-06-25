package com.cloud.mq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author lisw
 * @create 2021/6/25 21:51
 * 发送延时消息
 */
@RestController
@RequestMapping("/ttl")
@Slf4j
public class MsgController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**开始发消息*/
    @GetMapping("/sendMsg/{msg}")
    public void sendMsg(@PathVariable("msg")String msg){
      /**后者会给占位符赋值，实现动态传递*/
      log.info("当前时间:{},发送一条消息给两个TTL队列",new Date(),msg);
      rabbitTemplate.convertAndSend("X","XA", "消息来自ttl为10s的队列:" + msg);
      rabbitTemplate.convertAndSend("X","XB", "消息来自ttl为40s的队列:" + msg);
    }
}
