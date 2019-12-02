package com.gty.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.SetParams;

import java.util.concurrent.locks.ReentrantLock;

public class JedisLock {
    private static JedisPool jedisPool;
    //实现单例jedisPool
    private static ReentrantLock lock = new ReentrantLock();

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(1001);
        config.setMaxIdle(40);
        config.setTestOnBorrow(true);

        if (null == jedisPool) {
            try {
                lock.lock();
                jedisPool = new JedisPool(config, "192.168.25.25", 6379);
                System.out.println(jedisPool);
            } finally {
                lock.unlock();
            }
        }
    }

    public static String getLock(String key) {
        String value = null;
        //获取客户端
        Jedis jedis = jedisPool.getResource();
        String random = UUIDUtil.getUUID();
        try {
            //配置参数nx以及ex失效时间
            SetParams params = new SetParams();
            params.nx().ex(10);
            //循环获取锁,直到获取到才跳出
            for (; ; ) {
                //注意:如果直接使用setnx,需要保证原子操作(使用事务)
                String set = jedis.set(key, random, params);
                //成功之后返回"OK"
                if ("OK".equals(set)) {
                    System.out.println("random===" + random);
                    value = random;
                    break;
                }
                //休眠一下,避免过于激烈的争抢
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            //关闭客户端
            jedis.close();
        }
        return value;
    }

    public static void delLock(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            //这里必须是原子操作,使用redis事务
            jedis.watch(key);
            if (value.equals(jedis.get(key))) {
                Transaction multi = jedis.multi();
                multi.del(key);
                //执行事务累计的操作
                multi.exec();
            }
            jedis.unwatch();
        } finally {
            jedis.close();
        }
    }
}
