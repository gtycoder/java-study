package com.gty.dao;

import com.github.pagehelper.Page;
import com.gty.domain.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserDao {
    List<User> getAllUserOne();
    Page<User> getAllUserTwo(int pageNum, int pageSize);
}
