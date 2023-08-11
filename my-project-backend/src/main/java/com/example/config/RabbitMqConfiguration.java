package com.example.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列配置
 * Created by 刘千山 on 2023/8/11/011-16:02
 */
@Configuration
public class RabbitMqConfiguration {

    @Bean("emailQueue")
    public Queue emailQueue(){
        return QueueBuilder.durable("mail").build();
    }
}
