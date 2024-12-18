package com.o2o.web.shopadmin;


import com.o2o.service.ShopService;
import com.o2o.service.impl.ShopServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

    private static final Logger logger = LoggerFactory.getLogger(ShopManagementController.class);

    @Autowired
    ShopService shopService;

    @ResponseBody
    @RequestMapping(value = "/registeredshop", method = RequestMethod.POST)
    public Map<String,Object> addShop (HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.debug("HttpServletRequest------"+request.toString());




        return resultMap;
    }

}
