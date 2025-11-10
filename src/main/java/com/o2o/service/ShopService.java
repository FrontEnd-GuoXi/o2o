package com.o2o.service;

import com.o2o.dto.ShopTransfer;
import com.o2o.entity.Shop;

import java.io.File;
import java.io.InputStream;

public interface ShopService {

    public ShopTransfer addShop (Shop shop, InputStream rawImg, String imgName);
    public ShopTransfer updateShop (Shop shop, InputStream rawImg, String imgName);
}
