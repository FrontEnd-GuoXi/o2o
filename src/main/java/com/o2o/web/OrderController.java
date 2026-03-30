package com.o2o.web;

import com.o2o.entity.Order;
import com.o2o.entity.OrderItem;
import com.o2o.entity.Product;
import com.o2o.entity.Shop;
import com.o2o.service.OrderService;
import com.o2o.service.ProductInfoService;
import com.o2o.util.ResponseResultWrap;
import com.o2o.util.SnowflakeIdGenerator;
import com.o2o.vo.OrderVO;
import com.o2o.vo.ProductItemVO;
import com.o2o.vo.ShopItemVo;
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

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseResultWrap<Boolean> createOrder (@RequestBody OrderVO orderVo) {
         List<ShopItemVo> shopList = orderVo.getShopList();
         shopList.stream().forEach(shopItemVo -> {
             Order order = new Order();
             order.setOrderId(snowflakeIdGenerator.nextId());
             order.setOrderStatus(0);
             Date currentDate = new Date();
             order.setCreateTime(currentDate);
             order.setLastEditTime(currentDate);

             Shop shop = new Shop();
             shop.setShopId(shopItemVo.getShopId());
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
