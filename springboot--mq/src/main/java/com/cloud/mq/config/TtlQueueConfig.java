package com.cloud.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author lisw
 * @create 2021/6/25 21:22
 */
@Configuration
public class TtlQueueConfig {

    /**普通交换机*/
    private static final String X_EXCHANGE = "X";
    /**死信交换机*/
    private static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    /**普通队列*/
    private static final String QUEUE_A ="QA";
    private static final String QUEUE_B ="QB";
    /**死信队列*/
    private static final String DEAD_LETTER_QUEUE = "QD";

    /**新的普通队列*/
    private static final String QUEUE_C ="QC";


    /**声明XExchange  别名*/
    @Bean("xExchange")
    public DirectExchange xDirectExchange(){
        return new DirectExchange(X_EXCHANGE);
    }

    /**声明XExchange  别名*/
    @Bean("yExchange")
    public DirectExchange yDirectExchange(){
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    /**声明queueA*/
    @Bean("queueA")
    public Queue queueA(){
        HashMap<String, Object> map = new HashMap<>(4);
        /**设置死信队列的交换机*/
        map.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        /**设置死信队列的routingKey*/
        map.put("x-dead-letter-routing-key", "YD");
        /**设置过期时间*/
        map.put("x-message-ttl", 10000);

        return QueueBuilder.durable(QUEUE_A).withArguments(map).build();
    }

    /**声明queueB*/
    @Bean("queueB")
    public Queue queueB(){
        HashMap<String, Object> map = new HashMap<>(4);
        /**设置死信队列的交换机*/
        map.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        /**设置死信队列的routingKey*/
        map.put("x-dead-letter-routing-key", "YD");
        /**设置过期时间*/
        map.put("x-message-ttl", 40000);

        return QueueBuilder.durable(QUEUE_B).withArguments(map).build();
    }
    /**声明queueC
     * 用来存放发送者自定义的延迟队列 因此取消定义队列过期时间
     * */
    @Bean("queueC")
    public Queue queueC(){
        HashMap<String, Object> map = new HashMap<>(4);
        /**设置死信队列的交换机*/
        map.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        /**设置死信队列的routingKey*/
        map.put("x-dead-letter-routing-key", "YD");

        return QueueBuilder.durable(QUEUE_C).withArguments(map).build();
    }

    /**声明死信队列*/
    @Bean("queueD")
    public Queue queueD(){

        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    /**队列和交换机绑定*/
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") Exchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA").noargs();
    }

    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") Exchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB").noargs();
    }

    @Bean
    public Binding queueCBindingY(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") Exchange xExchange){
        return BindingBuilder.bind(queueC).to(xExchange).with("XC").noargs();
    }

    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") Exchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD").noargs();
    }
}
