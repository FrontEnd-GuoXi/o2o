package com.o2o.service;

import com.o2o.dto.ShopDTO;
import com.o2o.entity.Shop;
import com.o2o.vo.ShopVO;

import java.io.InputStream;
import java.util.List;

public interface ShopService {

     ShopDTO addShop (Shop shop, InputStream rawImg, String filePath);
     ShopDTO updateShop (Shop shop, InputStream rawImg, String filePath);
     List<ShopVO> queryShopList(Long userId);
     Boolean deleteShop (Long shopId);
     ShopVO queryShopById(long shopId, long userId);
     List<ShopVO> queryShopListByCategoryId(Long categoryId);
}
