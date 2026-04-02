package com.o2o.web;

import com.o2o.dto.PersonInfoDTO;
import com.o2o.entity.*;
import com.o2o.security.UserContextHolder;
import com.o2o.service.OrderService;
import com.o2o.util.ResponseResultWrap;
import com.o2o.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;



    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseResultWrap<String> createOrder (@RequestBody OrderVO orderVO) {
        PersonInfoDTO userInfoVO = UserContextHolder.getUserInfo();
        PersonInfo userInfo = new PersonInfo();
        BeanUtils.copyProperties(userInfoVO, userInfo);

        String totalPrice = orderService.addOrder(orderVO, userInfo);
        return ResponseResultWrap.success(totalPrice);
    }


}
