
package com.gty.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;


/**
 * redis的缓存客户端，同时支持redis集群和单机环境
 */

//@Configuration
//@ConditionalOnClass({JedisCluster.class})
public class CacheClusterClient {

    @Autowired
    protected JedisPoolConfig redisPoolConfig;

    private volatile String clusters;

    @Value("${redis.address}")
    private String redisAddress;

    @PostConstruct
    public void init() {
        if (clusters == null) {
            clusters = redisAddress;
        }
    }

    @Bean
    public JedisCluster getJedisCluster() {
        Set<HostAndPort> jedisClusterNodes = new HashSet<>();
        System.out.println("============" + clusters);
        if (clusters.contains(",")) {
            //说明是集群模式
            getClusterNodes(jedisClusterNodes, clusters);
            return new JedisCluster(jedisClusterNodes, redisPoolConfig);
        }
        //说明是单机模式,要开启集群连接模式,虽然是单机,但是要使用集群的方式连接
        return new JedisCluster(getClusterNodes(clusters));
    }

    //创建集群的HostAndPort对象
    private void getClusterNodes(Set<HostAndPort> jedisClusterNodes, String clusters) {
        Stream.of(clusters.split(",")).forEach(t -> {
            String[] cluster = t.split(":");
            jedisClusterNodes.add(new HostAndPort(cluster[0], Integer.parseInt(cluster[1])));
        });
    }

    //创建单机的HostAndPort对象
    private HostAndPort getClusterNodes(String clusters) {
        String[] cluster = clusters.split(":");
        return new HostAndPort(cluster[0], Integer.parseInt(cluster[1]));
    }

    //redispoolconfig配置
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        /*连接池耗尽时是否阻塞*/
        config.setBlockWhenExhausted(false);
        /*连接池耗尽时最大连接时间*/
        config.setMaxWaitMillis(1000);
        /*最大连接数*/
        config.setMaxTotal(200);
        config.setMaxIdle(40);
        config.setMinIdle(0);
        return config;
    }
}

