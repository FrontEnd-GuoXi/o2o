package com.o2o.service.impl;

import com.o2o.dao.ShopDao;
import com.o2o.dto.ShopDTO;
import com.o2o.entity.Shop;
import com.o2o.enums.ShopStateEnum;
import com.o2o.exceptions.BusinessException;
import com.o2o.exceptions.ShopOperationException;
import com.o2o.service.ShopService;
import com.o2o.util.ImageUtil;
import com.o2o.vo.ShopVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl implements ShopService {
    private static final Logger logger = LoggerFactory.getLogger(ShopServiceImpl.class);

    @Autowired
    private ShopDao shopDao;

    @Transactional
    public ShopDTO addShop (Shop shop, InputStream rawImg, String filePath) {
        ShopDTO shopTransfer;

        if (shop == null) {
            shopTransfer = new ShopDTO(ShopStateEnum.NULL_SHOP);
        } else {
            try {
                shop.setEnableStatus(0);
                shop.setCreateTime(new Date());
                shop.setLastEditTime(new Date());
                int affectedRows = shopDao.addShop(shop);
                long shopId = shop.getShopId();

                if (affectedRows != -1) {
                    try {
                        String dest = ImageUtil.genImgAndSave(rawImg, shopId, filePath);
                        String pathWithoutDrive = dest.replaceAll("^[a-zA-Z]:", "");
                        shop.setShopImg(pathWithoutDrive);
                        shopDao.updateShop(shop);
                        shopTransfer = new ShopDTO(ShopStateEnum.CHECK, shop);
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

    @Transactional
    public ShopDTO updateShop (Shop shop, InputStream rawImg, String filePath) {
        try {
            if (shop == null || shop.getShopId() == null) {
                return new ShopDTO(ShopStateEnum.NULL_SHOP);
            }

            if (rawImg != null && filePath != null && ! "".equals(filePath)) {
                Boolean deleteResult = ImageUtil.deleteAllPictruesInTheFolder(ImageUtil.getBasePath(shop.getShopId()));
                if (deleteResult) {
                    String dest = addImg(shop.getShopId(), rawImg, filePath);
                    String pathWithoutDrive = dest.replaceAll("^[a-zA-Z]:", "");
                    shop.setShopImg(pathWithoutDrive);
                } else {
                    throw new ShopOperationException("店铺更新失败");
                }
            }


            int effectedNum = shopDao.updateShop(shop);
            if (effectedNum <= 0) {
                throw  new ShopOperationException("店铺更新失败");
            }

            return new ShopDTO(ShopStateEnum.SUCCESS, shop);
        } catch (ShopOperationException e) {
            logger.warn("店铺更新失败：{}", e.toString());
            throw new ShopOperationException("店铺更新失败");
        } catch (Exception e) {
            logger.error(e.toString());
            throw new ShopOperationException("店铺更新失败");
        }
    }

    public String addImg (Long shopId, InputStream rawImg, String filePath) {
        return ImageUtil.genImgAndSave(rawImg, shopId, filePath);
    }


    public List<ShopVO> queryShopList (Long userId) {
        try {
            List<Shop> shopList = shopDao.checkYourShopList(userId);
            List<ShopVO> shopVOList = shopList.stream().map(shop -> {
                ShopVO shopVO = new ShopVO();
                this.shopToShopVO(shop, shopVO);
                return shopVO;
            }).collect(Collectors.toList());
            return shopVOList;
        } catch (BusinessException e) {
            logger.warn("店铺列表查询失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.toString());
            throw  new BusinessException("店铺列表查询失败");
        }

    }

    private ShopVO shopToShopVO (Shop shop, ShopVO shopVO) {
        BeanUtils.copyProperties(shop, shopVO);
        shopVO.setOwnerId(String.valueOf(shop.getOwner().getUserId()));
        shopVO.setShopCategoryId(String.valueOf(shop.getShopCategory().getShopCategoryId()));
        shopVO.setShopCategoryName(shop.getShopCategory().getShopCategoryName());
        shopVO.setAreaId(String.valueOf(shop.getArea().getAreaId()));
        shopVO.setShopId(String.valueOf(shop.getShopId()));
        shopVO.setShopCategoryParentId(String.valueOf((shop.getShopCategory().getParent().getShopCategoryId())));
        return shopVO;
    }

    public Boolean deleteShop(Long shopId) {
        try {
            int affectedRows = shopDao.deleteShopById(shopId);
            return affectedRows > 0;
        } catch (BusinessException e) {
            logger.warn("店铺删除失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.toString());
            throw new BusinessException("店铺删除失败");
        }
    }

    public ShopVO queryShopById (long shopId, long userId) {
        try {
            Shop shop = shopDao.queryShopById(shopId, userId);
            ShopVO shopVO = new ShopVO();
            this.shopToShopVO(shop, shopVO);
            return shopVO;
        } catch (BusinessException e) {
            logger.warn("店铺查询失败：{}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.toString());
            throw new BusinessException("店铺查询失败");
        }
    }

}
