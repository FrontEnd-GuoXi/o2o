package com.o2o.service.impl;

import com.o2o.dao.ShopEvaluationDao;
import com.o2o.dto.ShopEvaluationDTO;
import com.o2o.entity.Order;
import com.o2o.entity.PersonInfo;
import com.o2o.entity.Shop;
import com.o2o.entity.ShopEvaluation;
import com.o2o.exceptions.BusinessException;
import com.o2o.service.ShopEvaluationService;
import com.o2o.vo.StoreReviewListVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

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
            throw new BusinessException("评价添加失败。");
        }
    }

    @Override
    public List<StoreReviewListVO> queryEvaluationListByShopId(Long shopId) {
        List<ShopEvaluation> shopEvaluationList = shopEvaluationDao.queryEvaluationListByShopId(shopId);
        List<StoreReviewListVO> storeReviewListVOS = shopEvaluationList.stream().map(shopEvaluation -> {
            StoreReviewListVO storeReviewListVO = new StoreReviewListVO();
            BeanUtils.copyProperties(shopEvaluation, storeReviewListVO);
            storeReviewListVO.setProfileImg(shopEvaluation.getUserInfo().getProfileImg());
            storeReviewListVO.setName(shopEvaluation.getUserInfo().getName());
            return storeReviewListVO;
        }).collect(Collectors.toList());
        return storeReviewListVOS;
    }
}
