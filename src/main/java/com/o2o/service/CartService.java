package com.o2o.service;

import com.o2o.dto.CartProductDTO;
import com.o2o.entity.Cart;

import java.util.List;

public interface CartService {

    int addOrUpdateProduct (Cart cart);
    int removeProductById (int productId, int userId);
    List<CartProductDTO> getCartProductListByUserId(int userId);

}
