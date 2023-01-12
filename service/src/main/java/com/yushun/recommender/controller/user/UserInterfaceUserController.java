package com.yushun.recommender.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yushun.recommender.security.helper.JwtHelper;
import com.yushun.recommender.security.result.Result;

import com.yushun.recommender.model.user.user.User;
import com.yushun.recommender.service.user.UserInterfaceUserService;
import com.yushun.recommender.vo.user.user.UserGoogleLoginVo;
import com.yushun.recommender.vo.user.user.UserReturnVo;
import com.yushun.recommender.vo.user.user.UserSystemRegisterVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * Welcome Controller
 * </p>
 *
 * @author yushun zeng
 * @since 2022-12-30
 */

@CrossOrigin
@RestController
@RequestMapping("/authentication")
public class UserInterfaceUserController {
    @Autowired
    private UserInterfaceUserService userInterfaceUserService;

    @PostMapping("/systemRegister")
    public Result systemRegister(UserSystemRegisterVo userSystemRegisterVo) {
        System.out.println(userSystemRegisterVo);

        return Result.fail().message("Successfully register");
    }

    @PostMapping("/googleLogin")
    public Result googleLogin(UserGoogleLoginVo userGoogleLoginVo) {
        // generate token
        String token = JwtHelper.createToken(userGoogleLoginVo.getEmail(), userGoogleLoginVo.getUsername());

        // form return data
        UserReturnVo userReturnVo = new UserReturnVo();
        BeanUtils.copyProperties(userGoogleLoginVo, userReturnVo);
        userReturnVo.setToken(token);

        // find if the user is first time sign in
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", userGoogleLoginVo.getEmail());

        User findUser = userInterfaceUserService.getOne(userWrapper);

        if(findUser == null) {
            // create new google user
            User storeUser = new User();
            BeanUtils.copyProperties(userGoogleLoginVo, storeUser);
            storeUser.setPolicy("F");
            storeUser.setType("G");
            storeUser.setCreateTime(new Date());
            storeUser.setUpdateTime(new Date());
            storeUser.setIsDeleted(0);

            boolean save = userInterfaceUserService.save(storeUser);

            if(!save) return Result.fail().message("Login failed");
        }

        return Result.ok(userReturnVo).message("Successfully login");
    }
}
