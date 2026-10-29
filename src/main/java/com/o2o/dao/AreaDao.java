package com.o2o.dao;

import com.o2o.entity.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AreaDao {
    List<Area> getList();

    /**
     * 批量插入区域信息
     *
     * @param areaList 区域实体列表
     * @return int 成功插入/影响的数据库行数 (Affected Rows)
     */
    int bactchInsertArea(@Param("areaList") List<Area> areaList);
}
