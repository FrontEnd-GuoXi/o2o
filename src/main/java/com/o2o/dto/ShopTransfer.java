package com.o2o.dto;

import com.o2o.entity.Shop;
import com.o2o.enums.ShopStateEnum;

import java.util.List;

public class ShopTransfer {

    private int state;
    private String stateInfo;
    private Shop shop;
    private List<Shop> shopList;

    public ShopTransfer () {}

    public ShopTransfer (ShopStateEnum shopStateEnum) {
        this.state = shopStateEnum.getState();
        this.stateInfo = shopStateEnum.getStateInfo();
    }

    public ShopTransfer (ShopStateEnum shopStateEnum, Shop shop) {
        this.state = shopStateEnum.getState();
        this.stateInfo = shopStateEnum.getStateInfo();
        this.shop = shop;
    }

    public ShopTransfer (ShopStateEnum shopStateEnum, List<Shop> shopList) {
        this.state = shopStateEnum.getState();
        this.stateInfo = shopStateEnum.getStateInfo();
        this.shopList = shopList;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public Shop getShop() {
        return shop;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }
}
