package com.o2o.dao;

import com.o2o.BaseTest;
import com.o2o.entity.Area;
import com.o2o.entity.PersonInfo;
import com.o2o.entity.Shop;
import com.o2o.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ShopDaoTest extends BaseTest {

    @Autowired
    ShopDao shopDao;

    @Test
    public void testAddShop () {
        Shop shop = new Shop();
        shop.setShopName("腾讯2");
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

        int primaryKey =  shopDao.addShop(shop);
        assertNotEquals(-1, primaryKey);
    }

    @Test
    public void testUpdateShop () {
        Shop shop = new Shop();
        shop.setShopId(2L);
        shop.setShopName("阿里巴巴");
        shop.setLastEditTime(new Date());

        int affectedRows = shopDao.updateShop(shop);
        assertEquals(1, affectedRows);
    }

    @Test
    public void testSelectShop () {
        Shop shop = shopDao.queryShopById(2);
    }

}
