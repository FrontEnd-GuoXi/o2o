package com.o2o.web;

import com.o2o.dto.PersonInfoDTO;
import com.o2o.entity.*;
import com.o2o.security.UserContextHolder;
import com.o2o.service.OrderService;
import com.o2o.service.ProductInfoService;
import com.o2o.service.ShopService;
import com.o2o.util.Cls2Cls;
import com.o2o.util.ResponseResultWrap;
import com.o2o.util.SnowflakeIdGenerator;
import com.o2o.vo.OrderVO;
import com.o2o.vo.ProductItemVO;
import com.o2o.vo.ShopItemVo;
import com.o2o.vo.ShopVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;



    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseResultWrap<Boolean> createOrder (@RequestBody OrderVO orderVO) {
        PersonInfoDTO userInfoVO = UserContextHolder.getUserInfo();
        PersonInfo userInfo = new PersonInfo();
        BeanUtils.copyProperties(userInfoVO, userInfo);

        Boolean success = orderService.addOrder(orderVO, userInfo);

    }


}
