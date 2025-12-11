CREATE
    DATABASE o2o;
SHOW
    DATABASES;
SHOW
    TABLES;
use
    o2o;

CREATE TABLE `tb_area`
(
    `area_id`        int(2)       NOT NULL auto_increment,
    `area_name`      varchar(200) NOT NULL,
    `priority`       int(2)       NOT NULL default '0',
    `create_time`    date         NOT NULL,
    `last_edit_time` date         NOT NULL,
    primary key (`area_id`),
    unique key `UK_AREA` (`area_name`)
) ENGINE = InnoDB
  auto_increment = 1
  default charset = utf8;



CREATE TABLE `tb_person_info`
(
    `user_id`        int(2)       NOT NULL auto_increment,
    `name`           varchar(200) NOT NULL,
    `profile_img`    varchar(1024)         DEFAULT NULL,
    `gender`         varchar(2)            DEFAULT NULL,
    `enable_status`  int(2)       NOT NULL DEFAULT '0' COMMENT '0：禁止使用，1：允许使用',
    `user_type`      int(2)       NOT NULL DEFAULT '1' COMMENT '1：顾客，2：店家，3：超级管理员',
    `create_time`    datetime     NOT NULL,
    `last_edit_time` datetime     NOT NULL,
    primary key (`user_id`)
) ENGINE = InnoDB
  auto_increment = 1
  default charset = utf8;


CREATE TABLE `wechat_auth`
(
    `wechat_auth_id` int(8)        NOT NULL,
    `open_id`        varchar(1024) NOT NULL,
    `create_time`    datetime      NOT NULL,
    `user_id`        int(2),
    primary key (`wechat_auth_id`),
    foreign key (`user_id`) references tb_person_info (`user_id`)
) ENGINE = InnoDB
  auto_increment = 1
  default charset = utf8;
ALTER TABLE `wechat_auth` RENAME TO `tb_wechat_auth`;


ALTER TABLE `wechat_auth`
    ADD CONSTRAINT `unique_open_id` UNIQUE (`open_id`);
SELECT CONSTRAINT_NAME
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_NAME = 'wechat_auth'
  AND REFERENCED_TABLE_NAME IS NOT NULL;

ALTER TABLE `wechat_auth`
    DROP FOREIGN KEY `foreign_user_id`;
ALTER TABLE `wechat_auth`
    ADD CONSTRAINT `fk_wechat_auth_user_id` FOREIGN KEY (`user_id`) REFERENCES tb_person_info (`user_id`);



CREATE TABLE `local_auth`
(
    `local_auth_id`  int(8)      NOT NULL auto_increment,
    `username`       varchar(16) NOT NULL,
    `password`       varchar(16) NOT NULL,
    `create_time`    datetime    NOT NULL,
    `last_edit_time` datetime    NOT NULL,
    `user_id`        int(2),
    primary key (`local_auth_id`),
    foreign key (`user_id`) references tb_person_info (`user_id`)
) ENGINE = InnoDB
  auto_increment = 1
  default charset = utf8;

SELECT CONSTRAINT_NAME
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_NAME = 'local_auth'
  AND REFERENCED_TABLE_NAME IS NOT NULL;

ALTER TABLE `local_auth`
    DROP FOREIGN KEY `local_auth_ibfk_1`;
ALTER TABLE `local_auth`
    ADD CONSTRAINT `fk_local_auth_user_id` FOREIGN KEY (`user_id`) REFERENCES tb_person_info (`user_id`);
ALTER TABLE `local_auth` RENAME TO `tb_local_auth`;


CREATE TABLE `head_line`
(
    `line_id`        int(100)      NOT NULL AUTO_INCREMENT,
    `line_name`      varchar(1000) NOT NULL,
    `line_link`      varchar(2000) NOT NULL,
    `priority`       int(2)        NOT NULL,
    `enable_status`  int(2)        NOT NULL,
    `create_time`    datetime      NOT NULL,
    `last_edit_time` datetime      NOT NULL,
    primary key (`line_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT charset = utf8;
ALTER TABLE `head_line` RENAME TO `tb_head_line`;






CREATE TABLE `tb_shop`
(
    `shop_id`          int(10)       NOT NULL AUTO_INCREMENT,
    `shop_name`        varchar(200)  NOT NULL,
    `shop_desc`        varchar(1024),
    `shop_addr`        varchar(200)  NOT NULL,
    `phone`            varchar(16)   NOT NULL,
    `shop_img`         varchar(1024) NOT NULL,
    `priority`         int(2)        NOT NULL,
    `create_time`      datetime      NOT NULL,
    `last_edit_time`   datetime      NOT NULL,
    `enable_status`    int(2)        NOT NULL DEFAULT '-1',
    `advice`           varchar(500),
    `area_id`          int(3)        NOT NULL,
    `owner_id`         int(5)        NOT NULL,
    `shop_category_id` int(5)        NOT NULL,
    primary key (`shop_id`),
    CONSTRAINT `fk_area` FOREIGN KEY (`area_id`) REFERENCES tb_area (`area_id`),
    CONSTRAINT `fk_owner` FOREIGN KEY (`owner_id`) REFERENCES tb_person_info (`user_id`),
    CONSTRAINT `fk_shop_category` FOREIGN KEY (`shop_category_id`) REFERENCES tb_shop_category (`shop_category_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  default charset = utf8;






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

ALTER TABLE tb_shop
    MODIFY shop_category_id int(5) NULL;
ALTER TABLE tb_shop
    MODIFY area_id int(3) NULL;
ALTER TABLE tb_shop
    MODIFY shop_img varchar(1024) NULL;






ALTER TABLE `tb_person_info` MODIFY COLUMN user_id int(10) AUTO_INCREMENT;

DROP TABLE tb_local_auth;








