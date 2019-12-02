package com.gty.utils;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.locks.ReentrantLock;

public class RedissonRedLock {

    private static RedissonClient redissonClient;
    private static ReentrantLock lock=new ReentrantLock();

    public static RedissonClient getClient() {
        if (null==redissonClient) {
            try {
                lock.lock();
                Config config = new Config();
                config.useSingleServer().setAddress("redis://192.168.25.25:6379");
                redissonClient = Redisson.create(config);
                System.out.println(redissonClient);
            }finally {
                lock.unlock();
            }
        }
        return redissonClient;
    }


}
