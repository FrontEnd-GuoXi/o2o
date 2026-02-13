package com.o2o.util;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Spring上下文加载监听器，用于验证Spring Context是否正确加载
 */
@Component
public class SpringContextLoaderListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(SpringContextLoaderListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 获取上下文名称
        String contextName = event.getApplicationContext().getDisplayName();
        logger.info("✅ Spring Context 已成功加载: {}", contextName);
        
        // 检查是否为根上下文
        if (contextName.contains("Root WebApplicationContext")) {
            logger.info("🎉 根Spring上下文加载完成，应用可以正常使用了！");
        }
    }
}