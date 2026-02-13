package com.o2o.service;

import com.o2o.dto.ShopDTO;
import com.o2o.entity.Shop;
import com.o2o.vo.ShopVO;

import java.io.InputStream;
import java.util.List;

public interface ShopService {

    public ShopDTO addShop (Shop shop, InputStream rawImg, String filePath);
    public ShopDTO updateShop (Shop shop, InputStream rawImg, String filePath);
    public List<ShopVO> queryShopList(Long userId);
    public Boolean deleteShop (Long shopId);
    public ShopVO queryShopById(long shopId, long userId);
}
