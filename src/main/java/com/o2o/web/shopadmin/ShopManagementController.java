package com.o2o.web.shopadmin;



import com.o2o.dto.AddShop;
import com.o2o.dto.PersonInfoDTO;
import com.o2o.dto.ShopDTO;
import com.o2o.entity.Area;
import com.o2o.entity.PersonInfo;
import com.o2o.entity.Shop;
import com.o2o.entity.ShopCategory;
import com.o2o.enums.ShopStateEnum;
import com.o2o.exceptions.BusinessException;
import com.o2o.security.UserContextHolder;
import com.o2o.service.ShopService;
import com.o2o.util.ResponseResultWrap;
import com.o2o.vo.ShopVO;
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

    @Autowired
    ShopService shopService;

    @ResponseBody
    @RequestMapping(value = "/registeredOrUpdateShop", method = RequestMethod.POST)
    public ResponseResultWrap<Object> addShop(@Valid AddShop addShopDTO, BindingResult result, @RequestParam(value = "shopImg", required = false) MultipartFile shopImg) {
        if (result.hasErrors()) {
            String errorMessage = result.getFieldError().getDefaultMessage();
            throw new BusinessException(errorMessage);
        }

        Shop shop = new Shop();
        BeanUtils.copyProperties(addShopDTO, shop);
        if (addShopDTO.getShopId() != null && !addShopDTO.getShopId().trim().isEmpty()) {
            shop.setShopId(Long.valueOf(addShopDTO.getShopId()));
        }

        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(addShopDTO.getCategorySub());
        shop.setShopCategory(shopCategory);

        Area area = new Area();
        area.setAreaId(addShopDTO.getArea());
        shop.setArea(area);

        PersonInfo personInfo = new PersonInfo();
        PersonInfoDTO personInfoDTO = UserContextHolder.getUserInfo();
        personInfo.setUserId(personInfoDTO.getUserId());
        shop.setOwner(personInfo);

        ShopDTO shopTransfer;
        if (shop.getShopId() != null) {
            if (shopImg != null) {
                CommonsMultipartFile img = (CommonsMultipartFile) shopImg;
                shopTransfer = shopService.updateShop(shop, img.getInputStream(), img.getOriginalFilename());
            } else {
                shopTransfer = shopService.updateShop(shop, null ,null);
            }
        } else {
            if (shopImg == null) {
                throw new BusinessException("新增店铺时必须上传店铺图片");
            }
            CommonsMultipartFile img = (CommonsMultipartFile) shopImg;
            shopTransfer = shopService.addShop(shop, img.getInputStream(), img.getOriginalFilename());
        }

        validateShopOperation(shopTransfer);
        return ResponseResultWrap.success(shopTransfer.getStateInfo());
    }






    @ResponseBody
    @RequestMapping(value = "/getShopList", method = RequestMethod.GET)
    public ResponseResultWrap<List<ShopVO>> queryShopList() {
        PersonInfoDTO userInfo = UserContextHolder.getUserInfo();
        Long userId = userInfo.getUserId();
        List<ShopVO> shopVOList = shopService.queryShopList(userId);
        return ResponseResultWrap.success(shopVOList);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteShop", method = RequestMethod.GET)
    public ResponseResultWrap<Boolean> deleteShop (@RequestParam("shopId") Long shopId) {
        Boolean deleted = shopService.deleteShop(shopId);
        if (!deleted) {
            throw new BusinessException("店铺删除失败");
        }
        return ResponseResultWrap.success(true, "店铺删除成功");
    }

    @ResponseBody
    @RequestMapping(value = "/queryShopById", method = RequestMethod.GET)
    public ResponseResultWrap<ShopVO> queryShopById (@RequestParam("shopId") long shopId) {
        long userId = UserContextHolder.getUserInfo().getUserId();
        ShopVO shopVO = shopService.queryShopById(shopId, userId);
        return ResponseResultWrap.success(shopVO);
    }

    private void validateShopOperation(ShopDTO shopTransfer) {
        if (shopTransfer == null) {
            throw new BusinessException("店铺操作失败");
        }

        int state = shopTransfer.getState();
        if (state != ShopStateEnum.CHECK.getState()
                && state != ShopStateEnum.SUCCESS.getState()
                && state != ShopStateEnum.PASS.getState()) {
            throw new BusinessException(shopTransfer.getStateInfo());
        }
    }

}
