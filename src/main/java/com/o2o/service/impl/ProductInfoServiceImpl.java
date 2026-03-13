package com.o2o.service.impl;

import com.o2o.dao.ProductInfoDao;
import com.o2o.dto.ProductBriefDTO;
import com.o2o.exceptions.BusinessException;
import com.o2o.service.ProductInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductInfoServiceImpl.class);

    @Autowired
    private ProductInfoDao productDao;

    public List<ProductBriefDTO> getProductListByShopId(Long shopId) {
        try {
            return productDao.getProductListByShopId(shopId);
        } catch (BusinessException e) {
            logger.warn("查询商铺列表失败：{}", e.toString());
            throw e;
        } catch (Exception e) {
            logger.error(e.toString());
            throw e;
        }
    }

}
