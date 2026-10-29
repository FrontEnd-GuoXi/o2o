package com.o2o.web.shop;


import com.o2o.entity.ShopCategory;
import com.o2o.exceptions.BusinessException;
import com.o2o.security.UserContextHolder;
import com.o2o.service.ShopCategoryService;
import com.o2o.service.ShopService;
import com.o2o.util.HttpServletRequestUtil;
import com.o2o.util.ResponseResultWrap;
import com.o2o.vo.ShopVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;



@Controller
@RequestMapping("/shopInfo")
public class ShopInfoController {

    private static final Logger logger = LoggerFactory.getLogger(ShopInfoController.class);

    @Autowired
    ShopCategoryService shopCategoryService;

    @Autowired
    ShopService shopService;

    @ResponseBody
    @RequestMapping(value = "/getShopCategoryByParentId", method = RequestMethod.GET)
    public ResponseResultWrap<List<ShopCategory>> getShopCategoryByParentId (HttpServletRequest request) {
            int parentId = HttpServletRequestUtil.getInt(request,"parentId");
            List<ShopCategory> shopCategoryList = shopCategoryService.queryShopCategoryByParentId(parentId);
            return ResponseResultWrap.success(shopCategoryList);
    }

    @ResponseBody
    @RequestMapping(value = "queryShopListByCategoryId", method = RequestMethod.GET)
    public ResponseResultWrap<List<ShopVO>> queryShopListByCategoryId (@RequestParam("categoryId") Long categoryId ) {
            List<ShopVO> shopVOList = shopService.queryShopListByCategoryId(categoryId);
            return ResponseResultWrap.success(shopVOList);
    }



    @ResponseBody
    @GetMapping("/getShopDetailById")
    public ResponseResultWrap<ShopVO> getShopDetailById (@RequestParam("shopId") Long shopId) {
        if (shopId == null) {
            throw new BusinessException("shopId不能为空");
        }
        ShopVO shopVO = shopService.getShopDetailById(shopId);
        return ResponseResultWrap.success(shopVO);
    }


}
