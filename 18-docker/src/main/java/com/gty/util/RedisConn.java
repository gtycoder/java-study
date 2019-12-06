package com.gty.util;

import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Set;

public class RedisConn {
    private static JedisPool jedisPool;
    private static JedisPoolConfig config = new JedisPoolConfig();

    static {
        config.setMaxTotal(10);
        config.setMaxIdle(3);
        config.setTestOnBorrow(true);
    }

    /**
     * redis集群模式连接
     */
    public static JedisCluster jedisCluster() {
        // 集群模式
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();

        HostAndPort hostAndPort1 = new HostAndPort("127.0.0.1", 6379);
        HostAndPort hostAndPort2 = new HostAndPort("服务器地址2", 6379);
        HostAndPort hostAndPort3 = new HostAndPort("服务器地址3", 6379);

        nodes.add(hostAndPort1);
        nodes.add(hostAndPort2);
        nodes.add(hostAndPort3);

        JedisCluster jedisCluster = new JedisCluster(nodes, config);
        String key = jedisCluster.get("key");
        System.out.println(key);
        return jedisCluster;
    }

    /**
     * redis单机模式连接
     */
    public static Jedis jedis() {

        //唯一实例
        if (jedisPool == null) {
            synchronized (RedisConn.class) {
                if (jedisPool == null) {
                    jedisPool = new JedisPool(config, "redis44", 6379);
                }
            }
        }

        Jedis jedis = jedisPool.getResource();
        //jedis.set("key", "value");
        //String key = jedis.get("key");
        //System.out.println(key);
        return jedis;
    }

    public static void close(Jedis jedis) {
        if (null != jedis) {
            jedis.close();
        }
        if (null != jedisPool) {
            jedisPool.close();
        }
    }

    public static void close(JedisCluster jedisCluster) {
        if (null != jedisCluster) {
            try {
                jedisCluster.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Jedis jedis = jedis();
        close(jedis);
    }

}
