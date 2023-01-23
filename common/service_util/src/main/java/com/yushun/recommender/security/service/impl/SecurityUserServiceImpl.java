package com.yushun.recommender.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yushun.recommender.security.mapper.SecurityUserMapper;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.security.service.SecurityUserService;

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
public class SecurityUserServiceImpl extends ServiceImpl<SecurityUserMapper, User> implements SecurityUserService {

}
