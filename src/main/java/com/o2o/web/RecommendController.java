package com.o2o.web;

import com.o2o.exceptions.BusinessException;
import com.o2o.service.RecommendService;
import com.o2o.util.AboutString;
import com.o2o.util.ResponseResultWrap;
import com.o2o.vo.RecommendShopVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/recommend")
public class RecommendController {

    RecommendService recommendService;

    public RecommendController (RecommendService recommendService) {
        this.recommendService = recommendService;
    }

    @RequestMapping(value = "/shops", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResultWrap<List<RecommendShopVO>> getTheTopFiveShops (@RequestParam("rankingType") String rankingType) {
        if (AboutString.isEmpty(rankingType)) {
            throw new BusinessException("排名类型不能为空");
        } else {
            if (rankingType.equals("avg_score")) {
                List<RecommendShopVO> recommendShopVOS = recommendService.recommendByAvgScore();
                return ResponseResultWrap.success(recommendShopVOS);
            } else {
                throw new BusinessException("未知的排名类型");
            }
        }
    }

}
