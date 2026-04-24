package com.o2o.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.o2o.dto.PayOrderDTO;
import com.o2o.dto.PersonInfoDTO;
import com.o2o.entity.*;
import com.o2o.security.UserContextHolder;
import com.o2o.service.OrderService;
import com.o2o.service.impl.OrderServiceImpl;
import com.o2o.util.RabbitMQSender;
import com.o2o.util.ResponseResultWrap;
import com.o2o.dto.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Controller
@RequestMapping("/order")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;




    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseResultWrap<String> createOrder (@RequestBody OrderDTO orderDTO) {
        try{
            PersonInfoDTO userInfoVO = UserContextHolder.getUserInfo();
            PersonInfo userInfo = new PersonInfo();
            BeanUtils.copyProperties(userInfoVO, userInfo);
            String userId = userInfo.getUserId().toString();

            String token = orderDTO.getToken();
            String storedUserId = (String) redisTemplate.opsForValue().get("order_token:" + token);
            if (storedUserId == null || !storedUserId.equals(userId)) {
                return ResponseResultWrap.fail("无效或已使用的支付凭证");
            }

            String totalPrice = orderService.addOrder(orderDTO, userInfo);

            return ResponseResultWrap.success(totalPrice, "订单生成成功");
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseResultWrap.fail("订单生成失败");
        }

    }

    @ResponseBody
    @RequestMapping(value = "/getOrderToken", method = RequestMethod.GET)
    public ResponseResultWrap<String> getOrderToken () {
        try {
            PersonInfoDTO userInfoVO = UserContextHolder.getUserInfo();
            String userId = userInfoVO.getUserId().toString();
            UUID uuid = UUID.randomUUID();
            String uuidStr = uuid.toString().replace("-", "");
            redisTemplate.opsForValue().set("order_token:" + uuidStr, userId, 60 * 5, TimeUnit.SECONDS);
            return ResponseResultWrap.success(uuidStr, "token生成成功。");
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseResultWrap.fail("order token生成失败");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/inventoryDeduction", method = RequestMethod.POST)
    public ResponseResultWrap<Boolean> inventoryDeduction (@RequestBody PayOrderDTO payOrderDTO) {
        try {
            List<Long> ids = payOrderDTO.getOrderList();
            String token = payOrderDTO.getToken();
            Boolean result = orderService.inventoryDeduction(ids);
            Boolean deleted = redisTemplate.delete("order_token:" + token);
            if (!Boolean.TRUE.equals(deleted)) {
                return ResponseResultWrap.fail("无效或已使用的支付凭证");
            }
            return ResponseResultWrap.success(result, "库存扣减成功");
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseResultWrap.fail(false, "库存扣减失败");
        }
    }



    @ResponseBody
    @RequestMapping(value = "/inventoryRelease", method = RequestMethod.POST)
    public ResponseResultWrap<Boolean> inventoryRelease (@RequestBody PayOrderDTO payOrderDTO) {
        try {
            List<Long> ids = payOrderDTO.getOrderList();
            String token = payOrderDTO.getToken();
            Boolean result = orderService.inventoryRelease(ids);
            Boolean deleted = redisTemplate.delete("order_token:" + token);
            if (!Boolean.TRUE.equals(deleted)) {
                return ResponseResultWrap.fail("无效或已使用的支付凭证");
            }
            return ResponseResultWrap.success(result, "库存释放成功");
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseResultWrap.fail(false, "库存释放失败");
        }
    }



}
