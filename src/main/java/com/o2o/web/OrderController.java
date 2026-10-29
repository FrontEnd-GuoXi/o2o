package com.o2o.web;

import com.o2o.dto.PayOrderDTO;
import com.o2o.dto.PersonInfoDTO;
import com.o2o.entity.*;
import com.o2o.exceptions.BusinessException;
import com.o2o.security.UserContextHolder;
import com.o2o.service.OrderService;
import com.o2o.util.ResponseResultWrap;
import com.o2o.dto.OrderDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;




    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseResultWrap<Map<String, Object>> createOrder (@RequestBody OrderDTO orderDTO) {
        PersonInfoDTO userInfoVO = UserContextHolder.getUserInfo();
        PersonInfo userInfo = new PersonInfo();
        BeanUtils.copyProperties(userInfoVO, userInfo);
        String userId = userInfo.getUserId().toString();

        String token = orderDTO.getToken();
        String storedUserId = (String) redisTemplate.opsForValue().get("order_token:" + token);
        if (storedUserId == null || !storedUserId.equals(userId)) {
            throw new BusinessException("无效或已使用的支付凭证");
        }

        Map<String, Object> result = orderService.addOrder(orderDTO, userInfo);
        return ResponseResultWrap.success(result, "订单生成成功");
    }

    @ResponseBody
    @RequestMapping(value = "/getOrderToken", method = RequestMethod.GET)
    public ResponseResultWrap<String> getOrderToken () {
        PersonInfoDTO userInfoVO = UserContextHolder.getUserInfo();
        String userId = userInfoVO.getUserId().toString();
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString().replace("-", "");
        redisTemplate.opsForValue().set("order_token:" + uuidStr, userId, 60 * 5, TimeUnit.SECONDS);
        return ResponseResultWrap.success(uuidStr, "token生成成功。");
    }

    @ResponseBody
    @RequestMapping(value = "/inventoryDeduction", method = RequestMethod.POST)
    public ResponseResultWrap<Boolean> inventoryDeduction (@RequestBody PayOrderDTO payOrderDTO) {
        List<Long> ids = payOrderDTO.getOrderList();
        String token = payOrderDTO.getToken();
        Boolean result = orderService.inventoryDeduction(ids);
        Boolean deleted = redisTemplate.delete("order_token:" + token);
        if (!Boolean.TRUE.equals(deleted)) {
            throw new BusinessException("无效或已使用的支付凭证");
        }
        return ResponseResultWrap.success(result, "库存扣减成功");
    }



    @ResponseBody
    @RequestMapping(value = "/inventoryRelease", method = RequestMethod.POST)
    public ResponseResultWrap<Boolean> inventoryRelease (@RequestBody PayOrderDTO payOrderDTO) {
        List<Long> ids = payOrderDTO.getOrderList();
        String token = payOrderDTO.getToken();
        Boolean result = orderService.inventoryRelease(ids);
        Boolean deleted = redisTemplate.delete("order_token:" + token);
        if (!Boolean.TRUE.equals(deleted)) {
            throw new BusinessException("无效或已使用的支付凭证");
        }
        return ResponseResultWrap.success(result, "库存释放成功");
    }



}
