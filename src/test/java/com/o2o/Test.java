package com.o2o;

import com.o2o.entity.Shop;

public class Test {

    public static void main(String[] args) {
        Shop shop = new Shop();
        String str = null;
        Long id = Long.valueOf(str);
        System.out.println("id:"+ id);
        System.out.println("shopId:" + shop.getShopId());
        shop.setShopId(id);
    }

}
