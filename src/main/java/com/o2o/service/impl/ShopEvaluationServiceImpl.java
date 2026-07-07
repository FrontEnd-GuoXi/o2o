package com.o2o.service.impl;

import com.o2o.dao.ShopEvaluationDao;
import com.o2o.dto.ShopEvaluationDTO;
import com.o2o.entity.Order;
import com.o2o.entity.PersonInfo;
import com.o2o.entity.Shop;
import com.o2o.entity.ShopEvaluation;
import com.o2o.exceptions.BusinessException;
import com.o2o.service.ShopEvaluationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ShopEvaluationServiceImpl implements ShopEvaluationService {
    private final ShopEvaluationDao shopEvaluationDao;

    public ShopEvaluationServiceImpl (ShopEvaluationDao shopEvaluationDao) {
        this.shopEvaluationDao = shopEvaluationDao;
    }

    private BigDecimal calcAvgScore (BigDecimal totalScore, BigDecimal environmentScore, BigDecimal serviceScore) {
        BigDecimal totalWeight = new BigDecimal("0.6");
        BigDecimal environmentWeight = new BigDecimal("0.2");
        BigDecimal serviceWeight = new BigDecimal("0.2");
        BigDecimal avgScore = totalScore.multiply(totalWeight).add(environmentScore.multiply(environmentWeight))
                .add(serviceScore.multiply(serviceWeight));
        avgScore = avgScore.setScale(2, RoundingMode.HALF_UP);
        return avgScore;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addEvaluation(ShopEvaluationDTO shopEvaluationDTO) {
        ShopEvaluation shopEvaluation = new ShopEvaluation();
        BeanUtils.copyProperties(shopEvaluationDTO, shopEvaluation);
        Shop shop = new Shop();
        shop.setShopId(shopEvaluationDTO.getShopId());
        shopEvaluation.setShop(shop);
        BigDecimal totalScore = BigDecimal.valueOf(shopEvaluation.getTotalScore());
        BigDecimal environmentScore = BigDecimal.valueOf(shopEvaluation.getEnvironmentScore());
        BigDecimal serviceScore = BigDecimal.valueOf(shopEvaluation.getServiceScore());
        BigDecimal avgScore = calcAvgScore(totalScore, environmentScore, serviceScore);
        shop.setAvgScore(avgScore);


        PersonInfo userInfo = new PersonInfo();
        userInfo.setUserId(shopEvaluationDTO.getUserId());
        shopEvaluation.setUserInfo(userInfo);

        Order order = new Order();
        order.setOrderId(shopEvaluationDTO.getOrderId());
        shopEvaluation.setOrder(order);

        int evaRows = shopEvaluationDao.addEvaluation(shopEvaluation);
        int scoreRows = shopEvaluationDao.updateShopScoreAndCount(shop.getShopId(), shop.getAvgScore());
        if (evaRows > 0 && scoreRows > 0) {
            return true;
        } else {
            throw new BusinessException("评论添加失败。");
        }
    }
}
