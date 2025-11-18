package com.o2o.dao;

import com.o2o.entity.Shop;


public interface ShopDao {

    int addShop (Shop shop);

    int updateShop (Shop shop);

    Shop queryShopById (long id);



}
