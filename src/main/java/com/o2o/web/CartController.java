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
import org.springframework.web.bind.annotation.*;

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
            Date now = new Date();
            if (cartId == 0) {
                cart.setCreateTime(now);
            } else {
                cart.setCartId(cartId);
            }
            cart.setLastEditTime(now);
            cart.setCount(count);
            cart.setProductId(productId);
            // 从 UserContextHolder 获取当前登录用户的 userId
            int userId = UserContextHolder.getUserInfo().getUserId().intValue();
            cart.setUserId(userId);
            int affectRow = cartService.addOrUpdateProduct(cart);
            if (affectRow == 1 || affectRow == 2) {
                return ResponseResultWrap.success(cart.getCartId());
            } else {
                return ResponseResultWrap.fail("添加产品或更新产品失败");
            }
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseResultWrap.fail("添加产品或更新产品失败");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/productList", method = RequestMethod.GET)
    public ResponseResultWrap<List<CartProductDTO>> getCartProductList () {
        try {
            int userId = UserContextHolder.getUserInfo().getUserId().intValue();
            List<CartProductDTO> list = cartService.getCartProductListByUserId(userId);
            return ResponseResultWrap.success(list);
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseResultWrap.fail("购物列表查询失败");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/removeProduct", method = RequestMethod.GET)
    public ResponseResultWrap<Boolean> removeProductById (@RequestParam("productId") int productId) {
        try {
            int userId = UserContextHolder.getUserInfo().getUserId().intValue();
            int affectRow = cartService.removeProductById(productId, userId);
            if (affectRow == 1) {
                return ResponseResultWrap.success("删除成功");
            } else {
                return ResponseResultWrap.fail("删除失败");
            }
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseResultWrap.fail("删除失败");
        }
    }





}
