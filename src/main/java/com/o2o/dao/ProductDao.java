package com.o2o.dao;

import com.o2o.entity.Product;

import java.util.List;

public interface ProductDao {
   public List<Product> getProductListByShopId(Long shopId);
}
