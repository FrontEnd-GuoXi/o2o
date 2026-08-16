package com.o2o.service;

import com.o2o.entity.Area;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AreaService {
    List<Area> getAreaList();

    int importAreaBatch(MultipartFile file) throws IOException;
}
