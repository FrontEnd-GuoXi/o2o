package com.o2o.dao;

import com.o2o.dto.ProductBriefDTO;
import com.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductInfoDao {
    List<ProductBriefDTO> getProductListByShopId(Long shopId);

    Product getProductByProductId (Long productId);

    int updateProductInventory (@Param("product") Product product, @Param("purchaseQuantity") Integer purchaseQuantity);
}
