package com.yushun.recommender.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.vo.user.user.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

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

    Result userSystemRegister(UserSystemRegisterVo userSystemRegisterVo);

    Result tokenLoginRefresh(String token);


    Result getUserDetailByToken(String token);

    UserDetailReturnVo getUserDetailByEmail(String email);

    Result updateUserDetail(User user);

    Result updateUserAvatar(MultipartFile file, HttpServletRequest request);

    Result updateSystemUserPassword(UserUpdatePasswordVo userUpdatePasswordVo);

    boolean updateUserPassword(String username, String password, String email);
}
