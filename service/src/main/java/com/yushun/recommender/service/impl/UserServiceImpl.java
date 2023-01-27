package com.yushun.recommender.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yushun.recommender.mapper.UserMapper;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * User Interface User Service Impl
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-5
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
