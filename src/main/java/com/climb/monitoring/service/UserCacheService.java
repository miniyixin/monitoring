package com.climb.monitoring.service;

import com.climb.monitoring.annotation.DeleteCache;
import com.climb.monitoring.constants.CacheConstants;
import com.climb.monitoring.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author xin.yi
 */
@Service
public class UserCacheService {


    /**
     * 查找
     * 先查缓存，如果查不到，会查数据库并存入缓存
     */
    @Cacheable(value = CacheConstants.TEST_CACHE, key = "#id", sync = true)
    public User getUser(Long id){
        System.out.println("查询数据库：" + id);
        User user = new User();
        user.setId(id);
        user.setName("test");
        return user;
    }

    /**
     * 更新/保存
     */
    @DeleteCache(value = CacheConstants.TEST_CACHE, key = "#user.id")
    public void saveOrUpdateUser(User user){
        System.out.println("保存或更新数据库" + user.getId());
    }

    /**
     * 删除
     */
    @DeleteCache(value = CacheConstants.TEST_CACHE, key = "#user.id")
    public void delUser(User user){
        System.out.println("删除数据库");
    }

    /**
     * 删除
     */
    @DeleteCache(value = CacheConstants.TEST_CACHE, key = "#id")
    public void delUser(Long id){
        System.out.println("删除数据库");
    }

}

