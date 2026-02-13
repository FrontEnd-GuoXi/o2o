package com.o2o.service.impl;
import com.o2o.entity.Area;
import com.o2o.dao.AreaDao;
import com.o2o.exceptions.BusinessException;
import com.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {
    private static final Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);

    @Autowired
    AreaDao areaDao;


    public List<Area> getAreaList () {
        try {
            return areaDao.getList();
        } catch (BusinessException e) {
            logger.warn("区域查询失败：{}", e.toString());
            throw e;
        } catch (Exception e) {
            logger.error(e.toString());
            throw e;
        }
    }
}
