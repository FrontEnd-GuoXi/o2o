package com.o2o.web;


import com.o2o.entity.Area;
import com.o2o.service.AreaService;
import com.o2o.util.ResponseResultWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/o2o/area")
public class AreaController {
    @Autowired
    AreaService areaService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseResultWrap<List<Area>> getAreaList () {
        List<Area> areaList = areaService.getAreaList();
        return ResponseResultWrap.success(areaList);
    }

}
