package com.o2o.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.o2o.dto.CartProductDTO;
import com.o2o.entity.Cart;
import com.o2o.security.UserContextHolder;
import com.o2o.service.CartService;
import com.o2o.util.ResponseResultWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    CartService cartService;

    @ResponseBody
    @RequestMapping(value = "/addOrUpdateProduct", method = RequestMethod.POST)
    public ResponseResultWrap<Integer> addOrUpdateProduct (@RequestBody JsonNode cartInfo) {
        try {
            int cartId = 0;
            if (cartInfo.get("cartId") != null) {
                cartId = cartInfo.get("cartId").asInt();
            }
            int count = cartInfo.get("count").asInt();
            int productId = cartInfo.get("productId").asInt();
            Cart cart = new Cart();
            if (cartId == 0) {
                cart.setCreateTime(new Date());
            } else {
                cart.setCartId(cartId);
                cart.setLastEditTime(new Date());
            }
            cart.setCount(count);
            cart.setProductId(productId);
            int affectNum = cartService.addOrUpdateProduct(cart);
            if (affectNum == 1) {
                return new ResponseResultWrap(cart.getCartId());
            }
        } catch (Exception e) {

        }
    }

    @ResponseBody
    @RequestMapping(value = "/productList", method = RequestMethod.GET)
    public ResponseResultWrap<List<CartProductDTO>> getCartProductList () {
        try {
            int userId =  UserContextHolder.getUserInfo().getUserId().intValue();
            List<CartProductDTO> list = cartService.getCartProductListByUserId(userId);
            ResponseResultWrap.success(list);
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseResultWrap.fail("购物列表查询失败");
        }
    }



}
