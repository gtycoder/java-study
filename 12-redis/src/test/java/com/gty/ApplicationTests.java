package com.gty;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisCluster;

@SpringBootTest
class ApplicationTests {
    /*@Autowired
    private JedisCluster jedisCluster;*/

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


/*    @Test
    void test01() {
        String set = jedisCluster.set("key", "value");
        System.out.println(set);
    }*/

    @Test
    void test02() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        ValueOperations<String, Object> string = redisTemplate.opsForValue();
        string.set("key", "value");
    }

    @Test
    void test03() {
        ValueOperations<String, Object> string = redisTemplate.opsForValue();
        Object key = string.get("key");
        System.out.println(key);
    }

}
