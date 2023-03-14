package com.climb.monitoring.listener;

import com.climb.monitoring.config.CacheConfig;
import com.climb.monitoring.constants.RedisCache;
import com.climb.monitoring.util.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author xin.yi
 */
@Component
@Slf4j
public class RedisCacheListener implements ApplicationRunner, Ordered {

    @Autowired
    private RedissonClient redisson;

    @Override
    public void run(ApplicationArguments applicationArguments){
        RTopic topic = redisson.getTopic(RedisCache.REDIS_CACHE_TOPIC);
        topic.addListener(String.class, (channel, msg) -> {
            CacheManager cacheManager = SpringBeanUtil.getBean("caffeineCacheManager", CacheManager.class);
            String[] split = msg.split(CacheConfig.DOUBLE_COLON);
            Cache cache = cacheManager.getCache(split[0]);
            evictOrClear(cache, split);
            log.info("{} 缓存清除完成", msg);
        });
    }

    private void evictOrClear(Cache cache, String[] split) {
        Objects.requireNonNull(cache);
        if (split.length > 1) {
            cache.evict(split[1]);
        } else {
            cache.clear();
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}


