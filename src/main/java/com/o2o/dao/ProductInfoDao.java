package com.o2o.dao;

import com.o2o.dto.ProductBriefDTO;
import com.o2o.entity.Product;

import java.util.List;

public interface ProductInfoDao {
    List<ProductBriefDTO> getProductListByShopId(Long shopId);

    Product getProductByProductId (Long productId);
}
