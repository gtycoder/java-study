package com.gty.controller;

import com.gty.dao.TestMapping;
import com.gty.domain.DockerProp;
import com.gty.domain.Person;
import com.gty.util.RedisConn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    private TestMapping testMapping;
    @Autowired
    private DockerProp dockerProp;

    //只发布一个docker
    @RequestMapping("/one")
    public String test01() {
        return "说明docker容器发布成功了.........";
    }

    //添加一个Redis容器
    @RequestMapping("/setRedis")
    public String test02() {
        System.out.println("setRedissetRedissetRedissetRedissetRedissetRedissetRedis");
        Jedis jedis = RedisConn.jedis();
        jedis.set("dockerKey", "这是从Redis中取出的数据");
        return "设置完成";
    }

    //添加一个Redis容器
    @RequestMapping("/getRedis")
    public String test03() {
        System.out.println("getRedisgetRedisgetRedisgetRedisgetRedisgetRedisgetRedisgetRedis");
        Jedis jedis = RedisConn.jedis();
        String dockerKey = jedis.get("dockerKey");
        return dockerKey;
    }

    @RequestMapping("/two")
    public String test04() {
        System.out.println("twotwotwotwotwo");
        return "twotwotwotwotwo";
    }

    @RequestMapping("/setMysql")
    public String test05() {
        Person person = new Person();
        person.setName("这是docker中的用户");
        person.setAge(19);
        person.setSex("docker");
        int i =  testMapping.addPerson(person);
        if (i==1) {
            return "设置完成";
        }
        return "设置失败了=============";
    }

    @RequestMapping("/getMysql")
    public List<Person> getPerson() {
        List<Person> personList = testMapping.getAllPerson();
        return personList;
    }

    @RequestMapping("/getProp")
    public String getProp() {
        return dockerProp.getContent();
    }

}
