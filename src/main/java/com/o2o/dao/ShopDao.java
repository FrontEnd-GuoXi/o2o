package com.o2o.dao;

import com.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ShopDao {

    int addShop (Shop shop);

    int updateShop (Shop shop);

    Shop queryShopById (@Param("shopId") long shopId, @Param("userId") long userId);

    List<Shop> checkYourShopList (long userId);

    int deleteShopById (long id);

}
