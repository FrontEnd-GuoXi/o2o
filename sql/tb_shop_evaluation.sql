CREATE TABLE `tb_shop_evaluation` (
  `evaluation_id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `shop_id` int NOT NULL COMMENT '关联店铺ID',
  `user_id` int NOT NULL COMMENT '关联用户ID',
  `order_id` int DEFAULT NULL COMMENT '关联订单ID（O2O场景通常需要凭订单评价，防止恶意刷单）',
  `total_score` tinyint NOT NULL COMMENT '综合评分(1-5分)',
  `service_score` tinyint NOT NULL COMMENT '服务评分(1-5分)',
  `environment_score` tinyint NOT NULL COMMENT '环境评分(1-5分)',
  `content` varchar(500) DEFAULT NULL COMMENT '评价文字内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价创建时间',
  `last_edit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`evaluation_id`),
  -- 核心索引优化：高并发下按店铺分页查询评价列表
  KEY `idx_shop_create_time` (`shop_id`, `create_time`),
  -- 唯一约束：一个用户针对一个订单只能评价一次
  UNIQUE KEY `uk_user_order` (`user_id`, `order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店铺评分明细表';

ALTER TABLE `tb_shop` ADD COLUMN `avg_score` decimal(3,2) NOT NULL DEFAULT '5.00' COMMENT '店铺平均综合得分';
ALTER TABLE `tb_shop` ADD COLUMN `evaluation_count` int NOT NULL DEFAULT '0' COMMENT '总评价人数';