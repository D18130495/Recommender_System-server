package com.yushun.recommender.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yushun.recommender.security.mapper.UserMapper;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.security.service.UserService;

import org.springframework.stereotype.Service;

/**
 * <p>
 * User Service Impl
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-13
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
