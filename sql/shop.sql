CREATE TABLE `tb_shop_category`
(
    `shop_category_id`   int(3)        NOT NULL AUTO_INCREMENT,
    `shop_category_name` varchar(200)  NOT NULL,
    `shop_category_img`  varchar(2000) NOT NULL,
    `priority`           int(2)        NOT NULL,
    `create_time`        datetime      NOT NULL,
    `last_edit_time`     datetime      NOT NULL,
    `parent_id`          int(3),
    primary key (`shop_category_id`),
    CONSTRAINT `fk_shop_category_id` FOREIGN KEY (`parent_id`) REFERENCES tb_shop_category (`shop_category_id`)
) ENGINE = InnoDB
  auto_increment = 1
  default charset = utf8;

INSERT INTO tb_shop_category (
    shop_category_name,
    shop_category_img,
    priority,
    create_time,
    last_edit_time,
    parent_id
) VALUES
-- 顶级分类 1: 电子产品
( '电子产品', 'img_electronics.jpg', 1, NOW(), NOW(), NULL ),
-- 顶级分类 2: 服装服饰
( '服装服饰', 'img_clothing.jpg', 2, NOW(), NOW(), NULL ),
-- 顶级分类 3: 家居用品
( '家居用品', 'img_home.jpg', 3, NOW(), NOW(), NULL ),

-- 子分类：电子产品 -> 手机
( '手机', 'img_phone.jpg', 1, NOW(), NOW(), 1 ),
-- 子分类：电子产品 -> 平板电脑
( '平板电脑', 'img_tablet.jpg', 2, NOW(), NOW(), 1 ),
-- 子分类：电子产品 -> 智能穿戴
( '智能穿戴', 'img_wearable.jpg', 3, NOW(), NOW(), 1 ),

-- 子分类：服装服饰 -> 男装
( '男装', 'img_men.jpg', 1, NOW(), NOW(), 2 ),
-- 子分类：服装服饰 -> 女装
( '女装', 'img_women.jpg', 2, NOW(), NOW(), 2 ),
-- 子分类：服装服饰 -> 童装
( '童装', 'img_kids.jpg', 3, NOW(), NOW(), 2 ),

-- 子分类：家居用品 -> 家具
( '家具', 'img_furniture.jpg', 1, NOW(), NOW(), 3 ),
-- 子分类：家居用品 -> 家电
( '家电', 'img_appliance.jpg', 2, NOW(), NOW(), 3 ),
-- 子分类：家居用品 -> 装饰
( '装饰', 'img_decor.jpg', 3, NOW(), NOW(), 3 ),

-- 三级分类：手机 -> 智能手机
( '智能手机', 'img_smartphone.jpg', 1, NOW(), NOW(), 4 ),
-- 三级分类：手机 -> 功能手机
( '功能手机', 'img_featurephone.jpg', 2, NOW(), NOW(), 4 ),

-- 三级分类：男装 -> 上衣
( '上衣', 'img_top.jpg', 1, NOW(), NOW(), 7 ),
-- 三级分类：男装 -> 裤子
( '裤子', 'img_pants.jpg', 2, NOW(), NOW(), 7 ),

-- 三级分类：家具 -> 沙发
( '沙发', 'img_sofa.jpg', 1, NOW(), NOW(), 10 ),
-- 三级分类：家具 -> 床
( '床', 'img_bed.jpg', 2, NOW(), NOW(), 10 ),

-- 三级分类：家电 -> 冰箱
( '冰箱', 'img_refrigerator.jpg', 1, NOW(), NOW(), 11 ),
-- 三级分类：家电 -> 洗衣机
( '洗衣机', 'img_washer.jpg', 2, NOW(), NOW(), 11 );