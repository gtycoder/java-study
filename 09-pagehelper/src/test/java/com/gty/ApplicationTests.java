package com.gty;

import com.github.pagehelper.PageInfo;
import com.gty.controller.UserController;
import com.gty.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ApplicationTests {
    @Autowired
    private UserController userController;
    @Test
    void testOne() {
        PageInfo<User> one = userController.getOne(2, 4);
        System.out.println(one.toString());
        List<User> list = one.getList();
        list.forEach(user->{
            System.out.println(user.toString());
        });
    }

    @Test
    void testTwo() {
        PageInfo<User> two = userController.getTwo(2, 4);
        System.out.println(two.toString());
        List<User> list = two.getList();
        list.forEach(user->{
            System.out.println(user.toString());
        });
    }

}
