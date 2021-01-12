package com.gty;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@MapperScan(basePackages = "com.gty.mapper")
public class DynamicDsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicDsDemoApplication.class, args);
    }

}
