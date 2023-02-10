package com.yushun.recommender.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.vo.user.user.UserDetailReturnVo;
import com.yushun.recommender.vo.user.user.UserGoogleLoginVo;
import com.yushun.recommender.vo.user.user.UserSystemLoginVo;

/**
 * <p>
 * User Interface User Service
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-5
 */

public interface UserService extends IService<User> {
    Result googleLogin(UserGoogleLoginVo userGoogleLoginVo);

    Result userSystemLogin(UserSystemLoginVo userSystemLoginVo);

    Result getUserDetailByToken(String token);

    UserDetailReturnVo getUserDetailByEmail(String email);
}
