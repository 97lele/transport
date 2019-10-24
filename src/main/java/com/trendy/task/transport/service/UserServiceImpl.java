package com.trendy.task.transport.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trendy.task.transport.mapper.UserMapper;
import com.trendy.task.transport.model.User;
import org.springframework.stereotype.Service;

/**
 * @author: lele
 * @date: 2019/10/24 下午2:47
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService{
}
