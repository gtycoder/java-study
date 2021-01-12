package com.gty.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.gty.domain.UserOne;
import com.gty.mapper.UserOneMapper;
import com.gty.service.UserOneService;
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
@DS("mysql_one")
public class UserOneServiceImpl extends ServiceImpl<UserOneMapper, UserOne> implements UserOneService {
    @Resource
    private UserOneMapper userOneMapper;


    @Override
    public List<UserOne> getAll() {
        return userOneMapper.getAll();
    }
}
