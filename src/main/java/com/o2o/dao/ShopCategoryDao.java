package com.o2o.dao;

import com.o2o.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryDao {

    List<ShopCategory> queryShopCategoryByParentId (long parentId);
}
