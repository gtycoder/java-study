package com.gty.controller;

import com.gty.dao.UserMapper;
import com.gty.domain.User;
import com.gty.util.EhCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.KeyGenerator;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private EhCacheCacheManager ehCacheCache;
    @Autowired
    private UserMapper userMapper;

    /**
     * 使用工具类的方式获取
     */
    @GetMapping("/get")
    public List<User> get() {
        //先查询缓存
        List<User> list = (List) EhCacheUtil.getCache(ehCacheCache, "userList");
        if (list != null) {
            //如果有,遍历查看
            list.forEach(user -> {
                log.info("=========" + user.toString());
            });

        } else {
            //如果没有就去数据库查看
            log.info("========这里去了数据库");
            list = userMapper.getAllUser();
            //将list存入缓存,使用相同的key哦
            EhCacheUtil.setCache(ehCacheCache, "userList", list);
        }
        return list;
    }

    /**
     * 使用注解使用EhCache
     * 获取数据 @Cacheable(value = "myCache")
     * 更新/新增数据 @CachePut(value = "myCache22")
     * 删除数据 @CacheEvict(value = "myCache22")
     */

}

