package com.o2o.service;

import com.o2o.dto.ShopEvaluationDTO;
import com.o2o.vo.StoreReviewListVO;

import java.util.List;

public interface ShopEvaluationService {

    boolean addEvaluation (ShopEvaluationDTO shopEvaluationDTO);

    List<StoreReviewListVO> queryEvaluationListByShopId(Long shopId);

}
