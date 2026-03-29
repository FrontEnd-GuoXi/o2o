package com.o2o.web;

import com.o2o.service.OrderService;
import com.o2o.util.ResponseResultWrap;
import com.o2o.vo.OrderVO;
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

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseResultWrap<Boolean> addOrder (@RequestBody OrderVO order) {
        orderService.addOrder()
    }


}
