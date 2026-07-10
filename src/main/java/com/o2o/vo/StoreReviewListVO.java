package com.o2o.vo;


import lombok.Data;
import java.util.Date;

@Data
public class StoreReviewListVO {

    // 主键ID
    private Integer evaluationId;
    private Long userId;
    private String name;
    private String profileImg;

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
