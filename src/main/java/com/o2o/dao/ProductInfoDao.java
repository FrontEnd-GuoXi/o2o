package com.o2o.dao;

import com.o2o.dto.ProductBriefDTO;

import java.util.List;

public interface ProductInfoDao {
   public List<ProductBriefDTO> getProductListByShopId(Long shopId);
}
