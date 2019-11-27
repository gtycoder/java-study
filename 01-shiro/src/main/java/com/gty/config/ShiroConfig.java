package com.gty.config;

import com.gty.realm.MyJdbcRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    /**
     * 创建自定义的验证规则
     */
    @Bean
    public Realm MyJdbcRealm() {
        return new MyJdbcRealm();
    }

    /**
     * 创建安全管理,注意创建实现了web的对象
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(MyJdbcRealm());
        return manager;
    }

    /**
     * 创建shiro的过滤器,定义过滤规则
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean filter = new ShiroFilterFactoryBean();
        filter.setSecurityManager(securityManager());

        //拦截到没有登录后跳到哪里去登录
        filter.setLoginUrl("/notLogin");

        //拦截没有权限的用户跳到哪里去
        filter.setUnauthorizedUrl("/noAuthorize");

        /*进行权限的控制,必须使用LinkHashMap,shrio要按照顺序进行设置*/
        Map<String, String> authMap = new LinkedHashMap<>();

        //游客不拦截
        authMap.put("/guest/**", "anon");
        //只允许user权限的访问
        authMap.put("/user/**", "roles[user]");
        //只允许admin权限的访问
        authMap.put("/admin/**", "roles[admin]");
        //登录页面不可以拦截,
        authMap.put("/login", "anon");
        /*最后在写剩下的所有全部拦截,否则会造成拦截所有的url*/
        authMap.put("/**", "authc");

        filter.setFilterChainDefinitionMap(authMap);
        System.out.println("---------------shirofactory创建成功");
        return filter;
    }
}