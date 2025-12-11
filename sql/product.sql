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