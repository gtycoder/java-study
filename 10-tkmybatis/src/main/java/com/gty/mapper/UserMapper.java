package com.gty.mapper;

import com.gty.domain.User;
import org.apache.ibatis.annotations.Mapper;

//使用@Mapper注解单独标记,避免在主类使用@MapperScan
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
