package com.o2o.service;

import com.o2o.BaseTest;
import com.o2o.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import static org.junit.Assert.*;

public class AreaServiceTest extends BaseTest {

    @Autowired
    private AreaService areaService;

    @Test
    public void testGetAreaList () {
        List<Area> areaList = areaService.getAreaList();
        assertEquals(3, areaList.size());
    }

}
