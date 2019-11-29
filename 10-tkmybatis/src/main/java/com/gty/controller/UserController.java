package com.gty.controller;

import com.gty.domain.User;
import com.gty.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;
import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {

    @Resource
    private UserMapper userMapper;

    @GetMapping("/get")
    public Object get() {
        List<User> list = userMapper.selectAll();
        return list;
    }

    @GetMapping("/add")
    public Object add() {
        User user = new User("tkmybatis",11,"未知");
        int i = userMapper.insertSelective(user);
        return i;
    }

}

