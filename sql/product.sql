CREATE TABLE `tb_product_category`
(
    `product_category_id`   int(3)      NOT NULL AUTO_INCREMENT,
    `shop_id`               int(10)     NOT NULL,
    `product_category_name` varchar(50) NOT NULL,
    `priority`              int(3)      NOT NULL,
    `create_time`           datetime    NOT NULL,
    PRIMARY KEY (`product_category_id`),
    CONSTRAINT `fk_shop_owner` FOREIGN KEY (`shop_id`) REFERENCES tb_shop (`shop_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

  CREATE TABLE `tb_product`
(
    `product_id`          int(5)        NOT NULL AUTO_INCREMENT,
    `product_name`        varchar(50)   NOT NULL,
    `product_desc`        varchar(500),
    `img_addr`            varchar(1024) NOT NULL,
    `normal_price`        varchar(20)   NOT NULL,
    `promotion_price`     varchar(20),
    `priority`            int(2)        NOT NULL,
    `create_time`         datetime      NOT NULL,
    `last_edit_time`      datetime      NOT NULL,
    `enable_status`       int(2)        NOT NULL,
    `product_category_id` int(3)        NOT NULL,
    `shop_id`             int(10)       NOT NULL,
    PRIMARY KEY (`product_id`),
    CONSTRAINT `fk_product_category` FOREIGN KEY (`product_category_id`) REFERENCES tb_product_category (`product_category_id`),
    CONSTRAINT `fk_product_shop_owner` FOREIGN KEY (`shop_id`) REFERENCES tb_shop (`shop_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

  ALTER TABLE `tb_prouduct` ADD COLUMN `product_number` int DEFAULT 0 NOT NULL COMMENT '商品数量';


CREATE TABLE `tb_product_img`
(
    `product_img_id` int(10)       NOT NULL AUTO_INCREMENT,
    `img_addr`       varchar(1024) NOT NULL,
    `img_desc`       varchar(500),
    `priority`       int(2)        NOT NULL,
    `create_time`    datetime      NOT NULL,
    `product_id`     int(10)       NOT NULL,
    primary key (`product_img_id`),
    CONSTRAINT `fk_img_owner` FOREIGN KEY (`product_id`) REFERENCES tb_product (`product_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;