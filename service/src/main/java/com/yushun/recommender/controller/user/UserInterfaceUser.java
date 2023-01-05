package com.yushun.recommender.controller.user;

import com.yushun.recommender.common.helper.JwtHelper;
import com.yushun.recommender.common.result.Result;

import com.yushun.recommender.model.user.user.User;
import com.yushun.recommender.vo.user.user.UserAuthVo;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * welcome controller
 * </p>
 *
 * @author yushun zeng
 * @since 2022-12-30
 */

@CrossOrigin
@RestController
@RequestMapping("/authentication")
public class UserInterfaceUser {
    @PostMapping("/googleLogin")
    public Result login(User user) {
        String token = JwtHelper.createToken(user.getEmail(), user.getUsername());

        UserAuthVo userAuthVo = new UserAuthVo();
        BeanUtils.copyProperties(user, userAuthVo);
        userAuthVo.setToken(token);

        return Result.ok(userAuthVo).message("Successfully login");
    }
}
