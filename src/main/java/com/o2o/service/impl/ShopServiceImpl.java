package com.o2o.service.impl;

import com.o2o.dao.ShopDao;
import com.o2o.dto.ShopTransfer;
import com.o2o.entity.Shop;
import com.o2o.enums.ShopStateEnum;
import com.o2o.exceptions.ShopOperationException;
import com.o2o.service.ShopService;
import com.o2o.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public class ShopServiceImpl implements ShopService {
    private static final Logger logger = LoggerFactory.getLogger(ShopServiceImpl.class);

    @Autowired
    private ShopDao shopDao;

    @Transactional
    public ShopTransfer addShop (Shop shop, File rawImg) {
        ShopTransfer shopTransfer;

        if (shop == null) {
            shopTransfer = new ShopTransfer(ShopStateEnum.NULL_SHOP);
        } else {
            try {
                shop.setEnableStatus(0);
                shop.setCreateTime(new Date());
                shop.setLastEditTime(new Date());
                long shopId = (long) shopDao.addShop(shop);

                if (shopId != -1) {
                    try {
                        String dest = ImageUtil.genImgAndSave(rawImg, shopId);
                        shop.setShopImg(dest);
                        shopDao.updateShop(shop);
                        updateShop(shop, rawImg);
                        shopTransfer = new ShopTransfer(ShopStateEnum.CHECK, shop);
                    } catch (RuntimeException e) {
                        logger.error(e.toString());
                        throw new ShopOperationException("店铺新建失败");
                    }

                } else {
                    throw new ShopOperationException("店铺新建失败");
                }
            } catch (Exception e) {
                throw new ShopOperationException("店铺新建失败");
            }

        }



        return shopTransfer;
    }

    public ShopTransfer updateShop (Shop shop, File rawImg) {
        String dest = addImg(shop.getShopId(), rawImg);
        shop.setShopImg(dest);
        shopDao.updateShop(shop);

        return new ShopTransfer(ShopStateEnum.SUCCESS, shop);
    }

    public String addImg (Long shopId, File rawImg) {
        return ImageUtil.genImgAndSave(rawImg, shopId);
    }

}
