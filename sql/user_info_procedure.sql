-- 1. 如果之前有同名的存储过程，先删除
DROP PROCEDURE IF EXISTS batch_init_o2o_users_correct;

-- 2. 创建存储过程
DELIMITER $$
CREATE PROCEDURE batch_init_o2o_users_correct()
BEGIN
    -- 定义循环变量
    DECLARE i INT DEFAULT 1;
    -- 定义临时变量，用来存放副表 tb_user_info 生成的 user_id
    DECLARE current_user_id INT;

    -- 开启事务，100 条数据一眨眼就能插完
    START TRANSACTION;

    -- 循环 100 次，生成 100 套账号
    WHILE i <= 100 DO

        -- 【步骤 A】先往副表 tb_user_info 插入详细信息
        -- 此时 tb_user_info 的 user_id 是主键（自增）
            SET current_date = NOW();
            INSERT INTO tb_person_info (name, gender, enable_status, last_edit_time, create_time)
            VALUES (
                       CONCAT('测试用户', i),
                        '1',
                        1,
                       current_date,
                       current_date
                   );

            -- 【步骤 B】精准捞出刚刚副表 tb_user_info 自动生成的自增主键 user_id
            SET current_user_id = LAST_INSERT_ID();

            -- 【步骤 C】拿着这个 user_id，去插入主表 tb_user
            -- 此时 tb_user 的 id 是自增的（不用传），但必须把 current_user_id 传给它的外键字段 user_id
            INSERT INTO tb_user_identity (user_id, identity_type, identifier, credential, create_time, last_edit_time)
            VALUES (
                       current_user_id,         -- 核心：将副表的主键，作为外键填入主表
                       'password',
                       CONCAT('test_user_', i), -- 登录账号
                       'password123',           -- 登录密码（需匹配你后端的加密逻辑）
                       current_date,
                       current_date
                   );

            -- 变量递增
            SET i = i + 1;
        END WHILE;

    -- 3. 统一提交事务
    COMMIT;

END $$
DELIMITER ;

-- 4. 运行存储过程
CALL batch_init_o2o_users_correct();