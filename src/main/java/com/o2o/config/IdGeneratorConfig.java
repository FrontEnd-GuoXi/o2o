package com.o2o.config;

import com.o2o.util.SnowflakeIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdGeneratorConfig {

    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator() {
        // 机器 ID：可通过配置文件、IP 哈希、或部署脚本指定（0~1023）
        long machineId = 1; // 生产环境建议动态获取
        return new SnowflakeIdGenerator(machineId);
    }
}
