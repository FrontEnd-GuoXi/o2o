package com.o2o.service;

import com.o2o.dto.ProductBriefDTO;
import com.o2o.entity.Product;

import java.util.List;

public interface ProductInfoService {

     List<ProductBriefDTO> getProductListByShopId(Long ShopId);

      Product getProductByProductId (Long productId);

}
