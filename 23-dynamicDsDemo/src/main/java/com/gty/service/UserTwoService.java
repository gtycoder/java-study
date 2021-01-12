package com.gty.service;

import com.gty.domain.UserTwo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaotainyu
 * @since 2020-12-15
 */
public interface UserTwoService extends IService<UserTwo> {

    List<UserTwo> getAllTwo();

}
