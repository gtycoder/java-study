package com.gty.realm;

import com.gty.dao.RolesMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

public class MyJdbcRealm extends AuthorizingRealm {
    @Resource
    private RolesMapper rolesMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("============用户权限==============");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        /*获取当前的用户*/
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        /*查询用户的权限*/
        String role = rolesMapper.getRole(username);
        /*将role放在一个集合中,多个权限使用集合set*/
        Set<String> rolesSet = new HashSet<>();
        rolesSet.add(role);
        //如果是管理员登录,可以访问所有的权限
        if ("admin".equals(role)) {
            rolesSet.add("user");
            rolesSet.add("guest");
        }
        info.addRoles(rolesSet);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("============用户验证==============");
        //从token中获取信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        String password = rolesMapper.getPassword(username);

        if (null == password) {
            throw new AuthenticationException("用户名不对");
        } else if (!password.equals(new String(token.getPassword()))){
            throw new AuthenticationException("密码不对");
        }
        //组合一个验证信息
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(token.getPrincipal(),password,getName());
        return info;
    }
}

