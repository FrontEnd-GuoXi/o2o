package com.o2o.web;


import com.o2o.entity.Area;
import com.o2o.service.AreaService;
import com.o2o.util.ResponseResultWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/area")
public class AreaController {
    @Autowired
    AreaService areaService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseResultWrap<List<Area>> getAreaList () {
        List<Area> areaList = areaService.getAreaList();
        return ResponseResultWrap.success(areaList);
    }

    @PostMapping("/import")
    public ResponseResultWrap<Integer> importAreas(@RequestParam("file")MultipartFile file) throws IOException {
        int count = areaService.importAreaBatch(file);
        return ResponseResultWrap.success(count, "成功导入" + count + "条区域数据");
    }

}
