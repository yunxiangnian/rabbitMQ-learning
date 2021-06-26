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

    /**备份交换机*/
    private static final String BACKUP_EXCHANGE_NAME = "backup-exchange";
    /**备份队列*/
    private static final String BACKUP_QUEUE_NAME = "backup-queue";
    /**报警队列*/
    private static final String WARNING_QUEUE_NAME = "warning-queue";

    @Bean("confirmExchange")
    public DirectExchange directExchange(){
        /**确认交换机配置备份交换机 以确保宕机后将消息转发到备份交换机*/
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).durable(true)
                    .withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME).build();
    }

    @Bean("backupExchange")
    public FanoutExchange backupExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    @Bean("confirmQueue")
    public Queue confirmQueue(){
        HashMap<String, Object> map = new HashMap<>(8);
        return new Queue(CONFIRM_QUEUE_NAME,false,false,false,map);
    }

    @Bean("backupQueue")
    public Queue backupQueue(){
        HashMap<String, Object> map = new HashMap<>(8);
        return new Queue(BACKUP_QUEUE_NAME,false,false,false,map);
    }

    @Bean("warningQueue")
    public Queue warningQueue(){
        HashMap<String, Object> map = new HashMap<>(8);
        return new Queue(WARNING_QUEUE_NAME,false,false,false,map);
    }

    @Bean
    public Binding queueConfirmBindingExchange(@Qualifier("confirmQueue")Queue queue,
                                        @Qualifier("confirmExchange")Exchange exchange){

        return BindingBuilder.bind(queue).to(exchange).with(CONFIRM_ROUTING_KEY).noargs();
    }

    @Bean
    public Binding backupConfirmBindingExchange(@Qualifier("backupQueue")Queue queue,
                                               @Qualifier("backupExchange")FanoutExchange exchange){

        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Binding warningConfirmBindingExchange(@Qualifier("warningQueue")Queue queue,
                                               @Qualifier("backupExchange")FanoutExchange exchange){

        return BindingBuilder.bind(queue).to(exchange);
    }
}
