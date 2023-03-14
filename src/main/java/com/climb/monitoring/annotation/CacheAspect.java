package com.climb.monitoring.annotation;


import com.climb.monitoring.config.CacheConfig;
import com.climb.monitoring.constants.RedisCache;
import com.climb.monitoring.util.ExpressionUtil;
import com.climb.monitoring.util.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class CacheAspect {

    @Autowired
    private RedissonClient redissonClient;

    @Pointcut("@annotation(com.climb.monitoring.annotation.DeleteCache)")
    public void fun() {}

    /**
     * 主要是做节点同步。若没有cacheKey将会把整个缓存Map给清除掉
     */
    @AfterReturning(value = "fun()", returning = "object")
    public void doAfter(JoinPoint joinPoint, Object object) {
        RTopic topic = redissonClient.getTopic(RedisCache.REDIS_CACHE_TOPIC);
        CacheManager cacheManager = SpringBeanUtil.getBean("caffeineCacheManager", CacheManager.class);
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        DeleteCache deleteCache = method.getAnnotation(DeleteCache.class);
        String value = deleteCache.value();
        String key = deleteCache.key();
        Object cacheKey = ExpressionUtil.isEl(key) ? ExpressionUtil.parse(deleteCache.key(), method, joinPoint.getArgs()) : key;
        String pushMsg;

        Cache cache = cacheManager.getCache(value);
        if (ObjectUtils.isEmpty(cache)) return;

        if (ObjectUtils.isEmpty(cacheKey)) {
            cache.clear();
            pushMsg = value + CacheConfig.DOUBLE_COLON;
        } else {
            cache.evict(cacheKey);
            pushMsg = value + CacheConfig.DOUBLE_COLON + cacheKey;
        }
        try {
            // todo 后续是否对多节点返回数据监控
            topic.publish(pushMsg);
        } catch (Exception e) {
            log.info("分布式缓存刷新通知异常,缓存 {}", value, e);
            // 缓存删除失败监控
        }
    }

}

