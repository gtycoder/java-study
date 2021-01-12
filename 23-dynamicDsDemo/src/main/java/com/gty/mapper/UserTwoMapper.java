package com.gty.mapper;

import com.gty.domain.UserTwo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gaotainyu
 * @since 2020-12-15
 */
public interface UserTwoMapper extends BaseMapper<UserTwo> {

    List<UserTwo> getAllTwo();
}
