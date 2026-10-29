package com.o2o.web;

import com.o2o.dto.ShopEvaluationDTO;
import com.o2o.exceptions.BusinessException;
import com.o2o.service.ShopEvaluationService;
import com.o2o.util.ResponseResultWrap;
import com.o2o.vo.StoreReviewListVO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/evaluation")
public class EvaluationController {

     ShopEvaluationService shopEvaluationService;

     public EvaluationController (ShopEvaluationService shopEvaluationService) {
          this.shopEvaluationService = shopEvaluationService;
     }


     @PostMapping("/add")
     @ResponseBody
     public ResponseResultWrap<Boolean> addEvaluation (
             @Valid @RequestBody ShopEvaluationDTO shopEvaluationDTO, BindingResult result) {
          // 1. 检查是否有参数校验错误
          if (result.hasErrors()) {
               String errStr = result.getFieldErrors()
                       .stream()
                       .map(error -> error.getField() + "：" + error.getDefaultMessage())
                       .collect(Collectors.joining("；"));

               // 2. 抛出你的自定义业务异常，直接交给全局异常处理器（@RestControllerAdvice）去统一打包返回
               throw new BusinessException(errStr);
          }

          boolean success = shopEvaluationService.addEvaluation(shopEvaluationDTO);
          return ResponseResultWrap.success(success);
     }

     @GetMapping("/queryList")
     @ResponseBody
     public ResponseResultWrap<List<StoreReviewListVO>> queryEvaluationListByShopId (@RequestParam("shopId") Long shopId) {
          if (shopId == null) {
               throw new BusinessException("shopId不能为空");
          }

          List<StoreReviewListVO> storeReviewListVOS = shopEvaluationService.queryEvaluationListByShopId(shopId);
          return ResponseResultWrap.success(storeReviewListVOS);
     }

}
