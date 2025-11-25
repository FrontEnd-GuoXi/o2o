package com.o2o.web.shopadmin;


import com.o2o.entity.ShopCategory;
import com.o2o.service.ShopCategoryService;
import com.o2o.util.HttpServletRequestUtil;
import com.o2o.util.ResponseResultWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;



@Controller
@RequestMapping("/shopInfo")
public class ShopInfo {

    private static final Logger logger = LoggerFactory.getLogger(ShopInfo.class);

    @Autowired
    ShopCategoryService shopCategoryService;

    @ResponseBody
    @RequestMapping(value = "/getShopCategoryByParentId", method = RequestMethod.GET)
    public ResponseResultWrap<List<ShopCategory>> getShopCategoryByParentId (HttpServletRequest request) {
        try {
            long parentId = HttpServletRequestUtil.getInt(request,"parentId");
            List<ShopCategory> shopCategoryList = shopCategoryService.queryShopCategoryByParentId(parentId);
            return ResponseResultWrap.success(shopCategoryList);
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseResultWrap.fail(e.toString());
        }

    }

}
