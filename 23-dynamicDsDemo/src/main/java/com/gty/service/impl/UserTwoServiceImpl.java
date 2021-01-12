package com.gty.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.gty.domain.UserTwo;
import com.gty.mapper.UserTwoMapper;
import com.gty.service.UserTwoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gaotainyu
 * @since 2020-12-15
 */
@Service
@DS("mysql_two")
public class UserTwoServiceImpl extends ServiceImpl<UserTwoMapper, UserTwo> implements UserTwoService {
    @Resource
    private UserTwoMapper userTwoMapper;

    @Override
    public List<UserTwo> getAllTwo() {
        return userTwoMapper.getAllTwo();
    }
}
