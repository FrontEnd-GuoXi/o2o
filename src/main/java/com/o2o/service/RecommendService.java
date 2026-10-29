package com.o2o.service;

import com.o2o.vo.RecommendShopVO;

import java.util.List;

public interface RecommendService {

    List<RecommendShopVO> recommendByAvgScore();

}
