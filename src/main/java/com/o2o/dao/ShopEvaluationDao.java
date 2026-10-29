package com.o2o.dao;

import com.o2o.entity.ShopEvaluation;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ShopEvaluationDao {

    int addEvaluation(ShopEvaluation shopEvaluation);

    int updateShopScoreAndCount(@Param("shopId") Long shopId, @Param("newScore") BigDecimal newScore);

    List<ShopEvaluation> queryEvaluationListByShopId (Long shopId);


}
