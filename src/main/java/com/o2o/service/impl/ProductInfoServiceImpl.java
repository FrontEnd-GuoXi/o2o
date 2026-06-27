package com.o2o.service.impl;

import com.o2o.dao.ProductInfoDao;
import com.o2o.dto.ProductBriefDTO;
import com.o2o.entity.Product;
import com.o2o.exceptions.BusinessException;
import com.o2o.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoDao productDao;

    public List<ProductBriefDTO> getProductListByShopId(Long shopId) {
        try {
            return productDao.getProductListByShopId(shopId);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("查询商品列表失败", e);
        }
    }

    public Product getProductByProductId (Long productId) {
        try {
            return productDao.getProductByProductId(productId);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("查询商品失败", e);
        }
    }

}
