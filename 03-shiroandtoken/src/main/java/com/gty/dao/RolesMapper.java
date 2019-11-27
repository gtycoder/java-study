package com.gty.dao;

import com.gty.domain.Roles;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RolesMapper {
    Roles getRole(String username);

    String getPassword(String username);
}
