package com.o2o.service;

import com.o2o.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {

    public List<ShopCategory> queryShopCategoryByParentId (long parentId);

}
