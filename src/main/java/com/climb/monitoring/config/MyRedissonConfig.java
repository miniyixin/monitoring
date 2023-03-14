package com.climb.monitoring.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRedissonConfig {
    @Bean
    public RedissonClient getRedisson() {
        // 默认连接地址 127.0.0.1:6379
//    RedissonClient redisson = Redisson.create();
        //1.创建配置
        //Redis url should start with redis:// or rediss:// (for SSL connection)
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        //2.根据config创建处RedissonClient实例
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}

