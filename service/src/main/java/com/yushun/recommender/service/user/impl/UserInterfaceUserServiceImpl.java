package com.yushun.recommender.service.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yushun.recommender.mapper.user.UserInterfaceUserMapper;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.service.user.UserInterfaceUserService;
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
public class UserInterfaceUserServiceImpl extends ServiceImpl<UserInterfaceUserMapper, User> implements UserInterfaceUserService {

}
