package com.o2o.service;

import com.o2o.dto.ProductBriefDTO;

import java.util.List;

public interface ProductInfoService {

     List<ProductBriefDTO> getProductListByShopId(Long ShopId);

}
