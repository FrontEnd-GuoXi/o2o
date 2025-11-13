package com.o2o.service;

import com.o2o.BaseTest;
import com.o2o.entity.Area;
import com.o2o.entity.PersonInfo;
import com.o2o.entity.Shop;
import com.o2o.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Date;

public class ShopServiceTest extends BaseTest {

    @Autowired
    ShopService shopService;


    @Test
    public void testShopService () {

        Shop shop = new Shop();
        shop.setShopName("字节");
        shop.setShopDesc("测试数据");
        shop.setShopAddr("广州市番禺区");
        shop.setPhone("18312673102");
        shop.setPriority(1);
        shop.setCreateTime(new Date());
        shop.setLastEditTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("第一条数据");
        shop.setShopImg("img");

        Area area = new Area();
        area.setAreaId(1);
        shop.setArea(area);

        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        shop.setOwner(owner);

        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(1L);
        shop.setShopCategory(shopCategory);

      //  shopService.addShop(shop,new File("D:/javaImages/raw/3.jpg"));
    }
}
