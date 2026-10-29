package com.o2o.service.impl;

import com.o2o.dao.RecommendDao;
import com.o2o.entity.Shop;
import com.o2o.service.RecommendService;
import com.o2o.vo.RecommendShopVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendServiceImpl implements RecommendService {

    private final RecommendDao recommendDao;

    public RecommendServiceImpl(RecommendDao recommendDao) {
        this.recommendDao = recommendDao;
    }


    @Override
    public List<RecommendShopVO> recommendByAvgScore() {
        List<Shop> shopList = recommendDao.recommendByAvgScore();
        List<RecommendShopVO> recommendShopVOS = shopList.stream().map(shop -> {
            RecommendShopVO recommendShopVO = new RecommendShopVO();
            BeanUtils.copyProperties(shop, recommendShopVO);
            if (shop.getShopCategory() != null) {
                if (shop.getShopCategory().getShopCategoryId() != null) {
                    recommendShopVO.setShopCategoryId(shop.getShopCategory().getShopCategoryId().toString());
                }
                recommendShopVO.setShopCategoryName(shop.getShopCategory().getShopCategoryName());
            }
            return recommendShopVO;
        }).collect(Collectors.toList());
        return recommendShopVOS;
    }
}
