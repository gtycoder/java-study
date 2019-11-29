package com.gty.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gty.dao.UserDao;
import com.gty.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserDao userDao;

    /**
     * 这是一种比较复杂的分页方式,使用的是PageHelper类,对下一条SQL进行分页
     */
    @GetMapping("/getOne/{pageNo}/{pageSize}")
    public PageInfo<User> getOne(@PathVariable("pageNo")Integer pageNo, @PathVariable("pageSize")Integer pageSize) {
        //使用restful风格的方式接受参数.
        log.info("当前页码pageNo==="+pageNo+"````显示条数pageSize===="+pageSize);

        //将参数封装到Pagehelper,注意:当前设置只对下一个SQL语句生效,并且在mapper.xml中的SQL语句不要写分号.
        PageHelper.startPage(pageNo, pageSize);
        List<User> userList = userDao.getAllUserOne();

        //将返回的list封装成一个PageInfo<T>
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        return pageInfo;
    }

    /**
     * 这种方式可以在接收中显示的传递分页信息,但是只能使用pageNum,pageSize两个名字
     * 并且可以直接封装为Page<E>分页信息类
     */
    @GetMapping("/getTwo/{pageNum}/{pageSize}")
    public PageInfo<User> getTwo(@PathVariable("pageNum")Integer pageNum, @PathVariable("pageSize")Integer pageSize) {
        //也可以显示的传递分页信息,但是在dao层中就只能使用pageNum和pageSize这两个固定的参数名
        Page<User> userList = userDao.getAllUserTwo(pageNum,pageSize);
        System.out.println("1111"+userList.toString());

        //将返回的list封装成一个PageInfo<T>
        PageInfo<User> pageInfo = new PageInfo<>(userList);

        //这个SQL就不会分页.
        //Page<User> userList2 = userDao.getAllUser();
        //System.out.println("22222"+userList2);

        return pageInfo;
    }
}

