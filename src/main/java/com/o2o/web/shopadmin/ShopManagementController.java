package com.o2o.web.shopadmin;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.o2o.dto.ShopDTO;
import com.o2o.entity.PersonInfo;
import com.o2o.entity.Shop;
import com.o2o.service.ShopService;
import com.o2o.util.HttpServletRequestUtil;
import com.o2o.util.ResponseResultWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

    private static final Logger logger = LoggerFactory.getLogger(ShopManagementController.class);

    @Autowired
    ShopService shopService;

    @ResponseBody
    @RequestMapping(value = "/registeredshop", method = RequestMethod.POST)
    public ResponseResultWrap<Object> addShop(HttpServletRequest request) {
        try{
            String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
            ObjectMapper mapper = new ObjectMapper();
            Shop shop = mapper.readValue(shopStr, Shop.class);
            PersonInfo personInfo = new PersonInfo();
            personInfo.setUserId(1L);
            shop.setOwner(personInfo);
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> files = multipartRequest.getFiles("files");
            CommonsMultipartFile img = (CommonsMultipartFile) files.get(0);
            ShopDTO shopTransfer = shopService.addShop(shop, img.getInputStream(), img.getOriginalFilename());
            return ResponseResultWrap.success(shopTransfer.getStateInfo());
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseResultWrap.fail("店铺创建失败");
        }
    }

}
