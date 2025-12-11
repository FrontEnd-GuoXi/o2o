CREATE TABLE `tb_user_identity`
(
    `id`             int(10)      NOT NULL AUTO_INCREMENT,
    `user_id`        int(10)      NOT NULL,
    `identity_type` varchar(20)  NOT NULL COMMENT '登录类型：password，wechat等',
    `identifier`    varchar(100) NOT NULL COMMENT '唯一标识：如账号、微信openid',
    `credential`     varchar(255) COMMENT '凭证：密码哈希值或者access_token',
    `create_time`    datetime     NOT NULL,
    primary key (`id`),
    CONSTRAINT `fk_user_info` FOREIGN KEY (`user_id`) REFERENCES tb_person_info (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

RENAME TABLE `tb_user_indentity` TO `tb_user_identity`;
ALTER TABLE `tb_user_identity`
    CHANGE indentity_type identity_type varchar(20) not null COMMENT '登录类型：password，wechat等',
    CHANGE indentifier identifier varchar(100) not null COMMENT '唯一标识：如账号、微信openid';

ALTER TABLE `tb_user_identity` ADD COLUMN last_edit_time datetime;

ALTER TABLE `tb_user_identity` ADD UNIQUE (`identifier`);