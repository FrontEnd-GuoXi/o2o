package com.o2o.service.impl;

import com.o2o.dao.CartDao;
import com.o2o.dto.CartProductDTO;
import com.o2o.entity.Cart;
import com.o2o.exceptions.BusinessException;
import com.o2o.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    CartDao cartDao;

    public int addOrUpdateProduct (Cart cart) {
        try {
           int affectRow = cartDao.addOrUpdateProduct(cart);
           return affectRow;
        } catch (BusinessException e) {
            logger.warn("向购物车插入或更新数据失败：{}", e.toString());
            throw e;
        } catch (Exception e) {
            logger.error(e.toString());
            throw e;
        }
    }

    public int removeProductById (int productId, int userId) {
        try {
            int affectRow = cartDao.removeProductById(productId, userId);
            return affectRow;
        } catch (BusinessException e) {
            logger.warn("删除商品失败：{}", e.toString());
            throw e;
        } catch (Exception e) {
            logger.error(e.toString());
            throw e;
        }
    }

    public List<CartProductDTO> getCartProductListByUserId(int userId) {
        try {
            List<CartProductDTO> list = cartDao.getCartProductListByUserId(userId);
            return list;
        } catch (BusinessException e) {
            logger.warn("查询购车商品列表失败：{}", e.toString());
            throw e;
        } catch (Exception e) {
            logger.error(e.toString());
            throw e;
        }
    }

}
