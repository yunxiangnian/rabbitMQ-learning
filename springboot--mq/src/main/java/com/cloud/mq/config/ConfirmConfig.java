package com.cloud.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author lisw
 * @create 2021/6/26 15:29
 * 发布确认兜底方案  添加缓存测试
 */
@Configuration
public class ConfirmConfig {
    private static final String CONFIRM_EXCHANGE_NAME = "confirm-exchange";
    private static final String CONFIRM_QUEUE_NAME = "confirm-queue";
    private static final String CONFIRM_ROUTING_KEY = "confirm-key";

    @Bean("confirmExchange")
    public DirectExchange directExchange(){
        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
    }

    @Bean("confirmQueue")
    public Queue confirmQueue(){
        HashMap<String, Object> map = new HashMap<>(8);
        return new Queue(CONFIRM_QUEUE_NAME,false,false,false,map);
    }

    @Bean
    public Binding queueConfirmBindingExchange(@Qualifier("confirmQueue")Queue queue,
                                        @Qualifier("confirmExchange")Exchange exchange){

        return BindingBuilder.bind(queue).to(exchange).with(CONFIRM_ROUTING_KEY).noargs();
    }
}
