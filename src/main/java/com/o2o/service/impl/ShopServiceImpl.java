package com.o2o.service.impl;

import com.o2o.dao.ShopDao;
import com.o2o.dto.ShopTransfer;
import com.o2o.entity.Shop;
import com.o2o.enums.ShopStateEnum;
import com.o2o.exceptions.ShopOperationException;
import com.o2o.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

    @Transactional
    public ShopTransfer addShop (Shop shop, File rawImg) {
        ShopTransfer shopTransfer = null;

        if (shop == null) {
            shopTransfer = new ShopTransfer(ShopStateEnum.NULL_SHOP);
        } else {
            int shopId = shopDao.addShop(shop);

            if (shopId != -1) {

            } else {
                throw new ShopOperationException("店铺新建失败");
            }
        }



        return shopTransfer;
    }

    public ShopTransfer updateShop (Shop shop, File rawImg) {

        return null;
    }

    public String addImg (int shopId) {
        return "";
    }

}
