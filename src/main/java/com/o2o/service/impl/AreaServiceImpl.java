package com.o2o.service.impl;
import com.alibaba.excel.EasyExcel;
import com.o2o.dto.AreaImportDTO;
import com.o2o.entity.Area;
import com.o2o.dao.AreaDao;
import com.o2o.exceptions.BusinessException;
import com.o2o.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    AreaDao areaDao;


    public List<Area> getAreaList () {
        return areaDao.getList();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public int importAreaBatch(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传的文件不能为空");
        }

        // 1. 同步读取解析 Excel
        List<AreaImportDTO> dtoList = EasyExcel.read(file.getInputStream()).head(AreaImportDTO.class)
                .sheet().doReadSync();

        if (dtoList == null || dtoList.isEmpty()) {
            throw new BusinessException("Excel内容为空，无法导入");
        }

        // 2. 数据清洗与业务校验
        List<Area> insertList = new ArrayList<>();
        Set<String> nameSet = new HashSet<>();

        for(int i =0; i<dtoList.size(); i++) {
            // 获取DTO
            AreaImportDTO dto = dtoList.get(i);
            // 第1行为表头
            int rowNum = i + 2;

            if (dto.getAreaName() == null || dto.getAreaName().trim().isEmpty()) {
                throw new BusinessException("第 " + rowNum + " 行：区域名称不能为空");
            }

            if (dto.getAreaName().length() > 200) {
                throw new BusinessException("第 " + rowNum + " 行：区域名称长度不能超过200字符");
            }

            if (!nameSet.add(dto.getAreaName().trim())) {
                throw new BusinessException("Excel内部存在重复的区域名称：" + dto.getAreaName());
            }

            Area area = new Area();
            area.setAreaName(dto.getAreaName().trim());
            area.setPriority(dto.getPriority() != null ? dto.getPriority() : 0);
            insertList.add(area);
        }

        return areaDao.bactchInsertArea(insertList);
    }
}
