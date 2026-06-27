package com.o2o.service.impl;

import com.o2o.dao.CartDao;
import com.o2o.dto.CartProductDTO;
import com.o2o.entity.Cart;
import com.o2o.exceptions.BusinessException;
import com.o2o.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartDao cartDao;

    @Transactional
    public int addOrUpdateProduct (Cart cart) {
        try {
            return cartDao.addOrUpdateProduct(cart);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("添加产品或更新产品失败", e);
        }
    }

    public int removeProductById (Integer productId, int userId) {
        try {
            return cartDao.removeProductById(productId, userId);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("删除商品失败", e);
        }
    }

    public List<CartProductDTO> getCartProductListByUserId(int userId) {
        try {
            return cartDao.getCartProductListByUserId(userId);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("购物列表查询失败", e);
        }
    }

}
