-- 订单表：记录交易主信息
CREATE TABLE `tb_order`
(
    `order_id`       bigint(20)     NOT NULL AUTO_INCREMENT,
    `buyer_id`       int(10)        NOT NULL COMMENT '买家ID',
    `shop_id`        int(10)        NOT NULL COMMENT '店铺ID',
    `order_no`       varchar(64)    NOT NULL COMMENT '订单号',
    `total_price`    decimal(12, 2) NOT NULL COMMENT '订单总金额',
    `order_status`   int(2)         NOT NULL DEFAULT '0' COMMENT '0:未支付, 1:已支付, 2:已发货, 3:已完成, 4:已评价, -1:已取消',
    `create_time`    datetime       NOT NULL,
    `last_edit_time` datetime                DEFAULT NULL,
    PRIMARY KEY (`order_id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    CONSTRAINT `fk_order_buyer` FOREIGN KEY (`buyer_id`) REFERENCES `tb_person_info` (`user_id`),
    CONSTRAINT `fk_order_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- 订单详情表：记录具体卖出了多少货物，用于计算单品销售额
CREATE TABLE `tb_order_item`
(
    `order_item_id`   bigint(20)     NOT NULL AUTO_INCREMENT,
    `order_id`        bigint(20)     NOT NULL COMMENT '所属订单ID',
    `product_id`      int(10)        NOT NULL COMMENT '商品ID',
    `product_name`    varchar(200)   NOT NULL COMMENT '商品名称快照',
    `product_img`     varchar(1024)           DEFAULT NULL COMMENT '商品图片快照',
    `unit_price`      decimal(12, 2) NOT NULL COMMENT '成交单价',
    `quantity`        int(5)         NOT NULL COMMENT '购买数量',
    `total_price`     decimal(12, 2) NOT NULL COMMENT '该项总额',
    `create_time`     datetime       NOT NULL,
    PRIMARY KEY (`order_item_id`),
    CONSTRAINT `fk_item_order` FOREIGN KEY (`order_id`) REFERENCES `tb_order` (`order_id`),
    CONSTRAINT `fk_item_product` FOREIGN KEY (`product_id`) REFERENCES `tb_product` (`product_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


