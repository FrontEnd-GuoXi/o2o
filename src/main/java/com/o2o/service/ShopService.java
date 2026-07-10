package com.o2o.service;

import com.o2o.dto.ShopDTO;
import com.o2o.entity.Shop;
import com.o2o.vo.ShopVO;
import org.apache.ibatis.annotations.Param;

import java.io.InputStream;
import java.util.List;

public interface ShopService {

     ShopDTO addShop (Shop shop, InputStream rawImg, String filePath);
     ShopDTO updateShop (Shop shop, InputStream rawImg, String filePath);
     List<ShopVO> queryShopList(Long userId);
     Boolean deleteShop (Long shopId);
     ShopVO queryShopById(long shopId, long userId);
     List<ShopVO> queryShopListByCategoryId(Long categoryId);

     // 普通用户通过推荐访问店铺详情时的接口
     ShopVO getShopDetailById (Long shopId);
}
