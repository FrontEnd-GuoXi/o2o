package com.o2o.entity;

import lombok.Data;

import java.util.Date;

/**
 * 店铺评价实体类
 * 对应数据库表 tb_shop_evaluation
 */
@Data
public class ShopEvaluation {

    // 主键ID
    private Integer evaluationId;
    // 关联店铺ID
    private Shop shop;
    // 关联用户ID
    private PersonInfo userInfo;
    // 关联订单ID
    private Order order;
    // 综合评分(1-5分)
    private Integer totalScore;
    // 服务评分(1-5分)
    private Integer serviceScore;
    // 环境评分(1-5分)
    private Integer environmentScore;
    // 评价文字内容
    private String content;
    // 评价创建时间
    private Date createTime;
    // 修改时间
    private Date lastEditTime;
}
