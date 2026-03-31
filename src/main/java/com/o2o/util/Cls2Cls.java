package com.o2o.util;

import com.o2o.entity.Area;
import com.o2o.entity.PersonInfo;
import com.o2o.entity.Shop;
import com.o2o.entity.ShopCategory;
import com.o2o.vo.ShopVO;
import org.springframework.beans.BeanUtils;

public class Cls2Cls {

    public static ShopVO shopToShopVO (Shop shop, ShopVO shopVO) {
        BeanUtils.copyProperties(shop, shopVO);
        if (shop.getOwner() != null) {
            shopVO.setOwnerId(String.valueOf(shop.getOwner().getUserId()));
        }
        if (shop.getShopCategory() != null) {
            shopVO.setShopCategoryId(String.valueOf(shop.getShopCategory().getShopCategoryId()));
        }
        shopVO.setShopCategoryName(shop.getShopCategory() != null ? shop.getShopCategory().getShopCategoryName() : null);
        if (shop.getArea() != null) {
            shopVO.setAreaId(String.valueOf(shop.getArea().getAreaId()));
            shopVO.setAreaName(shop.getArea().getAreaName());
        }
        shopVO.setShopId(String.valueOf(shop.getShopId()));
        if (shop.getShopCategory() != null && shop.getShopCategory().getParent() != null) {
            shopVO.setShopCategoryParentId(String.valueOf(shop.getShopCategory().getParent().getShopCategoryId()));
        }
        return shopVO;
    }

    public static Shop shopVOToShop (ShopVO shopVO, Shop shop) {
        BeanUtils.copyProperties(shopVO, shop);
        shop.setShopId(Long.valueOf(shopVO.getShopId()));

        PersonInfo owner = new PersonInfo();
        owner.setUserId(Long.valueOf(shopVO.getOwnerId()));
        shop.setOwner(owner);

        Area area = new Area();
        area.setAreaId(Integer.valueOf(shopVO.getAreaId()));
        shop.setArea(area);

        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(Long.valueOf(shopVO.getShopCategoryId()));
        ShopCategory shopCategory1 = new ShopCategory();
        shopCategory1.setShopCategoryId(Long.valueOf(shopVO.getShopCategoryParentId()));
        shopCategory.setParent(shopCategory1);

        shop.setShopCategory(shopCategory);
        return shop;
    }

}
