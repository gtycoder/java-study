package com.gty.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RolesMapper {
    String getRole(String username);

    String getPassword(String username);
}
