package com.o2o.web.admin;

import com.o2o.entity.Area;
import com.o2o.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/area")
@ResponseBody
public class AreaController {

    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> getAreaList () {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result.put("list", areaService.getAreaList());
            result.put("success", true);
            result.put("msg", "查询成功");
            return result;
        } catch (Exception exception) {
            result.put("list", new ArrayList<Area>());
            result.put("success", false);
            result.put("msg", "查询失败");
            System.out.println("执行areaService.getAreaList异常---------" + exception.toString());
            return result;
        }
    }

}
