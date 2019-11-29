package com.gty.dao;

import com.gty.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> getAllUser();

    User getUserById(int id);
}
