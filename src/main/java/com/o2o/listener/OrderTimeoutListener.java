package com.o2o.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.o2o.entity.Order;
import com.o2o.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.util.ArrayList;
import java.util.List;

// 实现 ChannelAwareMessageListener 接口，这样可以获取 Channel 进行手动 ACK
public class OrderTimeoutListener implements ChannelAwareMessageListener {

    // 手动注入 Service (SSM 中通常通过 set 方法注入)
    private OrderService orderService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 1. 反序列化消息
            byte[] body = message.getBody();
            ArrayList<Long> orderIdList = objectMapper.readValue(body, new TypeReference<ArrayList<Long>>() {});


            // 2. 执行业务逻辑 (例如：扣减库存)
            orderService.inventoryDeduction(orderIdList);
            ArrayList<Order> list = new ArrayList<>();
            orderIdList.forEach(orderId -> {
                Order order = new Order();
                order.setOrderId(orderId);
                order.setOrderStatus(-1);
                list.add(order);
            });
            orderService.updateStatusOfMultipleOrder(list);

            // 3. 业务成功后，手动发送 ACK
            // false 表示不批量确认
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            e.printStackTrace();
            // 4. 业务失败处理
            // 如果希望重试，requeue=true；如果希望丢弃（或进死信），requeue=false
            channel.basicNack(deliveryTag, false, true);
            System.err.println("订单处理失败，消息重新入队");
        }
    }
}
