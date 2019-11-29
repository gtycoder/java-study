package com.gty;

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
    void contextLoads() {
        User userById = userController.getUserById(1);

        User userById1 = userController.getUserById(1);

        User userById2 = userController.getUserById(1);

    }

}
