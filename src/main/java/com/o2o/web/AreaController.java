package com.o2o.web;


import com.o2o.entity.Area;
import com.o2o.service.AreaService;
import com.o2o.util.ResponseResultWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/area")
public class AreaController {
    private static final Logger logger = LoggerFactory.getLogger(AreaController.class);

    @Autowired
    AreaService areaService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseResultWrap<List<Area>> getAreaList () {
        try {
            List<Area> areaList = areaService.getAreaList();
            return ResponseResultWrap.success(areaList);
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseResultWrap.fail("区域查询失败");
        }
    }

}
