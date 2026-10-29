package com.o2o.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class AreaImportDTO {

    @ExcelProperty(value = "区域名称", index = 0)
    private String areaName;

    @ExcelProperty(value = "权重优先级", index = 1)
    private Integer priority;

}
