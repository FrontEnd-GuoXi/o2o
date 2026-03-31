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

import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    SnowflakeIdGenerator snowflakeIdGenerator;

    @Autowired
    ProductInfoService productInfoService;

    @Autowired
    ShopService shopService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseResultWrap<Boolean> createOrder (@RequestBody OrderVO orderVo) {
        List<ShopItemVo> shopList = orderVo.getShopList();
        PersonInfoDTO userInfoVO = UserContextHolder.getUserInfo();
        Long userId = userInfoVO.getUserId();
        PersonInfo userInfo = new PersonInfo();
        BeanUtils.copyProperties(userInfo, userInfo);

         shopList.stream().forEach(shopItemVo -> {
             Order order = new Order();
             order.setOrderId(snowflakeIdGenerator.nextId());
             order.setOrderStatus(0);
             Date currentDate = new Date();
             order.setCreateTime(currentDate);
             order.setLastEditTime(currentDate);
             order.setBuyer(userInfo);

             ShopVO shopVO = shopService.queryShopById(shopItemVo.getShopId(), userId);
             Shop shop = Cls2Cls.shopVOToShop(shopVO, new Shop());
             order.setShop(shop);

             List<ProductItemVO>  productItemVOList = shopItemVo.getProductList();
             productItemVOList.stream().forEach(ProductItemVO -> {
                 OrderItem orderItem = new OrderItem();
                 Long productId = ProductItemVO.getProductId();
                 Product product = productInfoService.getProductByProductId(productId);
                 product.setProductId(productId);
                 orderItem.setProduct(product);
                 orderItem.setOrder(order);
             });


             orderService.addOrder(order);
         });

    }


}
