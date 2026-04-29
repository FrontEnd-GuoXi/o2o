package com.o2o.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.o2o.entity.Order;
import com.o2o.service.OrderService;
import com.o2o.service.impl.OrderServiceImpl;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.util.ArrayList;
import java.util.List;

// 实现 ChannelAwareMessageListener 接口，这样可以获取 Channel 进行手动 ACK
public class OrderTimeoutListener implements ChannelAwareMessageListener {

    // 手动注入 Service (SSM 中通常通过 set 方法注入)
    private OrderService orderService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(OrderTimeoutListener.class);

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 1. 反序列化消息
            byte[] body = message.getBody();
            Long orderId = objectMapper.readValue(body, Long.class);
            List<Long> orderIdList = new ArrayList<>();
            orderIdList.add(orderId);
            List<Order> orderList = orderService.queryOrderListByIds(orderIdList);
            Order order = orderList.get(0);
            if (order.getOrderStatus() == 0) {
                // 2. 执行业务逻辑 (例如：扣减库存)
                orderService.inventoryRelease(orderIdList);
                order.setOrderStatus(-1);
                orderService.updateStatusOfMultipleOrder(orderList);
            }



            // 3. 业务成功后，手动发送 ACK
            // false 表示不批量确认
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            // 4. 业务失败处理
            // 如果希望重试，requeue=true；如果希望丢弃（或进死信），requeue=false
            channel.basicNack(deliveryTag, false, true);
            logger.error("rabbitMQ监听异常：", e);
        }
    }
}
