package com.gty.server;

import com.gty.listener.event.UserAddEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServer {
    //已经存在的用户名,出现重复则触发listener
    private List<String> userList=new ArrayList<>(Arrays.asList("qqq","www","eee"));

    @Autowired
    private ApplicationContext applicationContext;

    public String addUser(String name) {
        UserAddEvent userAddEvent = new UserAddEvent(applicationContext, name);
        for (String username : userList) {
            if (username.equals(name)) {
                //如果出现相同的名字,就触发listen,(发布事件给listener)
                applicationContext.publishEvent(userAddEvent);
            }
        }
        return name;
    }
}
