package com.o2o.service;

import com.o2o.dto.ShopTransfer;
import com.o2o.entity.Shop;

import java.io.File;

public interface ShopService {

    public ShopTransfer addShop (Shop shop, File rawImg);
    public ShopTransfer updateShop (Shop shop, File rawImg);
}
