package com.gty.util;

import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;

/**
 * 这里可以直接使用@Cache注解,但是我更喜欢使用工具类,比较直观
 */
public class EhCacheUtil {

    public static void setCache(EhCacheCacheManager cache, String key, Object object) {
        //这里的 myCache是配置文件中的cache的name,是使用不同的缓存策略
        Cache objectCache = cache.getCache("myCache");
        objectCache.put(key,object);
    }

    public static Object getCache(EhCacheCacheManager cache,String key) {
        Cache objectCache = cache.getCache("myCache");
        if (objectCache.get(key)!=null && !objectCache.get(key).equals("")) {
            return objectCache.get(key).get();
        }
        return null;
    }
}

