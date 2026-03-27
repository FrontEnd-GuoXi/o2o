create table `tb_cart` (
    `cart_id` int(10) primary key auto_increment,
    `count` int(3) not null ,
    `create_time` datetime not null,
    `last_edit_time` datetime,
    `product_id` int(10) not null ,
    `user_id` int(10) not null,
    constraint `fk_cart_product_id` foreign key
        (`product_id`) references tb_product(`product_id`)
) ENGINE = InnoDB
  auto_increment = 1
  default charset = utf8;


alter table `tb_cart`
add constraint `fk_cart_user_id` foreign key (`user_id`) references tb_person_info(`user_id`),
add unique index `uk_user_product` (`user_id`, `product_id`);




ALTER TABLE tb_cart
MODIFY COLUMN create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;