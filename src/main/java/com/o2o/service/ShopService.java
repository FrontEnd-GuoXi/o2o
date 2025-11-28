package com.o2o.service;

import com.o2o.dto.ShopDTO;
import com.o2o.entity.Shop;

import java.io.InputStream;

public interface ShopService {

    public ShopDTO addShop (Shop shop, InputStream rawImg, String filePath);
    public ShopDTO updateShop (Shop shop, InputStream rawImg, String filePath);
}
