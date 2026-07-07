package com.o2o.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShopEvaluationDTO {

    // 关联店铺ID
    @NotNull(message = "shopId不能为空")
    private Long shopId;
    // 关联用户ID
    @NotNull(message = "userId不能为空")
    private Long userId;
    // 关联订单ID
    @NotNull(message = "orderId不能为空")
    private Long orderId;
    // 综合评分(1-5分)
    @NotNull(message = "totalScore不能为空")
    private Integer totalScore;
    // 服务评分(1-5分)
    @NotNull(message = "serviceScore不能为空")
    private Integer serviceScore;
    // 环境评分(1-5分)
    @NotNull(message = "environmentScore不能为空")
    private Integer environmentScore;
    // 评价文字内容
    @NotNull(message = "content不能为空")
    private String content;

}
