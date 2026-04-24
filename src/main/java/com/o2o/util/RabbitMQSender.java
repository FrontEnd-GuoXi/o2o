package com.o2o.util;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送订单创建消息
     * @param routingKey 路由键，如 "order.create"
     * @param message 消息对象 (会被自动转为JSON)
     */
    public void send(String exchange, String routingKey, Object message) {
        // 这里使用 convertAndSend，会自动使用配置好的 Jackson2JsonMessageConverter
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
