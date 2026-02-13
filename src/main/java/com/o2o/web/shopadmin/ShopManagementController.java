package com.o2o.web.shopadmin;



import com.o2o.dto.AddShop;
import com.o2o.dto.PersonInfoDTO;
import com.o2o.dto.ShopDTO;
import com.o2o.entity.Area;
import com.o2o.entity.PersonInfo;
import com.o2o.entity.Shop;
import com.o2o.entity.ShopCategory;
import com.o2o.security.UserContextHolder;
import com.o2o.service.ShopService;
import com.o2o.util.ResponseResultWrap;
import com.o2o.vo.ShopVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

    private static final Logger logger = LoggerFactory.getLogger(ShopManagementController.class);

    @Autowired
    ShopService shopService;

    @ResponseBody
    @RequestMapping(value = "/registeredOrUpdateShop", method = RequestMethod.POST)
    public ResponseResultWrap<Object> addShop(@Valid AddShop addShopDTO, BindingResult result, @RequestParam(value = "shopImg", required = false) MultipartFile shopImg) {
        try{
            if (result.hasErrors()) {
                // 2. 获取第一条错误信息并返回给前端
                String errorMessage = result.getFieldError().getDefaultMessage();
                return ResponseResultWrap.fail(errorMessage);
            }
            Shop shop = new Shop();
            BeanUtils.copyProperties(addShopDTO, shop);
            shop.setShopId(Long.valueOf(addShopDTO.getShopId()));

            // 设置店铺类别
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(addShopDTO.getCategorySub());
            shop.setShopCategory(shopCategory);

            //设置店铺的区域
            Area area = new Area();
            area.setAreaId(addShopDTO.getArea());
            shop.setArea(area);

            // 设置用户
            PersonInfo personInfo = new PersonInfo();
            PersonInfoDTO personInfoDTO = UserContextHolder.getUserInfo();
            personInfo.setUserId(personInfoDTO.getUserId());
            shop.setOwner(personInfo);

            ShopDTO shopTransfer;
            if (shop.getShopId() != null) {
                // 编辑的逻辑
                if (shopImg != null) {
                    CommonsMultipartFile img = (CommonsMultipartFile) shopImg;
                    shopTransfer = shopService.updateShop(shop, img.getInputStream(), img.getOriginalFilename());
                } else {
                    shopTransfer = shopService.updateShop(shop, null ,null);
                }
            } else {
                // 新增的逻辑
                CommonsMultipartFile img = (CommonsMultipartFile) shopImg;
                shopTransfer = shopService.addShop(shop, img.getInputStream(), img.getOriginalFilename());
            }
            return ResponseResultWrap.success(shopTransfer.getStateInfo());
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseResultWrap.fail("店铺创建失败");
        }
    }






    @ResponseBody
    @RequestMapping(value = "/getShopList", method = RequestMethod.GET)
    public ResponseResultWrap<List<ShopVO>> queryShopList() {
        try {
            PersonInfoDTO userInfo = UserContextHolder.getUserInfo();
            Long userId = userInfo.getUserId();
            List<ShopVO> shopVOList = shopService.queryShopList(userId);
            return ResponseResultWrap.success(shopVOList);
        } catch(Exception e) {
            logger.error(e.toString());
            return ResponseResultWrap.fail("店铺列表查询失败");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/deleteShop", method = RequestMethod.GET)
    public ResponseResultWrap<Boolean> deleteShop (@RequestParam("shopId") Long shopId) {
        try {
           Boolean deleted = shopService.deleteShop(shopId);
           if (deleted) {
               return ResponseResultWrap.success(true, "店铺删除成功");
           } else {
               return ResponseResultWrap.fail(false,"店铺删除失败");
           }
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseResultWrap.fail(false, "店铺删除失败，系统异常");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/queryShopById", method = RequestMethod.GET)
    public ResponseResultWrap<ShopVO> queryShopById (@RequestParam("shopId") long shopId) {
        try {
            long userId = UserContextHolder.getUserInfo().getUserId();
            ShopVO shopVO = shopService.queryShopById(shopId, userId);
            return ResponseResultWrap.success(shopVO);
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseResultWrap.fail(null, e.toString());
        }
    }

}
