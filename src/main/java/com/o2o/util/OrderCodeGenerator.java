package com.o2o.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class OrderCodeGenerator {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random random = new Random();

    public static String generateOrderSn(Long userId) {
        // 1. 时间戳 (14位)
        String dateTime = sdf.format(new Date());

        // 2. 用户ID后4位 (不足4位补0)
        String userSuffix = String.format("%04d", userId % 10000);

        // 3. 随机数 (3位)
        int rd = random.nextInt(900) + 100; // 100-999

        return dateTime + userSuffix + rd;
    }
}
// 生成结果示例：202604031530220886123
