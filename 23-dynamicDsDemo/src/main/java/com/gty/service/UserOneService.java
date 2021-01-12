package com.gty.service;

import com.gty.domain.UserOne;
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
public interface UserOneService extends IService<UserOne> {

    List<UserOne> getAll();

}
