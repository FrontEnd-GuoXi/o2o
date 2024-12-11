package com.o2o.dao;

import com.o2o.BaseTest;
import com.o2o.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class AreaDaoTest extends BaseTest {

    @Autowired
    private AreaDao areaDao;

    @Test
    public void TestGetArea () {
        List<Area> areaList = areaDao.getList();
        assertEquals(3, areaList.size());
    }
}

