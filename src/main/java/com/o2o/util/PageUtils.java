package com.o2o.util;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class PageUtils {
    /**
     * 将 PageInfo<Entity> 转换为 PageInfo<VO>，保留所有分页属性
     */
    public static <T, R> PageInfo<R> convert(PageInfo<T> sourcePage, List<R> targetList) {
        PageInfo<R> targetPage = new PageInfo<>();
        BeanUtils.copyProperties(sourcePage, targetPage);
        targetPage.setList(targetList);
        return targetPage;
    }
}
