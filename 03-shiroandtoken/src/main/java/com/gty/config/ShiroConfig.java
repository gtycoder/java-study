package com.gty.config;

import com.gty.filter.ClientShiroFilter;
import com.gty.realm.MyJdbcRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Bean
    public Realm MyJdbcRealm() {
        return new MyJdbcRealm();
    }
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(MyJdbcRealm());
        return manager;
    }
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean filter = new ShiroFilterFactoryBean();
        filter.setSecurityManager(securityManager());

        //加入自定义的filter
        Map<String, Filter> filterMap = filter.getFilters();
        filterMap.put("client", new ClientShiroFilter());
        filter.setFilters(filterMap);

        //定义相关路径
        filter.setLoginUrl("/login");
        filter.setUnauthorizedUrl("/noAuthorize");

        //定义拦截路径,记得将静态资源也排除过滤
        /*进行权限的控制,必须使用LinkHashMap,shrio要按照顺序进行设置*/
        Map<String, String> authMap = new LinkedHashMap<>();
        authMap.put("/guest/**", "anon");
        authMap.put("/static/**", "anon");
        authMap.put("/user/**", "client,roles[user]");
        authMap.put("/admin/**", "client,roles[admin]");
        authMap.put("/login", "anon");
        authMap.put("/**", "client");
        filter.setFilterChainDefinitionMap(authMap);

        //配置完成
        System.out.println("---------------shirofactory创建成功");
        return filter;

    }
}