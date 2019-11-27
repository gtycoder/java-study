package com.gty.service;

import com.gty.domain.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public User getUserByName(String username) {
        //模拟数据库查询
        return new User(2, "zhangsan");
    }
}
