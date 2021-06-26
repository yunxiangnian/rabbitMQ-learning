package com.cloud.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author lisw
 * @create 2021/6/26 14:43
 * 使用插件实现的延迟队列的配置类
 */
@Configuration
public class DelayedQueueConfig {
    /**
     * 交换机
     * 队列
     * routingKey
     * */
    private static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    private static final String DELAYED_QUEUE_NAME = "delayed.queue";
    private static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    /**声明队列*/
    @Bean
    public Queue delayedQueue(){
        return new Queue(DELAYED_QUEUE_NAME);
    }

    /**声明交换机 基于插件的*/
    @Bean
    public CustomExchange delayedExchange(){
        /**
         * 1、交换机名称
         * 2、交换机类型
         * 3、是否持久化
         * 4、是否自动删除
         * 5、参数
         * */
        HashMap<String, Object> map = new HashMap<>(2);
        /**固定参数*/
        map.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, map);
    }

    /**绑定交换机和队列*/
    @Bean
    public Binding queueBindingExchange(@Qualifier("delayedQueue") Queue queue,
                                        @Qualifier("delayedExchange")Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
