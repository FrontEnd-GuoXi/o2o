package com.o2o.web.product;

import com.o2o.dto.ProductBriefDTO;
import com.o2o.service.ProductInfoService;
import com.o2o.util.ResponseResultWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/productInfo")
public class ProductInfoController {

    private static final Logger logger = LoggerFactory.getLogger(ProductInfoController.class);

    @Autowired
    private ProductInfoService productInfoService;

    @ResponseBody
    @RequestMapping(value = "/getProductBriefListByShopId", method = RequestMethod.GET)
    public ResponseResultWrap<List<ProductBriefDTO>> getProductBriefListByShopId (@RequestParam("shopId") Long shopId) {
        try {
            List<ProductBriefDTO> list = productInfoService.getProductListByShopId(shopId);
            return ResponseResultWrap.success(list);
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseResultWrap.fail(null, "商品列表查询失败");
        }
    }

}
