package com.gty.realm;

import com.gty.dao.RolesMapper;
import com.gty.domain.Roles;
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
        System.out.println("============用户授权==============");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        /*获取当前的用户,已经登录后可以使用在任意的地方获取用户的信息*/
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        /*查询用户的权限*/
        Roles role = rolesMapper.getRole(username);
        /*将role放在一个集合中,多个权限使用集合*/
        info.addRole(role.getRole());
        return info;
    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("============用户验证==============");
        //从token中获取信息,此token只是shiro用于身份验证的,并非前端传过来的token.
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        System.out.println(username);
        String password = rolesMapper.getPassword(username);

        if (null == password) {
            throw new AuthenticationException("doGetAuthenticationInfo中的用户名不对");
        } else if (!password.equals(new String(token.getPassword()))){
            throw new AuthenticationException("doGetAuthenticationInfo中的密码不对");
        }
        //组合一个验证信息
        System.out.println("token.getPrincipal()默认返回的username======"+token.getPrincipal());
        System.out.println("getName()"+getName());
        SimpleAuthenticationInfo info =
                new SimpleAuthenticationInfo(token.getPrincipal(),password,getName());
        return info;
    }

}

