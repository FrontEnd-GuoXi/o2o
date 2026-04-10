package com.o2o.dao;

import com.o2o.dto.CartProductDTO;
import com.o2o.entity.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartDao {

    int addOrUpdateProduct(Cart cart);
    int removeProductById(@Param("productId") Integer productId, @Param("userId") int userId);
    List<CartProductDTO> getCartProductListByUserId(@Param("userId") int userId);

}
